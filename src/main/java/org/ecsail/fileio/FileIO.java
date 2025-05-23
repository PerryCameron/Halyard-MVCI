package org.ecsail.fileio;


import org.ecsail.interfaces.ConfigFilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;



public class FileIO implements ConfigFilePaths {

	private static final Logger logger = LoggerFactory.getLogger(FileIO.class);

	public static boolean hostFileExists(String file) {
		boolean doesExist = false;
		File g = new File(file);
		if(g.exists())
			doesExist = true;
		return doesExist;
	}

	public static void checkPath(String path) {
		File recordsDir = new File(path);
		if (!recordsDir.exists()) {
			logger.info("Creating dir: " + path);
			recordsDir.mkdirs();
		}
	}

	public static void deleteFile(String path) {
		File fileToDelete = new File(path);
		if (fileToDelete.delete()) {
			logger.info("Deleted the file: " + fileToDelete.getName());
		} else {
			logger.info("Failed to delete the file: " + fileToDelete.getName());
		}
	}

	public static boolean fileExists(String file) {
		File f = new File(file);
		if(f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	public static void copyFile(File srcFile, File destFile) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(destFile);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isImageType(String srcPath) {
		String[] extensionsAllowed = {"jpg","jpeg","png","bmp","gif"};
		String extension = FileIO.getFileExtension(srcPath);
		for (String ex : extensionsAllowed) {
			if (ex.equals(extension.toLowerCase())) return true;
		}
		return false;
	}

	public static String getFileExtension(String fileName) {
		if (fileName == null) throw new IllegalArgumentException("fileName must not be null!");
		String extension = "";
		int indexOfLastExtension = fileName.lastIndexOf(FILE_EXTENSION);
		// check last file separator, windows and unix
		int lastSeparatorPosWindows = fileName.lastIndexOf(WINDOWS_FILE_SEPARATOR);
		int lastSeparatorPosUnix = fileName.lastIndexOf(UNIX_FILE_SEPARATOR);
		// takes the greater of the two values, which mean last file separator
		int indexOflastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);
		// make sure the file extension appear after the last file separator
		if (indexOfLastExtension > indexOflastSeparator) {
			extension = fileName.substring(indexOfLastExtension + 1);
		}
		return extension.toLowerCase();
	}
}
