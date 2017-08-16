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
 */
public class MacroMap {

    private final Process process;
    private final Map<String, String> standardMap;
    private Map<String, String> macroMap;
    private String[] nameComponents = "targets,mapper,filtercode,instrument,nom_num,statistic".split(",");

    public MacroMap() {
        process = null;
        standardMap = new HashMap<>();
        macroMap = new HashMap<>();
    }

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

    public MacroMap A_BC_th(String value) {
        macroMap.put("A_BC_th", value);
        return this;
    }

    public MacroMap C_AB_th(String value) {
        macroMap.put("C_AB_th", value);
        return this;
    }

    public void activate() {
        StringBuilder modelname = new StringBuilder();
        for (String name : nameComponents) {
            if (macroMap.containsKey(name)) {
                modelname
                        .append(macroMap.get(name).replaceAll("( |:|,)", "_"))
                        .append("_");
            }
        }
        modelname.delete(modelname.lastIndexOf("_"), modelname.length());
        macroMap.put("modelname", modelname.toString());
        process.getContext().setMacros(this.toPairList());
    }

    public void setMacro(String key, String value) {
        macroMap.put(key, value);
    }

    public String getMacro(String key) {
        return macroMap.get(key);
    }

}
