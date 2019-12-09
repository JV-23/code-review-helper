package solution.presentation;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
	
	return "hello"; //view
	}
	
	// /hello?name=kotlin
	@GetMapping("/hello")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") 
			String name, Model model) {
		String line = new String();

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
		model.addAttribute("message", name);
	
		return "hello"; //view
	}

	/*@RequestMapping("/test")
	public String testing() {
		String line = new String();
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
		}
		return name;
	}*/
}