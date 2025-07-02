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

  // 登録
  @PostMapping("/studentManage")
  public void registerStudent(String id, String fullName, String furigana, String nickName,
      String mail, String region, int age, String gender, String course, java.sql.Date startDate,
      java.sql.Date scheduledEndDate) {

    if (!studentsRepository.isStudent(id)) {
      studentsRepository.registerStudent(id, fullName, furigana, nickName, mail, region, age,
          gender);
    }
    if (!coursesRepository.isCourse(id, course)) {
      coursesRepository.registerCourse(id, course, startDate, scheduledEndDate);
    }
  }

  // 検索
  @GetMapping("/studentManage")
  public String searchStudent(@RequestParam String fullName) {
    Student showStudent = studentsRepository.searchStudent(fullName);
    List<Course> showCourses = coursesRepository.searchCourse(showStudent.getId());
    return "受講生情報\nID: " + showStudent.getId() + " 名前: " + showStudent.getFullName()
        + " フリガナ: "
        + showStudent.getFurigana() + " ニックネーム: " + showStudent.getNickName() + " メール: "
        + showStudent.getMail() + " 居住地: " + showStudent.getRegion() + " 年齢: "
        + showStudent.getAge() + " 性別: " + showStudent.getGender() + "\n"
        + showCourses.stream()
        .map(v -> "コース: " + v.getCourse() + " 受講開始日: " + v.getStartDate() + " 受講終了予定日: "
            + v.getScheduledEndDate())
        .collect(Collectors.joining("\n"));
  }

  // 更新
  @PatchMapping("/studentManage")
  public void updateStudent(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String fullName,
      @RequestParam(required = false) String furigana,
      @RequestParam(required = false) String nickName,
      @RequestParam(required = false) String mail,
      @RequestParam(required = false) String region,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) String course,
      @RequestParam(required = false) java.sql.Date startDate,
      @RequestParam(required = false) java.sql.Date scheduledEndDate) {
    Student updateStudent = new Student();

    updateStudent.setId(id);

    if (id == null) {
      return;
    }
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

    Course updateCourse = new Course();
    updateCourse.setId(id);
    if (course != null) {
      updateCourse.setCourse(course);
    }
    if (startDate != null) {
      updateCourse.setStartDate(startDate);
    }
    if (scheduledEndDate != null) {
      updateCourse.setScheduledEndDate(scheduledEndDate);
    }

    studentsRepository.updateStudent(updateStudent);
    if (course == null) {
      return;
    }
    coursesRepository.updateCourse(updateCourse);

  }

  // 削除
  @DeleteMapping("/studentManage")
  public void deleteStudent(String id) {
    studentsRepository.deleteStudent(id);
    coursesRepository.deleteCourse(id);
  }
}
