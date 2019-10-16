package solution.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
	
	public List<File> getSubFolders(String dirName) throws Exception {
		File directory = new File(dirName);
	
	    List<File> resultList = new ArrayList<File>();
	
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isDirectory()) {
	        	resultList.add(file.getAbsoluteFile());
	            resultList.addAll(getSubFolders(file.getAbsolutePath()));
	        }
	    }
	    
	    return resultList;	
	}
}
