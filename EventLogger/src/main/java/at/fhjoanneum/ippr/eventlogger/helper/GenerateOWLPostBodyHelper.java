package at.fhjoanneum.ippr.eventlogger.helper;

import java.util.Map;

public class GenerateOWLPostBodyHelper {

    String processModelName;
    Map<String, String> pnmlFiles;

    public GenerateOWLPostBodyHelper() {
    }

    public GenerateOWLPostBodyHelper(String processModelName, Map<String, String> pnmlFiles) {
        this.processModelName = processModelName;
        this.pnmlFiles = pnmlFiles;
    }

    public String getProcessModelName() {
        return processModelName;
    }

    public void setProcessModelName(String processModelName) {
        this.processModelName = processModelName;
    }

    public Map<String, String> getPnmlFiles() {
        return pnmlFiles;
    }

    public void setPnmlFiles(Map<String, String> pnmlFiles) {
        this.pnmlFiles = pnmlFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenerateOWLPostBodyHelper that = (GenerateOWLPostBodyHelper) o;

        if (processModelName != null ? !processModelName.equals(that.processModelName) : that.processModelName != null)
            return false;
        return pnmlFiles != null ? pnmlFiles.equals(that.pnmlFiles) : that.pnmlFiles == null;
    }

    @Override
    public int hashCode() {
        int result = processModelName != null ? processModelName.hashCode() : 0;
        result = 31 * result + (pnmlFiles != null ? pnmlFiles.hashCode() : 0);
        return result;
    }
}
