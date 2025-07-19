package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "受講生コース情報IDです。受講生登録時に自動採番されます。", example = "123")
  private String id;

  @Schema(description = "受講生コース情報に紐づいた受講生IDです。", example = "123")
  private String studentId;

  @Schema(description = "受講生コース情報のコース名です。", example = "Java")
  private String courseName;

  @Schema(description = "受講生コース情報の受講開始日です。受講生登録時に自動的に現在時刻が設定されます。", example = "2025-01-01T19:00:00")
  private LocalDateTime courseStartAt;

  @Schema(description = "受講生コース情報の受講終了予定日です。受講生登録時より1年後が自動的に設定されます。", example = "2025-01-01T19:00:00")
  private LocalDateTime courseEndAt;

  @Schema(description = "受講生コース情報の論理削除フラグです。trueなら削除扱いとなります。", example = "true")
  private boolean isDeleted;

}
