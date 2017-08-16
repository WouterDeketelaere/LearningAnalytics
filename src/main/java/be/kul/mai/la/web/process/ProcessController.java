package be.kul.mai.la.web.process;

import be.kul.mai.la.domain.analytics.engine.RapidMinerAPI;
import be.kul.mai.la.services.exceptions.LAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by wouter on 14.01.17.
 */
@Controller
public class ProcessController {

    private final RapidMinerAPI rapidMinerAPI;

    @Autowired
    public ProcessController(RapidMinerAPI rapidMinerAPI) {

        this.rapidMinerAPI = rapidMinerAPI;
    }

    @MessageMapping("/starttrain")
    @SendTo("/queue/progressvalues")
    public void startTrainProcess() throws LAException {
        rapidMinerAPI.runProcess();
    }

    @MessageMapping("/startpredict")
    @SendTo("/queue/progressvalues")
    public void startPredictProcess() throws LAException {
        rapidMinerAPI.runProcess();
    }
}
