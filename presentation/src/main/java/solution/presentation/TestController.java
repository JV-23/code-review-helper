package solution.presentation;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class TestController {

	@RequestMapping("/api")
	public String index() {
		String line = new String();
		String name = new String();
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
			e.printStackTrace();;
		}
		return name;
	}
	
	@RequestMapping("/api2")
	public String index2() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\pullRequestCoverage.json");
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
	}

	@RequestMapping("/api3")
	public String index3() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\stableTestTimes.json");
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
	}
	
	@RequestMapping("/api4")
	public String index4() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\timeDifferences.json");
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
	}

	@RequestMapping("/api5")
	public String index5() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\coverageDifference.json");
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
	}
	
	@RequestMapping("/api6.svg")
	public String index6() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\stableflamegraph.svg");
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
	}
	
	@RequestMapping("/api7")
	public String index7() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\pullrequestflamegraph.svg");
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
	}
	
	@RequestMapping("/api8")
	public String index8() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\relatedFiles.json");
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
	}
	
	@RequestMapping("/api9")
	public String index9() {
		String line = new String();
		String name = new String();
		File file = new File(System.getProperty("user.dir"));
		File file2 = new File(file.getParent() + "\\core\\deadCode.json");
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
	}
}
