package be.kul.mai.la.services;


import be.kul.mai.la.domain.students.Student;
import be.kul.mai.la.services.api.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

/**
 * Created by wouter on 31.12.16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Test
    public void findOne() throws Exception {
        Student student = studentService.findOne(189631);
        assertThat(student.getCourses(),is(not(empty())));
    }
}