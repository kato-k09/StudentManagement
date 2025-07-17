package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static raisetech.StudentManagement.testutil.GetStudentDetailParams.getStudentDetailParams;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<CourseEnrollment> courseEnrollmentList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(repository.searchCourseEnrollmentList()).thenReturn(courseEnrollmentList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchCourseEnrollmentList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        courseEnrollmentList);

  }

  @Test
  void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);
    String courseId = "998";
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(courseId);
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(List.of(studentCourse));
    when(repository.searchCourseEnrollment(courseId)).thenReturn(new CourseEnrollment());

    StudentDetail expected = new StudentDetail(student, new ArrayList<>(), new ArrayList<>());

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());
    verify(repository, times(1)).searchCourseEnrollment("998");
    assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @ParameterizedTest
  @MethodSource("provideSearchParams")
  void 受講生パラーメーター検索_リポジトリの処理が適切に呼び出せていること(Integer minAge,
      Integer maxAge, LocalDateTime startAtBefore, LocalDateTime endAtAfter,
      String name, String courseName, String enrollment) {

    StudentDetail studentDetailParams = getStudentDetailParams(name,
        courseName, enrollment);
    sut.searchParams(studentDetailParams, minAge, maxAge, startAtBefore, endAtAfter);

    verify(repository, times(1)).searchParams(studentDetailParams, minAge, maxAge, startAtBefore,
        endAtAfter);
  }

  static Stream<Arguments> provideSearchParams() {
    LocalDateTime startAtBefore = LocalDateTime.of(2025, 6, 1, 0, 0);
    LocalDateTime endAtAfter = LocalDateTime.of(2025, 10, 1, 0, 0);

    return Stream.of(
        Arguments.of(20, 30, startAtBefore, endAtAfter, null, null, null),
        Arguments.of(20, 30, startAtBefore, null, null, null, null),
        Arguments.of(20, 30, null, null, null, null, null),
        Arguments.of(20, null, null, null, null, null, null),
        Arguments.of(null, null, null, null, null, null, null),
        Arguments.of(null, null, null, null, "野村", null, null),
        Arguments.of(null, null, null, null, null, "AWS", null),
        Arguments.of(null, null, null, null, null, null, "受講中")
    );
  }

  @Test
  void 受講生登録_リポジトリの処理が適切に呼び出せていること() {

    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    CourseEnrollment courseEnrollment = new CourseEnrollment();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseEnrollment> courseEnrollmentList = List.of(courseEnrollment);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList,
        courseEnrollmentList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);

    // courseEnrollmentはstudentDetailから渡されずサービス側で新インスタンスを作成している為CourseEnrollment.classを使って型比較をしています。
    verify(repository, times(1)).registerCourseEnrollment(any(CourseEnrollment.class));
  }

  @Test
  void 受講生コース情報の登録_初期化処理が行われること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertEquals(id, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getCourseStartAt().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(),
        studentCourse.getCourseEndAt().getYear());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void 受講生更新_リポジトリの処理が適切に呼び出せていること_削除フラグが連動していること(
      boolean isDeleted) {
    Student student = new Student();
    student.setDeleted(isDeleted);
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    CourseEnrollment courseEnrollment = new CourseEnrollment();
    List<CourseEnrollment> courseEnrollmentList = List.of(courseEnrollment);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList,
        courseEnrollmentList);

    sut.updateStudent(studentDetail);

    assertThat(studentCourse.isDeleted()).isEqualTo(isDeleted);
    assertThat(courseEnrollment.isDeleted()).isEqualTo(isDeleted);
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
    verify(repository, times(1)).updateCourseEnrollment(courseEnrollment);
  }

}
