import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private void Parser(String data) throws ParseException {
        String[] lines = data.split("\n");
        String dttm;
        String dttmPattern = "MMM dd yyyy HH:mm:ss z";
        SimpleDateFormat dtformat = new SimpleDateFormat(dttmPattern);
        String namespace;
        String operation;
        ArrayList<Integer> rec_count = new ArrayList<>();
        ArrayList<Integer> time_taken_in_ms = new ArrayList<>();

        //Parsing first line
        dttm = lines[0].split("INFO")[0].trim().replaceAll(".$", "");
        Date dt = dtformat.parse(dttm);
        String part1 = lines[0].split("histogram dump:")[1].trim();
        namespace = StringUtils.substringBetween(part1, "{", "}"); //namespace can be null for batch-index operations
        if(namespace == null)
            operation = part1.substring(0, part1.indexOf(" "));
        else
            operation = StringUtils.substringBetween(part1,  "{", " ");
        //Parsing subsequent lines
        for(int i=1; i<lines.length; i++) {
            String[] buckets = StringUtils.substringsBetween(lines[i], "(", ")");
            for (int j=2;j<buckets.length;j++) { //first 2 is for info, hist.c:341
                time_taken_in_ms.add(time_taken_in_ms.size(), (int)Math.pow(2, Integer.parseInt(buckets[j].split(":")[0].trim())));
                rec_count.add(rec_count.size(), Integer.valueOf(buckets[j].split(":")[1].trim()));
            }
        }

        //Create output
        for(int i=0; i<time_taken_in_ms.size();i++) {
            OutputFormat obj = new OutputFormat(dt, namespace, operation, rec_count.get(i), time_taken_in_ms.get(i));
            logger.log(Level.INFO, obj.getOutputRecord());
        }

    }
    public static void main(String[] args) throws IOException, ParseException {
        BasicConfigurator.configure(); //fix for log4j error. https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        //Validate input arguments
        if(args.length != 1) {
            logger.log(Level.ERROR, "Incorrect number of arguments specified. Please provide aerospike log file. Ex: /var/log/aerospike/aerospike.log");

        } else {
            HistogramParser hist = new HistogramParser();
            String filepath = args[0];
            hist.Parser(new String(Files.readAllBytes(Paths.get(filepath))));
        }
    }
}
