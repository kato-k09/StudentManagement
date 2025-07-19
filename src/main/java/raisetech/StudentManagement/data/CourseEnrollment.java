package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コース申込状況")
@Getter
@Setter
public class CourseEnrollment {

  @Schema(description = "コース申込状況IDです。受講生登録時に自動採番されます。", example = "123")
  private String id;

  @Schema(description = "コース申込状況に紐づいた受講生コースIDです。", example = "123")
  private String courseId;

  @Schema(description = "コース申込状況です。", example = "仮申込")
  private String enrollment;

  @Schema(description = "コース申込状況の論理削除フラグです。trueなら削除扱いとなります。", example = "true")
  boolean isDeleted;
}

