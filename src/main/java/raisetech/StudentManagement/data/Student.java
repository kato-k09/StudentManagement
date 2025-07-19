package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生IDです。受講生登録時には自動採番されるので値を入れる必要がありません。", example = "123")
  @Pattern(regexp = "^\\d+$", message = "数値のみ入力するようにしてください。")
  private String id;

  @Schema(description = "受講生の名前です。", example = "田中太郎")
  @NotBlank(message = "名前を入力してください。")
  private String name;

  @Schema(description = "受講生の名前のフリガナです。", example = "タナカタロウ")
  @NotBlank(message = "フリガナを入力してください。")
  private String kanaName;

  @Schema(description = "受講生のニックネームです。", example = "たろー")
  @NotBlank(message = "ニックネームを入力してください。")
  private String nickname;

  @Schema(description = "受講生のメールアドレスです。", example = "abc@example.com")
  @NotBlank(message = "メールアドレスを入力してください。")
  @Email
  private String email;

  @Schema(description = "受講生の住んでいる地域です。", example = "神奈川県横浜市")
  @NotBlank(message = "住んでいる地域を入力してください。")
  private String area;

  @Schema(description = "受講生の年齢です。1歳以上200歳以下の範囲で入力可能です。", example = "30")
  @Min(value = 0, message = "年齢を入力してください。")
  @Max(value = 200, message = "正しい年齢を入力してください。")
  private int age;

  @Schema(description = "受講生の性別です。", example = "男")
  @NotBlank(message = "性別を入力してください。")
  private String sex;

  @Schema(description = "受講生の備考です。", example = "Javaを大学で学んでいた経験有り")
  private String remark;

  @Schema(description = "受講生の論理削除フラグです。trueなら削除扱いとなります。", example = "false")
  private boolean isDeleted;

}
