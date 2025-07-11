package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

  @Pattern(regexp = "^\\d+$", message = "数値を入れてください。")
  private String id;

  @NotBlank(message = "名前を入力してください。")
  private String name;

  @NotBlank(message = "フリガナを入力してください。")
  private String kanaName;

  @NotBlank(message = "ニックネームを入力してください。")
  private String nickname;

  @NotBlank(message = "メールアドレスを入力してください。")
  @Email
  private String email;

  @NotBlank(message = "住んでいる地域を入力してください。")
  private String area;

  @Min(value = 1, message = "年齢を入力してください。")
  @Max(value = 200, message = "正しい年齢を入力してください。")
  private int age;

  @NotBlank(message = "性別を入力してください。")
  private String sex;

  private String remark;
  private boolean isDeleted;

}
