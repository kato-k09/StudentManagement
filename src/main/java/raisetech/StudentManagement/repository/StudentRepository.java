package raisetech.StudentManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * コース申込状況の全件検索を行います。
   *
   * @return コース申込状況の情報（全件）
   */
  List<CourseEnrollment> searchCourseEnrollmentList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);


  /**
   * 受講生コース情報IDに紐づくコース申込状況を検索します。
   *
   * @param courseId 受講生コース情報ID
   * @return 受講生コース情報IDに紐づくコース申込状況
   */
  CourseEnrollment searchCourseEnrollment(String courseId);

  /**
   * 受講生パラメータ検索です。
   *
   * @param studentDetailParams StudentDetail内のフィールド名と同一の検索パラメータを指定できます。
   * @param minAge              最小年齢を指定する検索パラメータです。
   * @param maxAge              最大年齢を指定する検索パラメータです。
   * @param startAtBefore       受講開始日がこの値より前を対象とした検索パラメータです。
   * @param endAtAfter          受講終了予定日がこの値より後を対象とした検索パラメータです。
   * @return パラメータ検索に該当した受講生ID
   */
  List<StudentDetail> searchParams(StudentDetail studentDetailParams, Integer minAge,
      Integer maxAge, LocalDateTime startAtBefore, LocalDateTime endAtAfter);

  /**
   * 受講生情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * コース申込状況を新規登録します。IDに関しては自動採番を行う。
   *
   * @param courseEnrollment コース申込状況
   */
  void registerCourseEnrollment(CourseEnrollment courseEnrollment);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);


  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * コース申込状況を更新します。
   *
   * @param courseEnrollment コース申込状況
   */
  void updateCourseEnrollment(CourseEnrollment courseEnrollment);

}
