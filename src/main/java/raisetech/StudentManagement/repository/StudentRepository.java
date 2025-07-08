package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Insert(
      "INSERT INTO students_courses(student_id, course_name, course_start_at, course_end_at, is_deleted)"
          + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentsCourses studentsCourses);

  /**
   * 個人受講生情報を検索します。
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchIndividualStudent(int id);

  /**
   * 個人コース情報を検索します。
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchIndividualStudentCourses(int studentId);

  /**
   * 個人受講生情報を更新します。
   */
  @Update("UPDATE students SET name=#{name}, kana_name=#{kanaName}, nickname=#{nickname}, email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark}, is_deleted=#{isDeleted} WHERE id=#{id}")
  void updateStudent(Student student);

  /**
   * 個人コース情報を更新します。
   */
  @Update("UPDATE students_courses SET course_name=#{courseName} WHERE student_id=#{studentId}")
  void updateCourse(StudentsCourses studentsCourses);

}
