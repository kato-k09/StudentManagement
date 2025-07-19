CREATE TABLE IF NOT EXISTS students
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(50) NOT NULL,
    kana_name  VARCHAR(50) NOT NULL,
    nickname   VARCHAR(50),
    email      VARCHAR(50) NOT NULL,
    area       VARCHAR(50),
    age        INT,
    sex        VARCHAR(10),
    remark     TEXT,
    is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    student_id      INT         NOT NULL,
    course_name     VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at   TIMESTAMP,
    is_deleted      boolean
);

CREATE TABLE IF NOT EXISTS courses_enrollments
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    enrollment ENUM('仮申込', '本申込', '受講中', '受講終了'),
    is_deleted boolean
);