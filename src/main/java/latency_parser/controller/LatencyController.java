package latency_parser.controller;

import latency_parser.HistogramParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan("latency_parser")
public class LatencyController {

    @Autowired
    @Qualifier("ParseRepository")
    private HistogramParser parser ;

    @RequestMapping(path="/metrics", method = RequestMethod.GET)
    public String getLatencyMetrics() {
        return parser.getLatencyRecords();
    }
}
