package be.kul.mai.la.domain.analytics.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RapidMinerAPITest {

    @Autowired
    private RapidMinerAPI rapidMinerAPI;

    @Test
    public void runROC() throws Exception {
        rapidMinerAPI.getMacroMap()
                .reset()
                .run_type("2")
                .type("roc")
                .instrument("7")
                .activate();
        rapidMinerAPI.setMacroValue("studentids", "2609;189631");
        List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
        rapidminerResults.forEach(rapidminerResult -> System.out.println(rapidminerResult.toJSON()));
    }

    @Test
    public void runPredict() throws Exception {
        rapidMinerAPI.getMacroMap()
                .reset()
                .run_type("2")
                .type("ensemble")
                .instrument("1")
                .studentids("2609;189631")
                .activate();
        List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
        rapidminerResults.forEach(rapidminerResult -> System.out.println(rapidminerResult.toJSON()));
    }

    @Test
    public void runTrain() throws Exception {
        rapidMinerAPI.setProcessName("Main");
        for (int instrument = 1; instrument < 6;instrument++) {
            for (int mapper = 1; mapper < 6 ; mapper++) {
                rapidMinerAPI.getMacroMap()
                        .reset()
                        .run_type("2")
                        .type("ensemble")
                        .filtercode("11,12,13")
                        .comprehensive("true")
                        .instrument(String.valueOf(instrument))
                        .mapper(String.valueOf(mapper))
                        .activate();
                List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
            }
            //rapidminerResults.forEach(rapidminerResult -> System.out.println(rapidminerResult.toJSON()));
        }
    }

    @Test
    public void runRadar() throws Exception{
        rapidMinerAPI.setProcessName("ClusterAnalysis");
        List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
        System.out.println("Dummy");
    }
}