package org.ecsail.static_tools;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.logging.HttpLoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.ecsail.mvci_main.MainModel;
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
        Request request = new Request.Builder()
                .url(serverUrl + "auth-check")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                Map<String, Boolean> result = objectMapper.readValue(
                        response.body().string(),
                        new TypeReference<>() {
                        }
                );
                return result.getOrDefault("requiresAuth", true);
            }
            signalError("Failed to check authentication status: " + response.code());
            throw new IOException("Failed to check authentication status: " + response.code());
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

        return client.newCall(request).execute();
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
    }

    public Response makeRequest(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + endpoint)
                .get()
                .build();

        logger.debug("Sending request to {} with cookies: {}", endpoint, client.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.parse(serverUrl + endpoint))));
        return client.newCall(request).execute();
    }

    public String fetchDataFromHalyard(String endpoint) throws Exception {
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
            signalError(e.getMessage());
            throw new Exception("Failed to fetch data: " + e.getMessage());
        }
    }

    private void signalError(String message) {
        mainModel.toggleClientConnectError();
        mainModel.errorMessageProperty().set(message);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
