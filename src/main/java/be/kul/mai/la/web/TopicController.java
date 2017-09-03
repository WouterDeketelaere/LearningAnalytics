package be.kul.mai.la.web;

import be.kul.mai.la.services.InformationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wouter on 24.01.17.
 */
@Controller
public class TopicController {

    private InformationServiceImpl informationService;

    @Autowired
    public TopicController(InformationServiceImpl informationService) {
        this.informationService = informationService;
    }

    @RequestMapping("/topic")
    public String showTopic(@RequestParam("id") String topic, Model model) {
        if(topic.equals("dashboard")) {
            model.addAttribute("mechanisms", informationService.getAttributeWeightings());
            model.addAttribute("instruments", informationService.getInstruments());
            model.addAttribute("datasets", informationService.getDatasets());
            model.addAttribute("studentids", informationService.getStudentIds());
            model.addAttribute("clusterdatasets", informationService.getClusterDatasets());
        }
        return topic.toLowerCase();
    }
}
