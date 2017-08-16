package be.kul.mai.la.domain.students;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by wouter on 23.12.16.
 */
@Document(collection = "students")
public class Student {

    @Id
    private String id;
    @Field("Stamnummer student")
    private Integer stamnummer;
    @Field("Academiejaar")
    private String academiejaar;
    @Field("CSE Januari")
    private Double CSE_Januari;

    //    private String CSE_Juni;
    //    private String CSE_September;

    private List<Course> courses;

    public Student(Integer stamnummer, String academiejaar, Double CSE_Januari, List<Course> courses) {
        this.stamnummer = stamnummer;
        this.academiejaar = academiejaar;
        this.CSE_Januari = CSE_Januari;
        this.courses = courses;
    }

    public String getId() {
        return id;
    }

    public Integer getStamnummer() {
        return stamnummer;
    }

    public String getAcademiejaar() {
        return academiejaar;
    }

    public Double getCSE_Januari() {
        return CSE_Januari;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
