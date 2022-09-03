package latency_parser;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        TailerListener listener = new LogFileListener();
        Tailer tailer = new Tailer(new File(LatencyMain.LogFilePath), listener, 10000, LatencyMain.tail);
        tailer.run();
    }
}
