package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Schema(description = "受講生オブジェクトです。")
  @Valid
  private Student student;

  @Schema(description = "受講生コース情報オブジェクトです。")
  @Valid
  private List<StudentCourse> studentCourseList;

  @Schema(description = "コース申込状況オブジェクトです。")
  @Valid
  private List<CourseEnrollment> courseEnrollmentList;

}
