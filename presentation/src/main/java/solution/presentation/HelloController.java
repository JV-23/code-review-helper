package solution.presentation;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.file.Paths;

@Controller
public class HelloController {

	// inject via application.properties
	@Value("${welcome.message}")
	private String message;
	
	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("message", message);
	    //model.addAttribute("tasks", tasks);
	
	return "home"; //view
	}
	
	// /hello?name=kotlin
	@GetMapping("/stablecoverage")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") 
			String name, Model model) {
		/*String line = new String();

		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\stableCoverage.json");
		FileSystemResource coverage = new FileSystemResource(file2);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(coverage.getInputStream()),1024);
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
			br.close();
			name = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("message", name);*/
	
		return "hello"; //view
	}

	@GetMapping("/pullcoverage")
	public String testing() {
		/*String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\population.csv");
		FileSystemResource coverage = new FileSystemResource(file2);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(coverage.getInputStream()),1024);
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
			br.close();
			name = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();;
		}*/
		return "pullcoverage";
	}
	
	@GetMapping("/coveragediff")
	public String diff() {
		/*String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\population.csv");
		FileSystemResource coverage = new FileSystemResource(file2);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(coverage.getInputStream()),1024);
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
			br.close();
			name = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();;
		}*/
		return "coveragediff";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/coverageonchanges")
	public String coverageOnChanges(Model model) throws ClassNotFoundException, IOException {
		Map<String, String> diff = new HashMap<String, String>();
		File file = new File(System.getProperty("user.dir"));
		FileInputStream fi = new FileInputStream(new File(file.getParent() + "\\core\\diffs"));
		ObjectInputStream oi = new ObjectInputStream(fi);

		// Read objects
		diff = (Map<String, String>) oi.readObject();
        JSONObject json = new JSONObject(diff);

		model.addAttribute("str", json.toString());

		oi.close();
		fi.close();
		
		//model.addAttribute("str", diff);
		
		/*String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\population.csv");
		FileSystemResource coverage = new FileSystemResource(file2);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(coverage.getInputStream()),1024);
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
			br.close();
			name = stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();;
		}*/
		return "coverageonchanges";
	}
}