package raisetech.StudentManagement;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentsRepository studentsRepository;
  @Autowired
  private CoursesRepository coursesRepository;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  // 21_実際に構築するWebアプリの解説とテーブル設計

  // 受講生情報登録
  @PostMapping("/studentManage")
  public void registerStudent(String id, String fullName, String furigana, String nickName,
      String mail, String region, int age, String gender) {

    if (id == null) {
      return; // パラメータにidが無ければ終了
    }

    if (!studentsRepository.isStudent(id)) {
      studentsRepository.registerStudent(id, fullName, furigana, nickName, mail, region, age,
          gender); // 登録済みでないIDのときのみ登録する
    }
  }

  // コース情報登録
  @PostMapping("/courseManage")
  public void registerCourse(String id, String course, java.sql.Date startDate,
      java.sql.Date scheduledEndDate) {

    if (id == null || course == null) {
      return; // パラメータにid or courseが無ければ終了
    }

    if (!coursesRepository.isCourse(id, course)) {
      coursesRepository.registerCourse(id, course, startDate, scheduledEndDate);
    } // 登録済みでないID、コースのときのみ登録する
  }

  // 検索
  @GetMapping("/studentManage")
  public String searchStudent(@RequestParam String fullName) {

    Student showStudent = studentsRepository.searchStudent(fullName); // フルネームから受講生情報を取得
    List<Course> showCourses = coursesRepository.searchCourse(
        showStudent.getId()); // 紐づけられたIDのコース情報を取得

    return "受講生情報\nID: " + showStudent.getId() + " 名前: " + showStudent.getFullName()
        + " フリガナ: "
        + showStudent.getFurigana() + " ニックネーム: " + showStudent.getNickName() + " メール: "
        + showStudent.getMail() + " 居住地: " + showStudent.getRegion() + " 年齢: "
        + showStudent.getAge() + " 性別: " + showStudent.getGender() + "\n"
        + showCourses.stream()
        .map(v -> "コース: " + v.getCourse() + " 受講開始日: " + v.getStartDate() + " 受講終了予定日: "
            + v.getScheduledEndDate())
        .collect(Collectors.joining("\n")); // 受講生、コース情報を出力
  }

  // 受講生情報更新
  @PatchMapping("/studentManage")
  public void updateStudent(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String fullName,
      @RequestParam(required = false) String furigana,
      @RequestParam(required = false) String nickName,
      @RequestParam(required = false) String mail,
      @RequestParam(required = false) String region,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String gender) {

    if (id == null) {
      return; // パラメータにidが無ければ終了
    }

    Student updateStudent = new Student();
    updateStudent.setId(id);

    if (fullName != null) {
      updateStudent.setFullName(fullName);
    }
    if (furigana != null) {
      updateStudent.setFurigana(furigana);
    }
    if (nickName != null) {
      updateStudent.setNickName(nickName);
    }
    if (mail != null) {
      updateStudent.setMail(mail);
    }
    if (region != null) {
      updateStudent.setRegion(region);
    }
    if (age != null) {
      updateStudent.setAge(age);
    }
    if (gender != null) {
      updateStudent.setGender(gender);
    }

    studentsRepository.updateStudent(updateStudent);

  }

  // コース情報更新
  @PatchMapping("/courseManage")
  public void updateCourse(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String course,
      @RequestParam(required = false) java.sql.Date startDate,
      @RequestParam(required = false) java.sql.Date scheduledEndDate) {

    if (id == null || course == null) {
      return; // パラメータにid or courseが無ければ終了
    }

    Course updateCourse = new Course();
    updateCourse.setId(id);
    updateCourse.setCourse(course);

    if (startDate != null) {
      updateCourse.setStartDate(startDate);
    }
    if (scheduledEndDate != null) {
      updateCourse.setScheduledEndDate(scheduledEndDate);
    }

    coursesRepository.updateCourse(updateCourse);
  }

  // 学生情報削除
  @DeleteMapping("/studentManage")
  public void deleteStudent(String id) {

    if (id == null) {
      return; // パラメータにidが無ければ終了
    }
    studentsRepository.deleteStudent(id);
    coursesRepository.deleteAllCourse(id);
  }

  // コース情報削除
  @DeleteMapping("/courseManage")
  public void deleteCourse(String id, String course) {

    if (id == null || course == null) {
      return; // パラメータにid or courseが無ければ終了
    }
    coursesRepository.deleteCourse(id, course);
  }
}
