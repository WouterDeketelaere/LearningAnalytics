package be.kul.mai.la.web.process;

public class ProgressValue {

    private String progressValue;
    private String processName;

    public ProgressValue(String processName, String progressValue) {
        this.processName = processName;
        this.progressValue = progressValue;
    }

    public String getProgressValue() {
        return progressValue;
    }

    public String getProcessName() {
        return processName;
    }
}
