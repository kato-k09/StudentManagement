package raisetech.StudentManagement;

import java.sql.Date;

public class Course {

  String id;
  String course;
  java.sql.Date startDate;
  java.sql.Date scheduledEndDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getScheduledEndDate() {
    return scheduledEndDate;
  }

  public void setScheduledEndDate(Date scheduledEndDate) {
    this.scheduledEndDate = scheduledEndDate;
  }
}
