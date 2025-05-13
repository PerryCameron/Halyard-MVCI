package org.ecsail.mvci_connect;

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
        // Custom CookieJar to store cookies in memory
        CookieJar cookieJar = new CookieJar() {
            private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), new ArrayList<>(cookies));
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        };

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
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
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl + "login")
                .post(formBody)
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

    public Response makeRequest(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(serverUrl + endpoint)
                .get()
                .build();

        return client.newCall(request).execute();
    }
}
