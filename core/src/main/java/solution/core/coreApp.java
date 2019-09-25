package solution.core;

import solution.service.*;

/**
 * Hello world!
 *
 */
public class coreApp 
{
    public static void main( String[] args )
    {
    	serviceApp gitService = new serviceApp();
        try {
			//gitService.downloadStableVersion();
			gitService.runStableVersionTests();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
