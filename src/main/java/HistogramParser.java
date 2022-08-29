import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*Sample file contents
Aug 24 2022 15:10:45 GMT: INFO (info): (hist.c:321) histogram dump: {digitwin}-write (1219810 total) msec
Aug 24 2022 15:10:45 GMT: INFO (info): (hist.c:331)  (00: 0001218561) (01: 0000000999) (02: 0000000237) (03: 0000000013)
Aug 24 2022 15:10:55 GMT: INFO (info): (hist.c:321) histogram dump: {digitwin}-read (5376216 total) msec
Aug 24 2022 15:10:55 GMT: INFO (info): (hist.c:340)  (00: 0005376216)
Aug 24 2022 15:10:55 GMT: INFO (info): (hist.c:321) histogram dump: {digitwin}-write (1220815 total) msec
Aug 24 2022 15:10:55 GMT: INFO (info): (hist.c:331)  (00: 0001219565) (01: 0000001000) (02: 0000000237) (03: 0000000013)
Aug 24 2022 15:11:05 GMT: INFO (info): (hist.c:321) histogram dump: {digitwin}-read (5376216 total) msec
Aug 24 2022 15:11:05 GMT: INFO (info): (hist.c:340)  (00: 0005376216)
Aug 29 2022 08:59:58 GMT: INFO (info): (hist.c:321) histogram dump: batch-index (657567 total) msec
Aug 29 2022 08:59:58 GMT: INFO (info): (hist.c:340)  (00: 0000657500) (01: 0000000049) (02: 0000000018)
*/
public class HistogramParser {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HistogramParser.class);
    private void Parser(String data) {
        String[] lines = data.split("\n");
        String dttm;
        String namespace;
        String operation;
        ArrayList<Integer> rec_count = new ArrayList<>();
        ArrayList<Integer> time_taken_in_ms = new ArrayList<>();

        //Parsing first line
        dttm = lines[0].split("INFO")[0].trim();
        namespace = StringUtils.substringBetween(lines[0], "{", "}");
        if(lines[0].contains("batch-index"))
            operation = "batch-index";
        else if(lines[0].contains("write"))
            operation = "write";
        else if(lines[0].contains("read"))
            operation = "read";
        else {
            operation = "unknown";
            logger.log(Level.WARN, "Unknown operations type");
        }
        //Parsing subsequent lines
        for(int i=1; i<lines.length; i++) {
            String[] buckets = StringUtils.substringsBetween(lines[i], "(", ")");
            for (int j=2;j<buckets.length;j++) { //first 2 is for info, hist.c:341
                time_taken_in_ms.add(time_taken_in_ms.size(), (int)Math.pow(2, Integer.valueOf(buckets[j].split(":")[0].trim())));
                rec_count.add(rec_count.size(), Integer.valueOf(buckets[j].split(":")[1].trim()));
            }
        }

        //Create output
        for(int i=0; i<time_taken_in_ms.size();i++) {
            OutputFormat obj = new OutputFormat(dttm, namespace, operation, rec_count.get(i), time_taken_in_ms.get(i));
            logger.log(Level.INFO, obj.getOutputRecord());
        }

    }
    public static void main(String[] args) throws IOException {
        HistogramParser hist = new HistogramParser();
        String filepath = "C:\\Users\\ws_htu374\\Downloads\\DigiTwin\\misc\\as_log_sample.log";
        BasicConfigurator.configure(); //fix for log4j error. https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        hist.Parser(new String(Files.readAllBytes(Paths.get(filepath))));
    }
}
