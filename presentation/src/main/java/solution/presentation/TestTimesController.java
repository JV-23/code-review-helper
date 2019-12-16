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
public class TestTimesController {

	// inject via application.properties
	@Value("${welcome.message}")
	private String message;
	
	/*@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("message", message);
	    //model.addAttribute("tasks", tasks);
	
	return "home"; //view
	}*/
	
	// /hello?name=kotlin
	@GetMapping("/stabletesttimes")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") 
			String name, Model model) {

	
		return "stableTestTimes"; //view
	}

}
