package be.kul.mai.la.services;

import be.kul.mai.la.web.process.ProgressValue;
import com.rapidminer.Process;
import com.rapidminer.ProcessListener;
import com.rapidminer.operator.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of Rapid Miner Process Listener interface
 * Monitors the progress of a Rapid Miner Process run through the API
 */
@Service
public class ProcessListenerImpl implements ProcessListener {

    @Autowired
    public SimpMessageSendingOperations messagingTemplate;

    private int totalNumberOfProcesses = 0;
    private int completedNumberOfProcesses = 0;
    private List<Operator> mainOperators;

    public void attachListener(Process process)
    {
        process.getRootOperator().addProcessListener(this);
        mainOperators = process.getRootOperator().getImmediateChildren();
        totalNumberOfProcesses = mainOperators.size();
    }

    public void detachListener(Process process) {
        process.getRootOperator().removeProcessListener(this);
    }

    public double getProgress() {
        double progress = (double) completedNumberOfProcesses / (double) totalNumberOfProcesses;
        return progress * 100;
    }

    @Override
    public void processStarts(Process process) {
        completedNumberOfProcesses = 0;
    }

    @Override
    public void processStartedOperator(Process process, Operator op) {
    }

    @Override
    public void processFinishedOperator(Process process, Operator op) {
        if (mainOperators.contains(op)) {
            completedNumberOfProcesses += 1;
            sendProgress(op.getName(),getProgress());
        }
    }

    @Override
    public void processEnded(Process process) {

    }

    private void sendProgress(String opname, double progress){
        messagingTemplate.convertAndSend("/queue/progressvalues",
                new ProgressValue(opname,String.valueOf(progress)));
    }

//System.out.println(String.format("%d of %d - %.1f%% - %s",
//    completedNumberOfProcesses,
//    totalNumberOfProcesses,
//    getProgress(),
//                    op.getName()));

//    private int countSubprocesses(Operator op) {
//
//        if (op instanceof CrossValidationOperator) {
//            int sum = 0;
//            CrossValidationOperator oc = (CrossValidationOperator) op;
//
//            for (Operator childOperator : oc.getAllInnerOperators()) {
//                sum += countSubprocesses(childOperator);
//            }
//            return sum;
//        }
//        return 1;
//    }
}
