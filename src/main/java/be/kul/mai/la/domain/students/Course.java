package be.kul.mai.la.domain.students;

/**
 * Created by wouter on 31.12.16.
 */
public class Course {

    private final String ID_OPO;
    private final Double grade;

    public Course(String ID_OPO, Double grade) {
        this.ID_OPO = ID_OPO;
        this.grade = grade;
    }

    public String getID_OPO() {
        return ID_OPO;
    }

    public Double getGrade() {
        return grade;
    }
}
