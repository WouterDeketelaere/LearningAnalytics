package be.kul.mai.la.repositories;

import be.kul.mai.la.domain.students.Student;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wouter on 31.12.16.
 */
public interface StudentRepository extends CrudRepository<Student, String> {

    Student findStudentByStamnummer(Integer stamnummer);
}
