package latency_parser;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class LogFileListener extends TailerListenerAdapter {
    ArrayList<String> newData = new ArrayList<>();

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LogFileListener.class);
    @Override
    public void handle(String line) {
        if(line.contains("hist.c")) {
            //dispatch
            if (line.contains("histogram dump") && newData.size() > 1) {
                HistogramParser histParser = new HistogramParser();
                try {
                    histParser.Parser(String.join("\n", newData));
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
                newData = new ArrayList<>();
            }
            //accumulate
            newData.add(line);
        }
    }

    @Override
    public void endOfFileReached() {
        super.endOfFileReached();
        if(new File(LatencyMain.outputFile+".tmp").exists()) {
            try {
                Files.move(Paths.get(LatencyMain.outputFile + ".tmp"), Paths.get(LatencyMain.outputFile), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
