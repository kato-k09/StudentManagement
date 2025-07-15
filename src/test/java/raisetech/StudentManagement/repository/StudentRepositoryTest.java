package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4", "5"})
  void 登録されているIDの受講生検索が行えること(String id) {
    Student actual = sut.searchStudent(id);

    switch (id) {
      case "1" -> assertThat(actual.getName()).isEqualTo("佐藤泰");
      case "2" -> assertThat(actual.getName()).isEqualTo("井上孝治");
      case "3" -> assertThat(actual.getName()).isEqualTo("田中啓二");
      case "4" -> assertThat(actual.getName()).isEqualTo("後藤桜");
      case "5" -> assertThat(actual.getName()).isEqualTo("野村佳奈多");
    }
  }

  @Test
  void 登録されていないIDの受講生検索が行えないこと() {
    Student actual = sut.searchStudent("6");

    assertThat(actual).isNull();
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(6);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4", "5"})
  void 登録されている受講生IDで受講生コース情報の検索が行えること(String studentId) {
    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

    switch (studentId) {
      case "1" -> {
        assertThat(actual.get(0).getCourseName()).isEqualTo("Java");
        assertThat(actual.get(1).getCourseName()).isEqualTo("AWS");
      }
      case "2" -> {
        assertThat(actual.get(0).getCourseName()).isEqualTo("Java");
        assertThat(actual.get(1).getCourseName()).isEqualTo("デザイン");
      }
      case "3" -> assertThat(actual.get(0).getCourseName()).isEqualTo("AWS");
      case "4" -> assertThat(actual.get(0).getCourseName()).isEqualTo("デザイン");
      case "5" -> assertThat(actual).isEmpty();
    }
  }

  @Test
  void 登録されていない受講生IDで受講生コース情報の検索が行えないこと() {
    List<StudentCourse> actual = sut.searchStudentCourse("6");

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("たろー");
    student.setEmail("test@example.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 名前設定をせずに受講生の登録を行った時例外がスローされること() {
    Student student = new Student();
    student.setKanaName("タナカタロウ");
    student.setNickname("たろー");
    student.setEmail("test@example.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男");
    student.setRemark("");
    student.setDeleted(false);

    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("999");
    studentCourse.setCourseName("Java");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
    studentCourse.setDeleted(false);

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void コース名を設定せずに受講生コース情報の登録を行った時に例外がスローされること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("999");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
    studentCourse.setDeleted(false);

    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
  }

  @Test
  void 受講生更新が行えること() {
    Student student = new Student();
    student.setId("1");
    student.setName("佐藤泰");
    student.setKanaName("サトウヤスシ");
    student.setNickname("さとさん");
    student.setEmail("sato@gmail.com");
    student.setArea("神奈川県"); // 期待更新箇所
    student.setAge(40); // 期待更新箇所
    student.setSex("男");
    student.setRemark("");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student actual = sut.searchStudent("1");

    assertThat(actual.getArea()).isEqualTo("神奈川県");
    assertThat(actual.getAge()).isEqualTo(40);
  }

  @Test
  void 受講生IDを設定せずに受講生更新を行った時に更新が行われないこと() {
    Student student = new Student();
    student.setName("佐藤泰");
    student.setKanaName("サトウヤスシ");
    student.setNickname("さとさん");
    student.setEmail("sato@gmail.com");
    student.setArea("神奈川県"); // 期待更新箇所
    student.setAge(40); // 期待更新箇所
    student.setSex("男");
    student.setRemark("");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student actual = sut.searchStudent("1");

    assertThat(actual.getArea()).isEqualTo("埼玉県");
    assertThat(actual.getAge()).isEqualTo(33);
  }

  @Test
  void 受講生コース情報更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("デザイン"); // 期待更新箇所
    studentCourse.setCourseStartAt(LocalDateTime.parse("2025-04-12T00:00:00"));
    studentCourse.setCourseEndAt(LocalDateTime.parse("2025-08-31T23:59:59"));
    studentCourse.setDeleted(false);

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertThat(actual.get(0).getCourseName()).isEqualTo("デザイン");
    assertThat(actual.get(1).getCourseName()).isEqualTo("AWS"); // 受講生コース情報IDが異なるので更新処理がされないことを期待
  }

  @Test
  void 受講生コースIDを設定せずに受講生コース情報更新を行った時に更新が行われないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("デザイン"); // 期待更新箇所
    studentCourse.setCourseStartAt(LocalDateTime.parse("2025-04-12T00:00:00"));
    studentCourse.setCourseEndAt(LocalDateTime.parse("2025-08-31T23:59:59"));
    studentCourse.setDeleted(false);

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertThat(actual.get(0).getCourseName()).isEqualTo("Java");
    assertThat(actual.get(1).getCourseName()).isEqualTo("AWS");
  }

}