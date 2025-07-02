package raisetech.StudentManagement;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentsRepository {

  // 検索
  @Select("SELECT * FROM students WHERE fullName = #{fullName}")
  Student searchStudent(String fullName);

  // 登録
  @Insert("INSERT students values(#{id},#{fullName}, #{furigana}, #{nickName}, #{mail}, #{region}, #{age}, #{gender})")
  void registerStudent(String id, String fullName, String furigana, String nickName, String mail,
      String region, int age, String gender);

  // ID重複確認
  @Select("SELECT EXISTS (SELECT 1 FROM students WHERE id = #{id})")
  boolean isStudent(String id);

  // 更新
  void updateStudent(Student student);

  // 削除
  @Delete("DELETE FROM students WHERE id = #{id}")
  void deleteStudent(String id);
}
