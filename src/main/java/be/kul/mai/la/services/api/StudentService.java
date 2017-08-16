package be.kul.mai.la.services.api;

import be.kul.mai.la.domain.students.Student;

/**
 * Created by wouter on 21.12.16.
 */
public interface StudentService {

    Student findOne(Integer stamnummer);
}
