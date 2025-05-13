package org.ecsail.mvci_connect;
import okhttp3.logging.HttpLoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private String serverUrl;

    public HttpClientUtil() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> logger.debug(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieJar cookieJar = new CookieJar() {
            private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), new ArrayList<>(cookies));
                logger.info("Saved cookies for host {}: {}", url.host(), cookies);
            }

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

    public boolean requiresAuthentication() throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + "auth-check")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                Map<String, Boolean> result = objectMapper.readValue(response.body().string(), Map.class);
                return result.getOrDefault("requiresAuth", true);
            }
            throw new IOException("Failed to check authentication status: " + response.code());
        }
    }

    public Response login(String username, String password) throws IOException {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                objectMapper.writeValueAsString(loginRequest)
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

        return client.newCall(request).execute();
    }

    public void logout() throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + "logout")
                .post(new FormBody.Builder().build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            logger.info("Logout response status: {}", response.code());
        }

        CookieJar cookieJar = client.cookieJar();
        if (cookieJar instanceof CookieJar) {
            ((CookieJar) cookieJar).saveFromResponse(HttpUrl.parse(serverUrl), new ArrayList<>());
        }
    }

    public Response makeRequest(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + endpoint)
                .get()
                .build();

        return client.newCall(request).execute();
    }
}
