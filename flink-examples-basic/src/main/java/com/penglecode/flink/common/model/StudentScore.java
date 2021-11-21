package com.penglecode.flink.common.model;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/21 21:32
 */
public class StudentScore {

    private String studentId;

    private String studentName;

    private String studentSex;

    private String courseName;

    private Integer courseScore;

    public StudentScore() {
    }

    public StudentScore(String studentId, String studentName, String studentSex, String courseName, Integer courseScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSex = studentSex;
        this.courseName = courseName;
        this.courseScore = courseScore;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(Integer courseScore) {
        this.courseScore = courseScore;
    }

    @Override
    public String toString() {
        return "StudentScore{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentSex='" + studentSex + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseScore=" + courseScore +
                '}';
    }
}
