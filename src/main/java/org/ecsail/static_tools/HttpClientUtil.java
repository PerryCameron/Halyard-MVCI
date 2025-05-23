package org.ecsail.static_tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.logging.HttpLoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.ecsail.mvci.main.MainModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final MainModel mainModel;
    private String serverUrl;
    private String csrfToken; // Store CSRF token
    private String csrfHeaderName; // Store CSRF header name (e.g., X-CSRF-TOKEN) // ADD THIS



    public HttpClientUtil(MainModel mainModel) {
        this.mainModel = mainModel;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger::debug);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieJar cookieJar = new CookieJar() {
            private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, @NotNull List<Cookie> cookies) {
                cookieStore.put(url.host(), new ArrayList<>(cookies));
                logger.info("Saved cookies for host {}: {}", url.host(), cookies);
            }

            @NotNull
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        };

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(loggingInterceptor)
                .build();

        objectMapper = new ObjectMapper();
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
    }

    public String getServerUrl() {
        return serverUrl;
    }

    /* Fetches the CSRF token from /open_api/csrf-token and stores it.
     * @throws IOException if the request fails or the response is invalid.
     */
    private void fetchCsrfToken() throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + "api/csrf-token")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String bodyString = response.body().string();
                try {
                    Map<String, String> result = objectMapper.readValue(bodyString, new TypeReference<>() {});
                    csrfToken = result.get("token");
                    csrfHeaderName = result.get("headerName");
                    logger.info("Fetched CSRF token: {}, header: {}", csrfToken, csrfHeaderName);
                    if (csrfToken == null || csrfHeaderName == null) {
                        throw new IOException("CSRF token or header name missing in response");
                    }
                } catch (JsonProcessingException e) {
                    logger.error("Failed to parse CSRF token JSON: {}", e.getMessage());
                    throw new IOException("Invalid CSRF token response", e);
                }
            } else {
                logger.error("Failed to fetch CSRF token: code={}", response.code());
                throw new IOException("Failed to fetch CSRF token: " + response.code());
            }
        }
    }

    /**
     * Checks if authentication is required by sending a GET request to the server's /auth-check endpoint.
     * The endpoint is expected to return a JSON response with a "requiresAuth" boolean field indicating
     * whether authentication is needed.
     *
     * @return true if authentication is required, false otherwise. Defaults to true if the "requiresAuth"
     * field is missing in the response.
     * @throws IOException if the request fails, the response is not successful, or the response body cannot
     *                     be read or parsed as JSON. The exception message includes the HTTP status code if
     *                     the response is not successful.
     */
    public boolean requiresAuthentication() throws IOException {
        String authUrl = serverUrl + "auth-check";
        logger.info("Checking authentication at: {}", authUrl);
        Request request = new Request.Builder()
                .url(authUrl)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String bodyString = response.body().string();
                try {
                    Map<String, Boolean> result = objectMapper.readValue(bodyString, new TypeReference<>() {
                    });
                    logger.info("Response: {}", bodyString);
                    logger.info("Server is reachable: true");
                    return result.getOrDefault("requiresAuth", true);
                } catch (JsonProcessingException e) {
                    logger.error("Failed to parse JSON: {}", e.getMessage());
                    throw new IOException("Invalid JSON response", e);
                }
            } else {
                String errorBody = response.body() != null ? response.body().string() : "No body";
                logger.error("Failed to check authentication: code={}, headers={}, body={}",
                        response.code(), response.headers(), errorBody);
                if (response.code() == 429) {
                    logger.error("Too many sessions");
                    // Trigger max sessions dialog in caller
                }
                signalError("Failed to check authentication status: " + response.code());
                throw new IOException("Failed to check authentication status: " + response.code());
            }
        } catch (IOException e) {
            logger.error("Network error connecting to server: {}", e.getMessage());
            throw e;
        }
    }

    public Response login(String username, String password) throws IOException {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(loginRequest),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(serverUrl + "api/login")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            fetchCsrfToken(); // Fetch CSRF token after successful login
        }

        return response;
//        return client.newCall(request).execute();
    }

    public Response logoutOthers() throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + "logout-others")
                .post(new FormBody.Builder().build())
                .build();
        logger.debug("Sending logout-others request");
        return client.newCall(request).execute();
    }

    public void logout() throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + "logout")
                .post(new FormBody.Builder().build())
                .build();
        logger.debug("Sending logout request");
        try (Response response = client.newCall(request).execute()) {
            logger.info("Logout response status: {}", response.code());
        }
        CookieJar cookieJar = client.cookieJar();
        cookieJar.saveFromResponse(Objects.requireNonNull(HttpUrl.parse(serverUrl)), new ArrayList<>());
        csrfToken = null; // Clear CSRF token on logout
    }

    public Response makeRequest(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + endpoint)
                .get()
                .build();
        logger.debug("Sending request to {} with cookies: {}", endpoint, client.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.parse(serverUrl + endpoint))));
        return client.newCall(request).execute();
    }

    public String fetchDataFromGybe(String endpoint) throws Exception {
        requiresAuthentication();
        try (Response response = makeRequest("halyard/" + endpoint)) {
            logger.info("Fetching data from /halyard/{}: Status {}", endpoint, response.code());
            String contentType = response.header("Content-Type", "");
            assert contentType != null;
            if (contentType.contains("text/html")) {
                signalError("Session invalid: Server redirected to login page. Please log in again.");
                throw new Exception("Session invalid: Server redirected to login page. Please log in again.");
            }
            if (response.code() == 403) {
                signalError("Access Denied: You don’t have the required permissions to access this resource. " + response.code());
                throw new AccessDeniedException("Access Denied: You don’t have the required permissions to access this resource.");
            } else if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                signalError("Failed to fetch data: " + response.code());
                throw new Exception("Failed to fetch data: " + response.code());
            }
        } catch (IOException e) {
            signalError("Failed to fetch data: " + e.getMessage());
            throw new Exception("Failed to fetch data: " + e.getMessage());
        }
    }

    /**
     * Sends a POST request to the specified halyard endpoint with the provided data as JSON.
     *
     * @param endpoint the endpoint under /halyard/ (e.g., "update/person")
     * @param data     the object to serialize as JSON for the request body
     * @return the response body as a string
     * @throws Exception if authentication is required but fails, the session is invalid,
     *                   access is denied, or the request fails
     */
    public String postDataToGybe(String endpoint, Object data) throws Exception {
        requiresAuthentication();
        if (csrfToken == null) {
            fetchCsrfToken(); // Ensure CSRF token is fetched
        }

        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(data);
            logger.debug("JSON Payload for POST: {}", jsonPayload);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize data to JSON", e);
            throw new Exception("Serialization error", e);
        }

        RequestBody body = RequestBody.create(
                jsonPayload,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(serverUrl + "halyard/" + endpoint)
                .post(body)
                .header(csrfHeaderName, csrfToken) // Use dynamic header name
                .build();

        try (Response response = client.newCall(request).execute()) {
            logger.info("Posting data to /halyard/{}: Status {}", endpoint, response.code());
            String contentType = response.header("Content-Type", "");
            if (contentType != null && contentType.contains("text/html")) {
                signalError("Session invalid: Server redirected to login page. Please log in again.");
                throw new Exception("Session invalid: Server redirected to login page. Please log in again.");
            }
            if (response.code() == 403) {
                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.contains("Invalid CSRF token")) {
                    logger.info("Invalid CSRF token, fetching new token");
                    fetchCsrfToken();
                    return postDataToGybe(endpoint, data); // Retry once
                }
                signalError("Access Denied: You don’t have the required permissions to access this resource. " + response.code());
                throw new AccessDeniedException("Access Denied: You don’t have the required permissions to access this resource.");
            } else if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                signalError("Failed to post data: " + response.code());
                throw new Exception("Failed to post data: " + response.code());
            }
        } catch (IOException e) {
            signalError("Failed to post data: " + e.getMessage());
            throw new Exception("Failed to post data: " + e.getMessage());
        }
    }

    private void signalError(String message) {
        mainModel.setConnectError(true);
        mainModel.errorMessageProperty().set(message);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
