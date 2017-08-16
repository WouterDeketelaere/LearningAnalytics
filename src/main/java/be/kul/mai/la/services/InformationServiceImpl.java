package be.kul.mai.la.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InformationServiceImpl {

    public List<SelectOption> getAttributeWeightings() {
        List<String> statistic = Arrays.asList("Correlation", "Chi²", "Information Gain");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < statistic.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), statistic.get(i - 1)));
        }
        return selectOptions;
    }

    public List<SelectOption> getInstruments() {
        List<String> statistic = Arrays.asList("Gradient Boosted Trees", "Naive Bayes", "Support Vector Machine", "Neural Network", "k-Nearest Neighbour");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < statistic.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), statistic.get(i - 1)));
        }
        return selectOptions;
    }

    public List<SelectOption> getDatasets() {
        List<String> statistic = Arrays.asList("Complete","Civil Engineering", "Civil Eng. Architecture", "Grades & CSE");
        List<SelectOption> selectOptions = new ArrayList<>();
        for (int i = 1; i < statistic.size() + 1; i++) {
            selectOptions.add(new SelectOption(String.valueOf(i), statistic.get(i - 1)));
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
