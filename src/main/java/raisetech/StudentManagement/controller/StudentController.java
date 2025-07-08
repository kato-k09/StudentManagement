package raisetech.StudentManagement.controller;

import java.util.Arrays;
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

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
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

  // 29課題　更新処理
  // 個人受講生情報の更新入力フォームを表示
  @GetMapping("/individualStudent")
  public String individualStudent(@RequestParam int id, Model model) {

    Student individualStudent = service.searchIndividualStudent(
        id); // パラメーターから受け取ったidに紐づいた受講生情報を取得
    List<StudentsCourses> individualStudentCourses = service.searchIndividualStudentCourses(
        id); // パラメーターから受け取ったid(studentId)に紐づいたコース情報を取得

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(individualStudent);
    studentDetail.setStudentsCourses(individualStudentCourses);

    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  // ブラウザから更新ボタンを押したときに実行
  // フォームの入力をstudentDetailとして取得しサービス層に渡す
  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "error"; // エラーがあったらエラー画面表示
    }

    service.updateStudent(studentDetail);

    return "redirect:/studentList";
  }

  // 課題外実装　課題28の時作成、以後リファクタリング実施。
  @GetMapping("/individualStudentCourses")
  public String showStudentCourse(@RequestParam int studentId, Model model) {

    // studentIdに紐づいたコース情報のみをstudentCoursesに入れる
    List<StudentsCourses> studentCourses = service.searchIndividualStudentCourses(studentId);

    // 個人のコース情報のみを表示するHTMLを表示する
    model.addAttribute("individualStudentCourses", studentCourses);
    return "individualStudentCourses";
  }

}
