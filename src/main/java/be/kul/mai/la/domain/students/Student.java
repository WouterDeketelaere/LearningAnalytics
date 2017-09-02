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
        //    private String CSE_Juni;
    //    private String CSE_September;

    private List<Course> courses;

    public Student(Integer stamnummer, String academiejaar, List<Course> courses) {
        this.stamnummer = stamnummer;
        this.academiejaar = academiejaar;
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

    public List<Course> getCourses() {
        return courses;
    }
}
