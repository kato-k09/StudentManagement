package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新等をREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索し、Json形式で結果を取得します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生検索", description = "idに紐づいた受講生を検索し、Json形式で結果を取得します。")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生パラメータ検索です。
   *
   * @param studentParams          Student内のフィールド名と一致しているパラメータを格納します。
   * @param studentCourseParams    StudentCourse内のフィールド名と一致しているパラメータを格納します。
   * @param courseEnrollmentParams CourseEnrollment内のフィールド名と一致しているパラメータを格納します。
   * @param minAge                 最小年齢を指定する検索パラメータです。
   * @param maxAge                 最大年齢を指定する検索パラメータです。
   * @param startAtBefore          受講開始日がこの値より前を対象とした検索パラメータです。
   * @param endAtAfter             受講終了予定日がこの値より後を対象とした検索パラメータです。
   * @return パラメータ検索に該当した受講生詳細リスト
   */
  @GetMapping("/searchParams")
  public List<StudentDetail> searchParams(@ModelAttribute Student studentParams,
      @ModelAttribute StudentCourse studentCourseParams,
      @ModelAttribute CourseEnrollment courseEnrollmentParams,
      @RequestParam(name = "minAge", required = false) Integer minAge,
      @RequestParam(name = "maxAge", required = false) Integer maxAge,
      @RequestParam(name = "startAtBefore", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAtBefore,
      @RequestParam(name = "endAtAfter", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endAtAfter) {

    StudentDetail studentDetailParams = new StudentDetail(studentParams,
        List.of(studentCourseParams), List.of(courseEnrollmentParams));

    return service.searchParams(studentDetailParams, minAge, maxAge, startAtBefore, endAtAfter);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。値はJson形式で設定する必要があります。IDは自動採番されるので設定不要です。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生IDに紐づけてコースを追加します。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/addCourse")
  public ResponseEntity<StudentDetail> addCourse(@Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.addCourse(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います（論理削除）。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "受講生を更新します。値はJson形式で設定する必要があります。設定した値全てが書き換わるので、変更したい箇所以外は既存の値を入れる必要があります。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @Operation(summary = "例外テスト", description = "例外テストを実行します。")
  @GetMapping("/exception")
  public ResponseEntity<String> exception() throws TestException {
    throw new TestException("このAPIは現在利用できません。古いURLとなっています。");
  }
}
