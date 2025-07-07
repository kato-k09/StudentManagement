package raisetech.StudentManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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
  @Insert("INSERT INTO students VALUES(#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, #{isDeleted})")
  void insertStudent(String id, String name, String kanaName, String nickname, String email,
      String area, int age, String sex, String remark, boolean isDeleted);

  /**
   * コース情報を登録します。
   */
  @Insert("INSERT INTO students_Courses VALUES(#{id}, #{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt}, #{isDeleted})")
  void insertCourse(String id, String studentId, String courseName, LocalDateTime courseStartAt,
      LocalDateTime courseEndAt, boolean isDeleted);

}
