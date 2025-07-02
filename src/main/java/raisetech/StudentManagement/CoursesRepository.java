package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CoursesRepository {

  // 検索
  @Select("SELECT * FROM students_courses WHERE id = #{id}")
  List<Course> searchCourse(String id);

  // 登録
  @Insert("INSERT students_courses values(#{id}, #{course}, #{startDate}, #{scheduledEndDate})")
  void registerCourse(String id, String course, java.sql.Date startDate,
      java.sql.Date scheduledEndDate);

  // 同一IDで同一コース重複確認
  @Select("SELECT EXISTS (SELECT 1 FROM students_courses WHERE id = #{id} AND course = #{course})")
  boolean isCourse(String id, String course);

  // 更新
  void updateCourse(Course course);

  // 削除
  @Delete("DELETE FROM students_courses WHERE id = #{id}")
  void deleteCourse(String id);
}
