package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    model.addAttribute("studentsCourseList",
        studentsCourses);
    return "studentsCourseList";
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
      return "error"; // エラーがあったらエラー画面表示
    }

    service.registerStudent(studentDetail);

    return "redirect:/studentList";
  }

  // studentList内のUUIDクリックでUUIDに紐づいた受講生コース情報を表示
  @GetMapping("/individualStudentCourses")
  public String showStudentCourse(@RequestParam String studentId, Model model) {

    // studentIdに紐づいたコース情報のみをstudentCoursesに入れる
    List<StudentsCourses> studentCourses = service.searchIndividualStudentCoursesService(studentId);

    // 個人のコース情報のみを表示するHTMLを表示する
    model.addAttribute("individualStudentCourses", studentCourses);
    return "individualStudentCourses";
  }


}
