package raisetech.StudentManagement;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentsRepository {

  @Select("SELECT * FROM students WHERE fullName = #{fullName}")
  Student searchStudent(String fullName);

  @Insert("INSERT students values(#{id},#{fullName}, #{furigana}, #{nickName}, #{mail}, #{region}, #{age}, #{gender})")
  void registerStudent(String id, String fullName, String furigana, String nickName, String mail,
      String region, int age, String gender);

  @Select("SELECT EXISTS (SELECT 1 FROM students WHERE id = #{id})")
  boolean isStudent(String id);

  // 更新
  void updateStudent(Student student);
}
