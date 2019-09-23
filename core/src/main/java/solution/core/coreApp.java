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
			gitService.printPubRepositoryPRs();
		} catch (Exception e) {
			System.out.println("something broke");
		}
    }
}
