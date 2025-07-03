package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
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

    // 24_Read処理のServiceとController部分を実装　課題1
    // 絞り込みをする。年齢が30代の人のみ抽出する。
    // 抽出したリストをコントローラーに返す。
    List<Student> student = repository.search();
    return student.stream()
        .filter(v -> 30 <= v.getAge() && v.getAge() < 40)
        .collect(Collectors.toList());
  }

  public List<StudentsCourses> searchStudentsCourseList() {

    // 24_Read処理のServiceとController部分を実装　課題2
    // 絞り込み検索で「Javaコース」のコース情報のみ抽出する。
    // 抽出したリストをコントローラーに返す。
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses();
    return studentsCourses.stream()
        .filter(v -> v.getCourseName().equals("Java"))
        .collect(Collectors.toList());
  }
}
