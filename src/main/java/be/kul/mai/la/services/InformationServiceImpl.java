package be.kul.mai.la.services;

import be.kul.mai.la.domain.students.Student;
import be.kul.mai.la.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InformationServiceImpl {

    private StudentRepository studentRepository;

    @Autowired
    public InformationServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<SelectOption> getAttributeWeightings() {
        List<String> statistic = Arrays.asList("Correlation", "Chi²", "Information Gain");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < statistic.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), statistic.get(i - 1)));
        }
        return selectOptions;
    }

    public List<SelectOption> getInstruments() {
        List<String> instrument = Arrays.asList("Gradient Boosted Trees", "Naive Bayes", "Support Vector Machine", "Neural Network", "k-Nearest Neighbour");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < instrument.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), instrument.get(i - 1)));
        }
        return selectOptions;
    }

    public List<SelectOption> getDatasets() {
        List<String> input = Arrays.asList("Complete", "Civil Engineering", "Civil Eng. Architecture", "Grades & CSE");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < input.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), input.get(i - 1)));
        }
        return selectOptions;
    }

    public List<SelectOption> getStudentIds() {
        Iterable<Student> iterable = studentRepository.findAll();
        List<Student> students = new ArrayList<>();
        iterable.forEach(students::add);
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < students.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(students.get(i - 1).getStamnummer()), String.valueOf(students.get(i - 1).getStamnummer())));
        }
        return selectOptions;
    }


    class SelectOption {
        private String id;
        private String name;

        public SelectOption(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
