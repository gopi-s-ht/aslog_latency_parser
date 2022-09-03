package latency_parser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"latency_parser.*"})
public class LatencyMain {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LatencyMain.class);
    private static void printHelp() {
        logger.log(Level.INFO, "This jar accepts following arguments" +
                "1. Aerospike Log File (Required)" +
                "       Ex: /var/log/aerospike/aerospike.log" +
                "2. Boolean value (Optional). Default is True." +
                "       Set to true to tail from the end of the file, false to tail from the beginning of the file.");

    }

    public static String LogFilePath;
    public static Boolean tail;
    public static void main(String[] args) {
        BasicConfigurator.configure(); //fix for log4j error. https://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        //Validate input arguments
        if(args.length != 1 && args.length != 2) {
            logger.log(Level.ERROR, "Incorrect number of arguments specified. Use --help or -h to see the usage of the jar.");
            printHelp();
        } else if(args[0].equals("--help") || args[0].equals("-h")) {
            printHelp();
        } else {
            LogFilePath = args[0];
            tail = args.length != 2 || !args[1].equals("false");
            SpringApplication.run(LatencyMain.class, args);
            /*TailerListener listener = new LogFileListener();
            Tailer tailer;
            if(args.length == 2) {
                tailer = new Tailer(new File(args[0]), listener, 10000, Boolean.getBoolean(args[1]));
            } else {
                tailer = new Tailer(new File(args[0]), listener, 10000, true);
            }
            tailer.run(); */
        }
    }
}
