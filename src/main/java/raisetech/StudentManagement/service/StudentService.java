package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseEnrollment;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.dto.StudentSearchParamsExtra;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    if (studentList == null) {
      throw new RuntimeException("登録されている受講生が存在しません。");
    }
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<CourseEnrollment> courseEnrollmentList = repository.searchCourseEnrollmentList();
    return converter.convertStudentDetails(studentList, studentCourseList, courseEnrollmentList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    if (student == null) {
      throw new RuntimeException("指定された受講生が見つかりません: ID = " + id);
    }
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getId());

    List<CourseEnrollment> courseEnrollmentList = new ArrayList<>();
    for (StudentCourse studentCourse : studentCourseList) {
      courseEnrollmentList.add(repository.searchCourseEnrollment(studentCourse.getId()));
    }

    return new StudentDetail(student, studentCourseList, courseEnrollmentList);
  }

  /**
   * 受講生詳細の一覧検索 兼 受講生パラメータ検索です。パラメーターを指定しなければ受講生詳細情報を全件取得します。
   *
   * @param studentDetailParams      StudentDetail内のフィールド名と同一の検索パラメーターを指定できます。
   * @param studentSearchParamsExtra 拡張検索パラメータです。StudentSearchParamsExtraクラス内で定義されたパラメーターです。
   * @return パラメータ検索に該当した受講生詳細リスト
   */
  public List<StudentDetail> searchParams(StudentDetail studentDetailParams,
      StudentSearchParamsExtra studentSearchParamsExtra) {

    return repository.searchParams(studentDetailParams, studentSearchParamsExtra);
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値や日付情報コース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student.getId());
      repository.registerStudentCourse(studentCourse);
    });

    // 受講生コース情報を元にコース申込状況を生成・受講生コース情報ID・仮申込情報を設定
    List<CourseEnrollment> courseEnrollmentList = new ArrayList<>();
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      CourseEnrollment courseEnrollment = new CourseEnrollment();
      courseEnrollment.setCourseId(studentCourse.getId());
      courseEnrollment.setEnrollment("仮申込");
      repository.registerCourseEnrollment(courseEnrollment);
      courseEnrollmentList.add(courseEnrollment);
    }
    studentDetail.setCourseEnrollmentList(courseEnrollmentList);

    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourses 受講生コース情報
   * @param id             受講生
   */
  void initStudentsCourse(StudentCourse studentCourses, String id) {
    LocalDateTime now = LocalDateTime.now();

    studentCourses.setStudentId(id);
    studentCourses.setCourseStartAt(now);
    studentCourses.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生IDに紐づけてコースを追加します。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Transactional
  public StudentDetail addCourse(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student.getId());
      repository.registerStudentCourse(studentCourse);
    });

    // 受講生コース情報を元にコース申込状況を生成・受講生コース情報ID・仮申込情報を設定
    List<CourseEnrollment> courseEnrollmentList = new ArrayList<>();
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      CourseEnrollment courseEnrollment = new CourseEnrollment();
      courseEnrollment.setCourseId(studentCourse.getId());
      courseEnrollment.setEnrollment("仮申込");
      repository.registerCourseEnrollment(courseEnrollment);
      courseEnrollmentList.add(courseEnrollment);
    }
    studentDetail.setCourseEnrollmentList(courseEnrollmentList);

    return studentDetail;
  }

  /**
   * 受講生詳細の更新を行います。 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    studentDetail.getStudentCourseList().forEach(studentCourses -> {
      if (studentDetail.getStudent().isDeleted()) {
        studentCourses.setDeleted(true);
      }
      repository.updateStudentCourse(studentCourses);
    });

    for (CourseEnrollment courseEnrollment : studentDetail.getCourseEnrollmentList()) {
      if (studentDetail.getStudent().isDeleted()) {
        courseEnrollment.setDeleted(true);
      }
      repository.updateCourseEnrollment(courseEnrollment);
    }
  }

}
