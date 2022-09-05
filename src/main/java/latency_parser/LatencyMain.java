package latency_parser;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.chainsaw.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@SpringBootApplication(scanBasePackages = {"latency_parser.*"})
public class LatencyMain {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LatencyMain.class);
    private static void printHelp() {
        logger.log(Level.INFO, "This jar accepts following arguments\n" +
                "1. Aerospike Log File (Required)\n" +
                "       Ex: /var/log/aerospike/aerospike.log\n" +
                "2. Boolean value (Optional). Default is True.\n" +
                "       Set to true to tail from the end of the file, false to tail from the beginning of the file.\n");

    }

    public static String logFilePath;
    public static Boolean tail;
    public static String outputFile;
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure(); //fix for log4j error. https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        String log4jConfig="/log4j.properties";
        Properties props = new Properties();
        InputStream is = Main.class.getResourceAsStream(log4jConfig);
        props.load(is);
        PropertyConfigurator.configure(props);
        //Validate input arguments
        if(args.length != 1 && args.length != 2) {
            logger.log(Level.ERROR, "Incorrect number of arguments specified. Use --help or -h to see the usage of the jar.\n");
            printHelp();
        } else if(args[0].equals("--help") || args[0].equals("-h")) {
            printHelp();
        } else {
            logFilePath = args[0];
            File logFile = new File(logFilePath);
            //Check if as_log_latency is present.
            if(!logFile.exists()) {
                logger.log(Level.ERROR, logFilePath + " doesn't exists. Terminating...");
                System.exit(1);
            }
            if(!logFile.canRead()) {
                logger.log(Level.ERROR, logFilePath + " is not readable, Please check file permissions. Terminating...");
                System.exit(1);
            }
            tail = args.length != 2 || !args[1].equals("false");
            if(System.getProperty("os.name").contains("Windows"))
                outputFile = "C:\\Users\\ws_htu374\\Documents\\as_latency_metrics.csv";
            else outputFile = "/var/log/as_latency_report.csv";

            logger.log(Level.INFO, "Sending metrics to " + outputFile);
            SpringApplication.run(LatencyMain.class, args);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void StartParser() {
        TailerListener listener = new LogFileListener();
        Tailer tailer = new Tailer(new File(LatencyMain.logFilePath), listener, 10000, LatencyMain.tail);
        tailer.run();
    }
}
