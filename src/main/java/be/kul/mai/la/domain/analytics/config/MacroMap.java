package be.kul.mai.la.domain.analytics.config;

import com.rapidminer.Process;
import com.rapidminer.tools.container.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// %{targets},"_",%{mapper}, "_", %{filtercode},"_", %{instrument},"_",%{nom_num},"_",%{statistic}

/**
 * Data structure that controls the RM macro values in the RM process
 * The Main RM process is controlled by the following parameters:
 * A_BC_th	-1                  : ROC threshold value, -1 use optimal
 * C_AB_th	-1                  : ROC threshold value, -1 use optimal
 * comprehensive	true        : Include resit grades as separate attributes
 * filtercode	11,12,13,21     : What semesters should be included
 * input	1                   : IR_IRA (1), IR(2), IRA(3), Grades only (4)
 * instrument	4               : Which instrument should be used for prediction
 * mapper	3                   : How is the target attribute mapped, 1=not, 2=binary, 3=ternary, ..., 5=fiveclasses
 * modelname instrument.model   : Instrument filename, based of off other parameters when "instrument.model"
 * nom_num	2                   : Nominal or numerical transformation
 * run_type	3                   : Attribute weights (1), Prediction (2), Training (3)
 * statistic	1               : Attribute weights statistic, correlation (1), chisquare (2), information gain (3)
 * studentids	all             : Predict for which students
 * targets	Doorloop: Studieduur: Target attribute name
 * type	regular                 : Prediction or training type: "regular", "roc", "multistage"
 */
public class MacroMap {

    private final Process process;
    private final Map<String, String> standardMap;
    private Map<String, String> macroMap;

    public MacroMap(Process process) {
        this.process = process;
        List<Pair<String, String>> macros = process.getContext().getMacros();
        standardMap = macros.stream()
                .collect(Collectors.toMap(item -> item.getFirst(), item -> item.getSecond()));
        macroMap = new HashMap<>(standardMap);
    }

    public List<Pair<String, String>> toPairList() {
        List<Pair<String, String>> macroList = new ArrayList<>();
        macroMap.entrySet()
                .forEach(entry -> macroList.add(new Pair(entry.getKey(), entry.getValue())));
        return macroList;
    }

    public MacroMap reset() {
        macroMap = new HashMap<>(standardMap);
        return this;
    }

    public MacroMap run_type(String value) {
        macroMap.put("run_type", value);
        return this;
    }

    public MacroMap targets(String value) {
        macroMap.put("targets", value);
        return this;
    }

    public MacroMap filtercode(String value) {
        macroMap.put("filtercode", value);
        return this;
    }

    public MacroMap input(String value) {
        macroMap.put("input", value);
        return this;
    }

    public MacroMap studentids(String value) {
        macroMap.put("studentids", value);
        return this;
    }

    public MacroMap mapper(String value) {
        macroMap.put("mapper", value);
        return this;
    }

    public MacroMap instrument(String value) {
        macroMap.put("instrument", value);
        return this;
    }

    public MacroMap nom_num(String value) {
        macroMap.put("nom_num", value);
        return this;
    }

    public MacroMap modelname(String value) {
        macroMap.put("modelname", value);
        return this;
    }

    public MacroMap statistic(String value) {
        macroMap.put("statistic", value);
        return this;
    }

    public MacroMap comprehensive(String value) {
        macroMap.put("comprehensive", value);
        return this;
    }

    public MacroMap type(String value) {
        macroMap.put("type", value);
        return this;
    }

    public MacroMap A_BC_th(String value) {
        macroMap.put("A_BC_th", value);
        return this;
    }

    public MacroMap C_AB_th(String value) {
        macroMap.put("C_AB_th", value);
        return this;
    }

    public void activate() {
        process.getContext().setMacros(this.toPairList());
    }

    public void setMacro(String key, String value) {
        macroMap.put(key, value);
    }

    public String getMacro(String key) {
        return macroMap.get(key);
    }

}
