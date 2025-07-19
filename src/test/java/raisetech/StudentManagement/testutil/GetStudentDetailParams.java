package raisetech.StudentManagement.testutil;

import java.util.List;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

public class GetStudentDetailParams {

  public static StudentDetail getStudentDetailParams(String name, String courseName,
      String enrollment) {
    StudentDetail studentDetailParams = new StudentDetail();
    Student studentParams = new Student();
    studentParams.setName(name);
    studentDetailParams.setStudent(studentParams);

    StudentCourse studentCourseParams = new StudentCourse();
    studentCourseParams.setCourseName(courseName);
    List<StudentCourse> studentCourseParamsList = List.of(studentCourseParams);
    studentDetailParams.setStudentCourseList(studentCourseParamsList);

    CourseEnrollment courseEnrollmentParams = new CourseEnrollment();
    courseEnrollmentParams.setEnrollment(enrollment);
    List<CourseEnrollment> courseEnrollmentParamsList = List.of(courseEnrollmentParams);
    studentDetailParams.setCourseEnrollmentList(courseEnrollmentParamsList);
    return studentDetailParams;
  }

}
