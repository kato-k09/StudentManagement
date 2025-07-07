package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {

    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/studentsCourseList")
  public String getStudentsCourseList(Model model) {

    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentsCoursesList",
        studentsCourses);
    return "studentsCoursesList";
  }


  @GetMapping("/newStudent")
  public String newStudent(Model model) {

    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String handleStudentRegistration(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent"; // エラーがあったら元の画面に戻す処理
    }

    // 新規受講生情報を登録する処理を実装する。
    // コース情報も一緒に登録できるように実装する。コースは単体で良い。
    service.registerStudent(studentDetail); // studentDetailにフォームに入力した情報が入り、それをサービスに繋ぐ。

    return "redirect:/studentList";
  }


}
