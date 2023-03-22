package org.ecsail.interfaces;

public interface ConfigFilePaths {

    String LOGIN_FILE = System.getProperty("user.home") + "/.halyard/login.objects";
    String SCRIPTS_FOLDER = System.getProperty("user.home") + "/.halyard/scripts";
    String LOG_FOLDER = System.getProperty("user.home") + "/.halyard/logs";
    String OUTPUT_FORMAT = "Path: %-30s -> File Extension: %s";
    String WINDOWS_FILE_SEPARATOR = "\\";
    String UNIX_FILE_SEPARATOR = "/";
    String FILE_EXTENSION = ".";
}
