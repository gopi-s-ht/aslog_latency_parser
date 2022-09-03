package latency_parser;

import java.util.Date;
//Latency will be recorded in below format
//Time,Namespace,Operation,Records,Latency in ms
public class ASLatency {
    Date dttm;
    String namespace;
    String operation;
    int rec_count;
    int time_taken_in_ms;
    final String DELIM = ",";
    public ASLatency(Date dttm, String namespace, String operation, int rec_count, int time_taken_in_ms) {
        this.dttm = dttm;
        this.namespace = namespace;
        this.operation = operation;
        this.rec_count = rec_count;
        this.time_taken_in_ms = time_taken_in_ms;
    }

    public String getOutputRecord() {
        return this.dttm.getTime()+DELIM+this.namespace+DELIM+this.operation+DELIM+this.rec_count+DELIM+this.time_taken_in_ms;
    }
}
