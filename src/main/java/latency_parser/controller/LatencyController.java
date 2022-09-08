package latency_parser.controller;

import latency_parser.LatencyMain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class LatencyController {

    @RequestMapping(path="/metrics", method = RequestMethod.GET, produces = "text/plain")
    public String getLatencyMetrics() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(LatencyMain.outputFile));
        String line;
        while((line = bufferedReader.readLine())!=null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
