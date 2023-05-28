package org.ecsail.static_calls;

import org.ecsail.mvci_roster.RosterInteractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HalyardPaths {

	private static final Logger logger = LoggerFactory.getLogger(HalyardPaths.class);


	public static final String LOGFILEDIR = System.getProperty("user.home") + "/.ecsc/logs";
	public static final String DEPOSITREPORTPATH = System.getProperty("user.home") + "/Documents/ECSC/Depost_Reports";
	public static final String RENEWALFORM = System.getProperty("user.home") + "/Documents/ECSC/Renewal_Forms";
	public static final String SQLBACKUP = System.getProperty("user.home") + "/Documents/ECSC/SQL_Backup";
	public static final String ROSTERS = System.getProperty("user.home") + "/Documents/ECSC/Rosters";
    public static final String ECSCHOME = System.getProperty("user.home") + "/Documents/ECSC";
	public static final String LOGO = "/ECSClogo4.png";
	public static final String HOSTS = System.getProperty("user.home") + "/.ecsc/hosts.ecs";
	public static final String TUPLECOUNTS = System.getProperty("user.home") + "/.ecsc/tuples.ecs";
	public static final String SLIPCHART = System.getProperty("user.home") + "/Documents/ECSC/SlipCharts";
	public static final String BOATDIR = System.getProperty("user.home") + "/Documents/ECSC/Boats";
 	public static final String DEFAULTPHOTO = "/personimg.png";
	public static final String BOATLISTS = System.getProperty("user.home") + "/Documents/ECSC/BoatLists";
	// "C:\\Users\\pcame\\Documents\\email.xlsx"
	
// 	private static boolean isDirEmpty(final Path directory) throws IOException {
// 	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
// 	        return !dirStream.iterator().hasNext();
// 	    }
// 	}
 	
 	public static String getFileExtension(File file) {
 	    String name = file.getName();
 	    int lastIndexOf = name.lastIndexOf(".");
 	    if (lastIndexOf == -1) {
 	        return ""; // empty extension
 	    }
 	    return name.substring(lastIndexOf);
 	}
 	
	public static ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> imageFiles = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	      //  if (fileEntry.isDirectory()) {
	      //      listFilesForFolder(fileEntry);
	      //  } else {
	        	imageFiles.add(fileEntry.getName());
	      //      System.out.println(fileEntry.getName());
	      //  }
	    }
		return imageFiles;    
	}
	
	public static void checkPath(String path) {
		File recordsDir = new File(path);
		if (!recordsDir.exists()) {
			logger.info("Creating dir: " + path);
		    recordsDir.mkdirs();
		}
	}
	
	public static boolean fileExists(String file) {
		File f = new File(file);
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		return false;
	}

	
	public static String getYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}
	
	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public static String printDate() {
		return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
	}

	public static String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
	
	public static boolean isWindows() {
		boolean result = false;
		if(System.getProperty("os.name").equals("Windows 10")) {
			result = true;
		}
		return result;
	}
	
	public static String getOperatingSystem() {
		return System.getProperty("os.name");
	}
	
}
//Documents