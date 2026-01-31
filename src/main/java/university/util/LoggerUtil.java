package university.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggerUtil {

    private static final String LOGGING_PROPERTIES = "logging.properties";
    private static boolean initialized = false;
    private static final String LOG_DIR = "logs";

    private  LoggerUtil() {
        throw new IllegalStateException("Utility class - Создание объектов класс запрещено");
    }

    public static void initializeLogging() {
        if (initialized) {
            return;
        }
        try{
            Files.createDirectories(Paths.get(LOG_DIR));
            Logger rootLogger = Logger.getLogger("");

            Handler [] handlers = rootLogger.getHandlers();
            for(Handler handler: handlers) {
                rootLogger.removeHandler(handler);
            }
            rootLogger.setLevel(Level.ALL);

            String logFilePattern = LOG_DIR + "/university_processor_%g.log";
            FileHandler fileHandler = new FileHandler(logFilePattern, 1024*1024, 5, true);
            fileHandler.setFormatter(new CustomFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new CustomFormatter());
            consoleHandler.setLevel(Level.INFO);
            rootLogger.addHandler(consoleHandler);

            Logger xmlLogger = Logger.getLogger("org.example.util.XmlWriter");
            xmlLogger.setLevel(Level.FINE);

            Logger jsonLogger = Logger.getLogger("org.example.util.JsonUtil");
            jsonLogger.setLevel(Level.FINE);

            initialized=true;
            getLogger(LoggerUtil.class).info("Система логирования инициализирована");
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации логорования " + e.getMessage());
        }

    }

    public static Logger getLogger(Class <?> clazz){
        return Logger.getLogger(clazz.getName());
    }

     public static void logException (Logger logger, String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

     public static void fine (Logger logger, String message) {
        if(logger.isLoggable(Level.FINER)) {
            logger.finer(message);
        }
    }

    public static void logXmlOperation (Logger logger, String operation, String details) {
        logger.log(Level.INFO, "[XML] {0}: {1}", new Object[]{operation, details});
    }

    public static void logJsonOperation (Logger logger, String operation, String details) {
        logger.log(Level.INFO, "[JSON] {0}: {1}", new Object[]{operation, details});
    }

    public static void logFileOperation (Logger logger, String operation, String filePath) {
        logger.log(Level.INFO, "[FILE] {0}: {1}", new Object[]{operation, filePath});
    }

    private static class CustomFormatter extends Formatter {

        private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        @Override
        public String format(LogRecord record) {

            StringBuilder sb = new StringBuilder();
            sb.append(dateFormat.format(new Date(record.getMillis())));
            sb.append(" ");

            sb.append(String.format("%-7s", record.getLevel().getName()));
            sb.append(" ");

            if(record.getSourceClassName()!=null) {
                String className = record.getSourceClassName();
                if(className.contains(".")) {
                    className = className.substring(className.lastIndexOf(".")+1);
                }
                sb.append(className);

                if(record.getSourceMethodName()!=null) {
                    sb.append(".");
                    sb.append(record.getSourceMethodName());
                }
                sb.append("-");
            }
            sb.append(formatMessage(record));
            sb.append("\n");

            if(record.getThrown()!=null) {
                sb.append("Exception: ");
                Throwable throwable  = record.getThrown();
                sb.append(throwable.toString());
                sb.append("\n");

                for(StackTraceElement element: throwable.getStackTrace()) {
                    sb.append("\t at");
                    sb.append(element.toString());
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
    }
}
