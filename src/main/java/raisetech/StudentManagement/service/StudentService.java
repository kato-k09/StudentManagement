package raisetech.StudentManagement.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 検索処理を行う
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {

    return repository.searchStudentsCourses();
  }

  public void registerStudent(StudentDetail studentDetail) {

    String studentUuid = UUID.randomUUID().toString();
    studentDetail.getStudent().setId(studentUuid);
    studentDetail.getStudentsCourses().get(0).setStudentId(studentUuid); // コースにも受講生IDをセット

    repository.insertStudent(
        studentDetail.getStudent().getId(),
        studentDetail.getStudent().getName(),
        studentDetail.getStudent().getKanaName(),
        studentDetail.getStudent().getNickname(),
        studentDetail.getStudent().getEmail(),
        studentDetail.getStudent().getArea(),
        studentDetail.getStudent().getAge(),
        studentDetail.getStudent().getSex(),
        studentDetail.getStudent().getRemark(),
        studentDetail.getStudent().isDeleted()
    );

    String courseUuid = UUID.randomUUID().toString();
    studentDetail.getStudentsCourses().get(0).setId(courseUuid); // コースごとのUUIDを設定

    repository.insertCourse(
        studentDetail.getStudentsCourses().get(0).getId(),
        studentDetail.getStudentsCourses().get(0).getStudentId(),
        studentDetail.getStudentsCourses().get(0).getCourseName(),
        studentDetail.getStudentsCourses().get(0).getCourseStartAt(),
        studentDetail.getStudentsCourses().get(0).getCourseEndAt(),
        studentDetail.getStudentsCourses().get(0).isDeleted()
    );
  }


}
