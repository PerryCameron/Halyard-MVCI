package org.ecsail.static_tools;

import org.ecsail.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class VersionUtil {

    public static Logger logger = LoggerFactory.getLogger(VersionUtil.class);
    private static final String VERSION_FILE = "/version.properties";

    public static String getVersion() {
        try {
            String version = getProperty("version", "Unknown");
            // Remove leading "v"
            if (version.startsWith("v")) {
                version = version.substring(1);
            }

            // Remove commit hash (-gxxxxxxx)
            int commitHashIndex = version.indexOf("-g");
            if (commitHashIndex != -1) {
                version = version.substring(0, commitHashIndex);
            }
            return version;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Unknown";
        }
    }

    public static String getBuildTimestamp() {
        return getProperty("build.timestamp", "Unknown");
    }

    public static String getJavaVersion() {
        return getProperty("java.version", "Unknown");
    }

    private static String getProperty(String key, String defaultValue) {
        Properties props = new Properties();
        try (InputStream input = VersionUtil.class.getResourceAsStream(VERSION_FILE)) {
            if (input == null) {
                return defaultValue;
            }
            props.load(input);
            return props.getProperty(key, defaultValue);
        } catch (IOException ex) {
            ex.printStackTrace();
            return defaultValue;
        }
    }

    public static void logAppVersion() {
        logger.info("Starting Halyard Application version: {}", getVersion());
    }
}
