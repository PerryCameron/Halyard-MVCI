package org.ecsail.interfaces;

public interface ConfigFilePaths {

    String LOGIN_FILE = System.getProperty("user.home") + "/.halyard/login.objects";
    String SCRIPTS_FOLDER = System.getProperty("user.home") + "/.halyard/scripts";
    String LOG_FOLDER = System.getProperty("user.home") + "/.halyard/logs";
    String OUTPUT_FORMAT = "Path: %-30s -> File Extension: %s";
    String WINDOWS_FILE_SEPARATOR = "\\";
    String UNIX_FILE_SEPARATOR = "/";
    String FILE_EXTENSION = ".";
    String ROSTERS = System.getProperty("user.home") + "/Documents/ECSC/Rosters";
    String DEFAULT_PHOTO = "/personimg.png";
    String BOAT_REMOTE_PATH = "/home/ecsc/ecsc_files/boat_images/";
    String BOAT_LOCAL_PATH = System.getProperty("user.home") + "/.ecsc/boat_images/";
    String IMAGE_REMOTE_PATH = "/home/ecsc/ecsc_files/boat_images/";
    String IMAGE_LOCAL_PATH = System.getProperty("user.home") + "/.ecsc/boat_images/";

}
