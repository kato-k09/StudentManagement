package raisetech.StudentManagement.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.data.CourseEnrollment;
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
  void 受講生のリストと受講生コース情報のリストとコース申込状況のリストを渡して受講生詳細のリストが作成できること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Java");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    CourseEnrollment courseEnrollment = new CourseEnrollment();
    courseEnrollment.setId("1");
    courseEnrollment.setCourseId("1");
    courseEnrollment.setEnrollment("受講中");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseEnrollment> courseEnrollmentList = List.of(courseEnrollment);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        courseEnrollmentList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
    assertThat(actual.get(0).getCourseEnrollmentList()).isEqualTo(courseEnrollmentList);
  }

  @ParameterizedTest
  @ValueSource(strings = {"999", "1"})
    // 受講生コース情報のコースIDとコース申込状況のコースIDが一致している場合も検証します。
  void 受講生のリストと受講生コース情報のリストとコース申込状況のリストを渡した時に紐づかない受講生コース情報とコース申込状況は除外されること(
      String courseId) {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("11");
    studentCourse.setCourseName("Java");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    CourseEnrollment courseEnrollment = new CourseEnrollment();
    courseEnrollment.setId("111");
    courseEnrollment.setCourseId(courseId);
    courseEnrollment.setEnrollment("受講中");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseEnrollment> courseEnrollmentList = List.of(courseEnrollment);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        courseEnrollmentList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
    assertThat(actual.get(0).getCourseEnrollmentList()).isEmpty();

  }

  private static Student createStudent() {
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("たろー");
    student.setEmail("test@example.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

}
