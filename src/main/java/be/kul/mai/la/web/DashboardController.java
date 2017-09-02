package be.kul.mai.la.web;

import be.kul.mai.la.domain.analytics.engine.RapidMinerAPI;
import be.kul.mai.la.domain.analytics.engine.RapidminerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling all Learning Analytics related requests.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private RapidMinerAPI rapidMinerAPI;
    private List<RapidminerResult> rocResults;
    private List<RapidminerResult> clusterResults;
    private Map<String, String> thresholds;

    @Autowired
    public DashboardController(RapidMinerAPI rapidMinerAPI) {
        this.rapidMinerAPI = rapidMinerAPI;
        thresholds = new HashMap<>();
        thresholds.put("AvsBC", "-1");
        thresholds.put("CvsAB", "-1");
    }

    @RequestMapping(value = "weights")
    public String weighAttributes(@RequestParam(value = "filtercode", required = false, defaultValue = "11")
                                          String filtercode,
                                  @RequestParam(value = "input", required = false, defaultValue = "1")
                                          String input,
                                  @RequestParam(value = "statistic", required = false, defaultValue = "1")
                                          String statistic,
                                  @RequestParam(value = "comprehensive", required = false, defaultValue = "false")
                                          String comprehensive) {

        rapidMinerAPI.getMacroMap()
                .reset()
                .run_type("1")
                .filtercode(filtercode)
                .input(input)
                .statistic(statistic)
                .comprehensive(comprehensive)
                .activate();

        List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
        return rapidminerResults.get(0).toJSON();
    }

    @RequestMapping(value = "predict")
    public String predict(@RequestParam(value = "instrument", required = false, defaultValue = "1")
                                  String instrument,
                          @RequestParam(value = "mapper", required = false, defaultValue = "1")
                                  String mapper,
                          @RequestParam(value = "statistic", required = false, defaultValue = "1")
                                  String statistic,
                          @RequestParam(value = "filtercode", required = false, defaultValue = "11")
                                  String filtercode,
                          @RequestParam(value = "studentids", required = false, defaultValue = "all")
                                  String studentids,
                          @RequestParam(value = "comprehensive", required = false, defaultValue = "false")
                                  String comprehensive,
                          @RequestParam(value = "type", required = false, defaultValue = "regular")
                                  String type) {

        rapidMinerAPI.getMacroMap()
                .reset()
                .run_type("2")
                .targets("Doorloop: Studieduur")
                .filtercode(filtercode)
                .input("1")
                .mapper(mapper)
                .instrument(instrument)
                .statistic(statistic)
                .nom_num("2")
                .studentids(studentids)
                .comprehensive(comprehensive)
                .type(type)
                .activate();

        List<RapidminerResult> rapidminerResults = rapidMinerAPI.runProcess();
        return rapidminerResults.get(0).toJSON();
    }

    @RequestMapping("tracks")
    public String getTracks(@RequestParam(value = "studentids", required = false, defaultValue = "all")
                                    String studentids,
                            @RequestParam(value = "which", required = true)
                                    String which,
                            @RequestParam(value = "threshold", required = false, defaultValue = "-1")
                                    String threshold) {

        mapThresholds(which, threshold);
        rapidMinerAPI.getMacroMap()
                .reset()
                .run_type("2")
                .filtercode(mapFiltercode("9"))
                .input("1")
                .type("roc")
                .studentids(studentids)
                .comprehensive("true")
                .A_BC_th(thresholds.get("AvsBC"))
                .C_AB_th(thresholds.get("CvsAB"))
                .activate();

        this.rocResults = rapidMinerAPI.runProcess();
        return rocResults.get(0).toJSON();
    }

    @RequestMapping("createclusters")
    public void createClusters() {

        rapidMinerAPI.setProcessName("ClusterAnalysis");
        this.clusterResults = rapidMinerAPI.runProcess();
        rapidMinerAPI.setProcessName("Main");
    }

    @RequestMapping("getclusters")
    public String getCluster(@RequestParam(value="type", required = false,defaultValue = "0") Integer type) {

        return clusterResults.get(type).toJSON();
    }



    @RequestMapping(value = "roc")
    public String getROCCurve(@RequestParam(value = "which", required = false, defaultValue = "track")
                                      String which) {

        switch (which) {
            case "optimal":
                return rocResults.get(1).toJSON();
            case "avsbc":
                return rocResults.get(2).toJSON();
            case "cvsab":
                return rocResults.get(3).toJSON();
            default:
                return rocResults.get(0).toJSON();
        }
    }

    private void mapThresholds(String which, String threshold) {

        switch (which) {
            case "avsbc":
                thresholds.put("AvsBC", threshold);
            default:
                thresholds.put("CvsAB", threshold);
        }
    }

    /**
     * Maps number onto filter code.
     * Note that that the code is reversed at the end.
     *
     * @param filtercode
     * @return
     */
    private String mapFiltercode(String filtercode) {
        StringBuilder output = new StringBuilder();
        switch (filtercode) {
            case "9":
                output.append(",33");
            case "8":
                output.append(",23");
            case "7":
                output.append(",13");
            case "6":
                output.append(",32");
            case "5":
                output.append(",22");
            case "4":
                output.append(",12");
            case "3":
                output.append(",31");
            case "2":
                output.append(",21");
            default:
                output.append(",11");
        }
        return output.reverse().deleteCharAt(output.lastIndexOf(",")).toString();
    }
}
