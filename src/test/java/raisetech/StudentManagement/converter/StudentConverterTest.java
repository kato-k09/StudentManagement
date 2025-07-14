package raisetech.StudentManagement.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@ExtendWith(MockitoExtension.class)
public class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void コンバーターにstudentListとstudentCourseListの正常な値を入れて正常に変換されていること() {
    List<Student> studentList = new ArrayList<>();
    Student student1 = new Student();
    student1.setId("999");
    student1.setName("田中太郎");
    student1.setKanaName("タナカタロウ");
    student1.setNickname("タロー");
    student1.setEmail("abcabc@exapmle.com");
    student1.setArea("東京都");
    student1.setAge(30);
    student1.setSex("男");
    student1.setDeleted(false);
    Student student2 = new Student();
    student2.setId("998");
    student2.setName("佐藤太一");
    student2.setKanaName("サトウタイチ");
    student2.setNickname("たいち");
    student2.setEmail("defdef@exapmle.com");
    student2.setArea("埼玉県");
    student2.setAge(25);
    student2.setSex("男");
    student2.setDeleted(false);

    studentList.add(student1);
    studentList.add(student2);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId("1");
    studentCourse1.setStudentId("999");
    studentCourse1.setCourseName("Java");
    studentCourse1.setCourseStartAt(LocalDateTime.parse("2025-01-01T00:00:00.000000"));
    studentCourse1.setCourseEndAt(LocalDateTime.parse("2025-04-01T00:00:00.000000"));
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId("2");
    studentCourse2.setStudentId("998");
    studentCourse2.setCourseName("AWS");
    studentCourse2.setCourseStartAt(LocalDateTime.parse("2025-01-01T00:00:00.000000"));
    studentCourse2.setCourseEndAt(LocalDateTime.parse("2025-04-01T00:00:00.000000"));
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setId("3");
    studentCourse3.setStudentId("998");
    studentCourse3.setCourseName("デザイン");
    studentCourse3.setCourseStartAt(LocalDateTime.parse("2025-01-01T00:00:00.000000"));
    studentCourse3.setCourseEndAt(LocalDateTime.parse("2025-04-01T00:00:00.000000"));

    studentCourseList.add(studentCourse1);
    studentCourseList.add(studentCourse2);
    studentCourseList.add(studentCourse3);

    StudentDetail studentDetail1 = new StudentDetail(student1, List.of(studentCourse1));
    StudentDetail studentDetail2 = new StudentDetail(student2, List.of(studentCourse2));

    List<StudentDetail> studentDetailList = new ArrayList<>();
    studentDetailList.add(studentDetail1);
    studentDetailList.add(studentDetail2);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    // コンバーターの返り値がList<StudentDetail>になっているかの検証
    assertThat(actual).isInstanceOf(List.class);
    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0)).isInstanceOf(StudentDetail.class);
    assertThat(actual.get(1)).isInstanceOf(StudentDetail.class);

    // コンバーターが受講生idとコース情報student_idを紐づけて変換しているかの検証
    assertThat(actual.get(0).getStudent().getId()).isEqualTo(
        actual.get(0).getStudentCourseList().get(0).getStudentId());
    assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo("Java");
    assertThat(actual.get(1).getStudentCourseList().get(0).getCourseName()).isEqualTo("AWS");
    assertThat(actual.get(1).getStudentCourseList().get(1).getCourseName()).isEqualTo("デザイン");
  }
}
