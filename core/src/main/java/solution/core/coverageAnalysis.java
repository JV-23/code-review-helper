package solution.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class coverageAnalysis {
	public void generateReports() throws Exception{
		ProcessBuilder proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd stableVersion && mvn jacoco:report");
        proc.redirectErrorStream(true);
        Process process = proc.start();

        // Read the output

        BufferedReader reader =  
              new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        process.waitFor();
        
        proc = new ProcessBuilder();
        proc.command("cmd.exe", "/c", "cd PRVersion && mvn jacoco:report");
        proc.redirectErrorStream(true);
        process = proc.start();

        // Read the output

        reader =  
              new BufferedReader(new InputStreamReader(process.getInputStream()));

        line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        process.waitFor();
	}
}
