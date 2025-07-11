package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト。
 */
@Getter
@Setter
public class Student {

  @Pattern(regexp = "^\\d+$")
  private String id;

  @NotBlank
  private String name;

  @NotBlank
  private String kanaName;

  @NotBlank
  private String nickname;

  @Email
  private String email;

  @NotBlank
  private String area;

  private int age;

  @NotBlank
  private String sex;

  private String remark;
  private boolean isDeleted;

}
