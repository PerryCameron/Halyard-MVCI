package org.ecsail.connection;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Mail {


    public static void composeEmail(String receiver, String subject, String body) {
        try {
            String mailto = String.format("mailto:%s?subject=%s&body=%s",
                    uriEncode(receiver),
                    uriEncode(subject),
                    uriEncode(body));

            Desktop.getDesktop().mail(new URI(mailto));
        } catch (Exception e) {
            throw new RuntimeException("Failed to open mail client", e);
        }
    }

    private static String uriEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
