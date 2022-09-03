package latency_parser;

import org.apache.commons.io.input.TailerListenerAdapter;

import java.text.ParseException;
import java.util.ArrayList;

public class LogFileListener extends TailerListenerAdapter {
    ArrayList<String> newData = new ArrayList<>();

    @Override
    public void handle(String line) {
        if(line.contains("hist.c")) {
            //dispatch
            if (line.contains("histogram dump") && newData.size() > 1) {
                HistogramParser histParser = new HistogramParser();
                try {
                    histParser.Parser(String.join("\n", newData));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newData = new ArrayList<>();
            }
            //accumulate
            newData.add(line);
        }
    }
}
