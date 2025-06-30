package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private String name = "Enami Kouji";
	private String age = "37";

	private Map<String, String> student = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		return name + " " + age + "歳";
	}
	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, String age) {
		this.name = name;
		this.age = age;
	}
	@PostMapping("/studentName")
	public void updateStudentName(String name) {
		this.name = name;
	}


	//student Mapで学生情報を管理
	@PostMapping("/getStudentMapInfo")
	public String getStudentMapInfo(String name) {
		return student.entrySet().stream()
				.filter(v -> v.getKey().equals(name))
				.map(v -> "氏名: " + v.getKey() + " 年齢: " + v.getValue())
				.findFirst()
				.orElse("該当者なし");

	}
	@PostMapping("/setStudentMapInfo")
	public void setStudentMapInfo(String name, String age) {
		this.student.put(name,age);
	}
}
