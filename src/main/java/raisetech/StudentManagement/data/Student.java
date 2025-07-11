package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト。
 */
@Getter
@Setter
public class Student {

  private String id;
  @NotBlank
  private String name;
  @NotBlank
  private String kanaName;
  @NotBlank
  private String nickname;
  @Email
  private String email;
  private String area;
  private int age;
  private String sex;
  private String remark;
  private boolean isDeleted;

}
