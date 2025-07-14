package raisetech.StudentManagement.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());
    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("タロー");
    student.setEmail("abcabc@exapmle.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("タロー");
    student.setEmail("abcabc@exapmle.com");
    student.setArea("東京都");
    student.setAge(30);
    student.setSex("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数値のみ入力するようにしてください。");
  }

  @Test
  void 受講生詳細のコース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("999");
    studentCourse.setCourseName("Java");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生登録_受講生詳細情報を含んだリクエストで異常が発生しないこと()
      throws Exception {
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(new StudentDetail());
    String jsonRequest = """
        {
          "student": {
            "name": "丹沢修司",
            "kanaName": "タンザワシュウジ",
            "nickname": "しゅー",
            "email": "tanzawa@gmail.com",
            "area": "茨城県",
            "age": 28,
            "sex": "男",
            "remark": ""
          },
          "studentCourseList": [
            {
              "courseName": "Java"
            }
          ]
        }
        """;

    mockMvc.perform(post("/registerStudent")
            .contentType(APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk());
    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生更新_受講生詳細情報を含んだリクエストで異常が発生しないこと()
      throws Exception {
    String jsonRequest = """
        {
          "student": {
            "id": "999",
            "name": "丹沢修司",
            "kanaName": "タンザワシュウジ",
            "nickname": "しゅー",
            "email": "tanzawa@gmail.com",
            "area": "茨城県",
            "age": 28,
            "sex": "男",
            "remark": "",
            "isDeleted": false
          },
          "studentCourseList": [
            {
              "courseName": "Java"
            }
          ]
        }
        """;

    mockMvc.perform(put("/updateStudent")
            .contentType(APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk());
    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }
}
