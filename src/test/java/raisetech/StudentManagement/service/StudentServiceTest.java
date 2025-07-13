package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
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
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

  }

  @Test
  void 受講生検索_IDに紐づいたリポジトリとStudentDetailの処理が適切に呼び出せていること() {
    String id = "10";
    Student student = new Student();
    student.setId(id);
    List<StudentCourse> studentCourseList = List.of(new StudentCourse());
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(
        studentCourseList);

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());
    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生登録_リポジトリの処理が適切に呼び出せていること() {

    Student student = new Student();
    student.setId("10");
    StudentCourse studentCourse = new StudentCourse();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    StudentDetail actual = sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);

    assertEquals(student, actual.getStudent());
  }

  @Test
  void 受講生コース情報初期化_studentCourseに適切に値が設定されていること() {
    Student student = new Student();
    student.setId("10");
    StudentCourse studentCourse = new StudentCourse();
    LocalDateTime now = LocalDateTime.now();

    sut.initStudentsCourse(studentCourse, student);

    assertEquals(student.getId(), studentCourse.getStudentId());
    assertEquals(now, studentCourse.getCourseStartAt());
    assertEquals(now.plusYears(1), studentCourse.getCourseEndAt());
  }

  @Test
  void 受講生更新_リポジトリの処理が適切に呼び出せている_studentCourseがstudentと連動してisDeletedがtrueであるか() {
    Student student = new Student();
    student.setId("10");
    student.setDeleted(true);
    StudentCourse studentCourse = new StudentCourse();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    sut.updateStudent(studentDetail);

    assertTrue(studentCourse.isDeleted(),
        "isDeletedがstudentCourseではfalseでありstudentと不一致です。");
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  void 受講生更新_リポジトリの処理が適切に呼び出せている_studentCourseがstudentと連動してisDeletedがfalseであるか() {
    Student student = new Student();
    student.setId("10");
    student.setDeleted(false);
    StudentCourse studentCourse = new StudentCourse();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    sut.updateStudent(studentDetail);

    assertFalse(studentCourse.isDeleted(),
        "isDeletedがstudentCourseではtrueでありstudentと不一致です。");
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

}
