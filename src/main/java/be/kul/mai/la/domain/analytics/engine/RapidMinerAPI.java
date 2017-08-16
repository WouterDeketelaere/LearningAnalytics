package be.kul.mai.la.domain.analytics.engine;

import be.kul.mai.la.domain.analytics.config.MacroMap;
import be.kul.mai.la.services.ProcessListenerImpl;
import be.kul.mai.la.services.exceptions.LAException;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.RepositoryProcessLocation;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessSetupError;
import com.rapidminer.operator.text.Document;
import com.rapidminer.repository.ProcessEntry;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.tools.XMLException;
import com.rapidminer.tools.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper class around Rapid Miner API.
 * Makes Rapid Miner API available as a Spring Component
 */
@Component
public class RapidMinerAPI {

    private final ProcessListenerImpl processListener;
    private Process process;
    private String baseProcessLocation = "//LearningAnalytics/processes/";
    private String processName = "Main";
    private String processFile;
    private MacroMap macroMap;

    /**
     * Initialize Rapid Miner API and load extra plugins
     */
    @Autowired
    public RapidMinerAPI(ProcessListenerImpl processListener) {
        this.processListener = processListener;
        processFile = baseProcessLocation + processName;
        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        Plugin.addAdditionalExtensionDir("lib");
        Plugin.setInitPlugins(true);
        RapidMiner.init();
        createProcess();
    }

    /**
     * This creates a RapidMiner Process from a process file
     */
    public void createProcess() throws LAException {

        try {
            RepositoryLocation pLoc = new RepositoryLocation(processFile);
            ProcessEntry pEntry = (ProcessEntry) pLoc.locateEntry();
            String processXML = pEntry.retrieveXML();
            process = new Process(processXML);
            process.setProcessLocation(new RepositoryProcessLocation(pLoc));

            // build internal representation of macros
            macroMap = new MacroMap(process);

        } catch (OperatorException | RepositoryException | IOException | XMLException e) {
            throw new LAException("Could not initialize RapidminerService", e);
        }
    }

    /**
     * Runs a Rapid Miner process that output a JSON reprentation of the results.
     *
     * @return JSON String
     * @throws LAException if process doesn't contain a JSON Output Operator (JSON to Data)
     */
    public List<RapidminerResult> runProcess() throws LAException {

        // attach process processListener so we can track process progress
        processListener.attachListener(process);

        // check if this process has a JSON output
        Operator json_output = process.getOperator("Generate output");
        if (json_output == null)
            throw new LAException("Process doesn't output JSON");

        IOContainer ioResult = null;
        try {
            // run Rapid Miner process
            ioResult = process.run();

            // collect results
            return Arrays.stream(ioResult.getIOObjects())
                    .map(ioObject -> new RapidminerResult((Document) ioObject))
                    .collect(Collectors.toList());

        } catch (OperatorException e) {
            List<ProcessSetupError> errorList = process.getRootOperator().getErrorList();
            throw new LAException(e);

        } finally {
            this.processListener.detachListener(process);
        }
    }

    public void setMacroValue(String key, String value) {
        macroMap.setMacro(key, value);
        process.getContext().setMacros(macroMap.toPairList());
    }

    public void setProcessName(String processName) {
        if (processName.isEmpty() || processName == null)
            throw new LAException("RapidMiner Service: incorrect process name");
        this.processName = processName;
        processFile = baseProcessLocation + processName;
        createProcess();
    }

    public MacroMap getMacroMap() {
        return macroMap;
    }

}
