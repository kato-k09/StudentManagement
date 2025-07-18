package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static raisetech.StudentManagement.testutil.GetStudentDetailParams.getStudentDetailParams;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

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

  @Test
  void コース申込状況の全件検索が行えること() {
    List<CourseEnrollment> actual = sut.searchCourseEnrollmentList();
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

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
  void 登録されている受講生コース情報IDでコース申込状況の検索が行えること(String courseId) {
    CourseEnrollment actual = sut.searchCourseEnrollment(courseId);

    switch (courseId) {
      case "1" -> assertThat(actual.getEnrollment()).isEqualTo("受講終了");
      case "2" -> assertThat(actual.getEnrollment()).isEqualTo("本申込");
      case "3" -> assertThat(actual.getEnrollment()).isEqualTo("仮申込");
      case "4" -> assertThat(actual.getEnrollment()).isEqualTo("仮申込");
      case "5" -> assertThat(actual.getEnrollment()).isEqualTo("受講中");
      case "6" -> assertThat(actual.getEnrollment()).isEqualTo("受講中");
    }
  }

  @Test
  void 登録されていない受講生コース情報IDでコース申込状況の検索が行えないこと() {
    CourseEnrollment actual = sut.searchCourseEnrollment("7");

    assertThat(actual).isNull();
  }

  @ParameterizedTest
  @MethodSource("provideSearchParams")
  void 受講生パラメーター検索による受講生ID取得が行えること(int testIndex, Integer minAge,
      Integer maxAge, LocalDateTime startAtBefore, LocalDateTime endAtAfter,
      String name, String courseName, String enrollment) {

    StudentDetail studentDetailParams = getStudentDetailParams(name,
        courseName, enrollment);

    List<StudentDetail> actual = sut.searchParams(studentDetailParams, minAge, maxAge,
        startAtBefore, endAtAfter);

    switch (testIndex) {
      case 1 -> assertThat(actual.size()).isEqualTo(0);
      case 2 -> assertThat(actual.size()).isEqualTo(1);
      case 3 -> assertThat(actual.size()).isEqualTo(3);
      case 4 -> assertThat(actual.size()).isEqualTo(5);
      case 5 -> assertThat(actual.size()).isEqualTo(5);
      case 6 -> assertThat(actual.size()).isEqualTo(1);
      case 7 -> assertThat(actual.size()).isEqualTo(2);
      case 8 -> assertThat(actual.size()).isEqualTo(2);
    }
  }

  static Stream<Arguments> provideSearchParams() {

    LocalDateTime startAtBefore = LocalDateTime.of(2025, 6, 1, 0, 0);
    LocalDateTime endAtAfter = LocalDateTime.of(2025, 10, 1, 0, 0);
    return Stream.of(
        Arguments.of(1, 20, 30, startAtBefore, endAtAfter, null, null, null),
        Arguments.of(2, 20, 30, startAtBefore, null, null, null, null),
        Arguments.of(3, 20, 30, null, null, null, null, null),
        Arguments.of(4, 20, null, null, null, null, null, null),
        Arguments.of(5, null, null, null, null, null, null, null),
        Arguments.of(6, null, null, null, null, "野村", null, null),
        Arguments.of(7, null, null, null, null, null, "AWS", null),
        Arguments.of(8, null, null, null, null, null, null, "受講中")
    );
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
  void コース申込状況の登録が行えること() {
    CourseEnrollment courseEnrollment = new CourseEnrollment();
    courseEnrollment.setCourseId("999");
    courseEnrollment.setEnrollment("仮申込"); // サービスで強制的に入力されるので設定しない場合のテストは行いません。
    courseEnrollment.setDeleted(false);

    sut.registerCourseEnrollment(courseEnrollment);

    List<CourseEnrollment> actual = sut.searchCourseEnrollmentList();

    assertThat(actual.size()).isEqualTo(7);
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

  @Test
  void コース申込状況更新が行えること() {
    CourseEnrollment courseEnrollment = new CourseEnrollment();
    courseEnrollment.setId("3");
    courseEnrollment.setEnrollment("受講中"); // 期待更新箇所
    courseEnrollment.setDeleted(false);

    sut.updateCourseEnrollment(courseEnrollment);

    CourseEnrollment actual = sut.searchCourseEnrollment("3");

    assertThat(actual.getEnrollment()).isEqualTo("受講中");
  }

  @Test
  void 受講生コース情報IDを設定せずにコース申込状況更新を行った時に更新が行われないこと() {
    CourseEnrollment courseEnrollment = new CourseEnrollment();
    courseEnrollment.setEnrollment("受講中"); // 期待更新箇所
    courseEnrollment.setDeleted(false);

    sut.updateCourseEnrollment(courseEnrollment);

    CourseEnrollment actual = sut.searchCourseEnrollment("3");

    assertThat(actual.getEnrollment()).isEqualTo("仮申込");
  }

}