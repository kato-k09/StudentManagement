package raisetech.StudentManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生拡張検索パラメーター")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchParamsExtra {

  @Schema(description = "最小年齢を定義するパラメーターです。")
  Integer minAge;
  @Schema(description = "最大年齢を定義するパラメーターです。")
  Integer maxAge;
  @Schema(description = "受講開始日がこの日時より前であることを定義するパラメーターです。")
  LocalDateTime startAtBefore;
  @Schema(description = "受講終了予定日がこの日時より後であることを定義するパラメーターです。")
  LocalDateTime endAtAfter;

}
