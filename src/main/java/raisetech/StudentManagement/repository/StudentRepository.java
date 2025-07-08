package raisetech.StudentManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ。
 * <p>
 * 全件検索や単一検索、コース情報検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  /**
   * 受講生情報を登録します。
   */
  @Insert(
      "INSERT INTO students(name, kana_name, nickname, email, area, age, sex, remark, is_deleted)"
          + " VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);


  /**
   * コース情報を登録します。
   */
  @Insert("INSERT INTO students_courses VALUES(#{id}, #{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt}, #{isDeleted})")
  void insertCourse(String id, String studentId, String courseName, LocalDateTime courseStartAt,
      LocalDateTime courseEndAt, boolean isDeleted);

  /**
   * 個人コース情報を検索します。
   */
  @Select("SELECT * FROM students_courses WHERE student_Id = #{studentId}")
  List<StudentsCourses> searchIndividualStudentCourses(String studentId);


}
