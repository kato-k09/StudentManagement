package raisetech.StudentManagement.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細情報を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。。。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングします。 受講生コース情報は受講生に対して複数あるのでループを回して受講生詳細情報を組み立てます。
   *
   * @param studentList       受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<CourseEnrollment> courseEnrollmentList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentCourseList(convertStudentCourseList);

      List<CourseEnrollment> convertCourseEnrollmentList = new ArrayList<>();
      for (StudentCourse studentCourse : convertStudentCourseList) {
        convertCourseEnrollmentList.addAll(courseEnrollmentList.stream()
            .filter(
                courseEnrollment -> courseEnrollment.getCourseId().equals(studentCourse.getId()))
            .collect(Collectors.toList()));
      }
      studentDetail.setCourseEnrollmentList(convertCourseEnrollmentList);

      studentDetails.add(studentDetail);
    });

    return studentDetails;
  }
}
