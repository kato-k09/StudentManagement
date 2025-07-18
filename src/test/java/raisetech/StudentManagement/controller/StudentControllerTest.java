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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
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
  void 受講生パラメーター検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList")
            .param("student.name", "佐藤")
            .param("studentCourse.courseName", "Java")
            .param("courseEnrollment.enrollment", "受講中")
            .param("minAge", "20")
            .param("maxAge", "30")
            .param("startAtBefore", LocalDateTime.of(2025, 5, 1, 0, 0).toString())
            .param("endAtAfter", LocalDateTime.of(2025, 10, 1, 0, 0).toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1))
        .searchParams(any(), any(), any(), any(), any());
  }

  @Test
  void 受講生パラメーター検索が一部パラメーターの指定が無くても実行できて空のリストが返ってくること()
      throws Exception {
    mockMvc.perform(get("/studentList")
            .param("student.name", "佐藤")
            .param("studentCourse.courseName", "Java")
            .param("courseEnrollment.enrollment", "受講中")
            .param("minAge", "20")
            .param("startAtBefore", LocalDateTime.of(2025, 5, 1, 0, 0).toString()))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1))
        .searchParams(any(), any(), any(), any(), any());
  }

  @Test
  void 受講生パラメーター検索がパラメーターの指定が無くても実行できて空のリストが返ってくること()
      throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1))
        .searchParams(any(), any(), any(), any(), any());
  }

  @Test
  void 受講生詳細の検索が実行できて空のリストが返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().json("{}"));
    verify(service, times(1)).searchStudent(id);
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
  void 受講生登録が実行できて空で返ってくること() throws Exception {
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(new StudentDetail());
    mockMvc.perform(post("/registerStudent").contentType(APPLICATION_JSON).content(
            """
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
                """
        ))
        .andExpect(status().isOk())
        .andExpect(content().json("{}"));
    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生登録後のコース追加が実行できて空で返ってくること() throws Exception {
    when(service.addCourse(any(StudentDetail.class))).thenReturn(new StudentDetail());
    mockMvc.perform(post("/addCourse").contentType(APPLICATION_JSON).content(
            """
                {
                  "student": {
                    "id": "7",
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
                      "courseName": "デザイン"
                    }
                  ]
                }
                """
        ))
        .andExpect(status().isOk())
        .andExpect(content().json("{}"));
    verify(service, times(1)).addCourse(any(StudentDetail.class));
  }

  @Test
  void 受講生更新が受講生詳細情報を含んだリクエストで実行できること() throws Exception {

    mockMvc.perform(put("/updateStudent").contentType(APPLICATION_JSON).content(
            """
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
                      "id": "999",
                      "studentId": "999",
                      "courseName": "Java",
                      "courseStartAt": "2025-01-01T00:00:00.000000",
                      "courseEndAt": "2025-04-01T00:00:00.000000"
                    }
                  ],
                  "courseEnrollmentList": [
                    {
                      "id": "999",
                      "courseId": "999",
                      "enrollment": "受講中"
                    }
                  ]
                }
                """
        ))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));
    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }
}
