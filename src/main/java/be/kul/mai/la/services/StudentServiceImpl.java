package be.kul.mai.la.services;

import be.kul.mai.la.domain.students.Student;
import be.kul.mai.la.repositories.StudentRepository;
import be.kul.mai.la.services.api.StudentService;
import be.kul.mai.la.services.exceptions.LAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wouter on 31.12.16.
 */
@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student findOne(Integer stamnummer) {
        Student student = studentRepository.findStudentByStamnummer(stamnummer);
        if(student == null)
            throw new LAException(String.format("Student %s not found", stamnummer));
        return student;
    }
}
