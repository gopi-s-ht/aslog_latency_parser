import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class OutputFormat {
    Date dttm;
    String namespace;
    String operation;
    int rec_count;
    int time_taken_in_ms;
    final String DELIM = ",";
    public OutputFormat(Date dttm, String namespace, String operation, int rec_count, int time_taken_in_ms) {
        this.dttm = dttm;
        this.namespace = namespace;
        this.operation = operation;
        this.rec_count = rec_count;
        this.time_taken_in_ms = time_taken_in_ms;
    }

    public Date getDttm() {
        return dttm;
    }

    public void setDttm(Date dttm) {
        this.dttm = dttm;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRec_count() {
        return rec_count;
    }

    public void setRec_count(int rec_count) {
        this.rec_count = rec_count;
    }

    public int getTime_taken_in_ms() {
        return time_taken_in_ms;
    }

    public void setTime_taken_in_ms(int time_taken_in_ms) {
        this.time_taken_in_ms = time_taken_in_ms;
    }

    public String getOutputRecord() {
        return this.dttm.getTime()+DELIM+this.namespace+DELIM+this.operation+DELIM+this.rec_count+DELIM+this.time_taken_in_ms;
    }
}
