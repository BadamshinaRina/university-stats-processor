package university.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerUtil {

    private static final String LOGGING_PROPERTIES = "logging.properties";
    private static boolean initialized = false;

    private  LoggerUtil() {
        throw new IllegalStateException("Utility class - Создание объектов класс запрещено");
    }

    public static void initializeLogging() {
        if(initialized) {return;}

        try(InputStream configStream = LoggerUtil.class.getClassLoader().getResourceAsStream(LOGGING_PROPERTIES)){
            if(configStream==null) {
                System.err.println("Конфигурационный файл logging.properties не найден");
                setupDefaultLogging();
            }
            else {
                LogManager.getLogManager().readConfiguration(configStream);
                getLogger(LoggerUtil.class).info("Логирование инициализировано из LOGGING_PROPERTIES");
            }
            try{
                createLogsDirectory();
            } catch (Exception e) {

            }

            initialized=true;
        } catch (IOException e) {
            System.err.println("Ошибка при чтении конфигурации логирования " + e.getMessage());
            setupDefaultLogging();
        }
        catch (SecurityException e) {
            System.err.println("Ошибка безопасности при настройке логирования " + e.getMessage());
        }
    }
    private static void setupDefaultLogging() {
        try {

            java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
            java.util.logging.Handler[] handlers = rootLogger.getHandlers();
            for (java.util.logging.Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }
            java.util.logging.ConsoleHandler consoleHandler = new java.util.logging.ConsoleHandler();
            consoleHandler.setFormatter(new java.util.logging.SimpleFormatter() {
                @Override
                public String format(java.util.logging.LogRecord record) {
                    return String.format("[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%2$-7s] %3$s %n",
                            record.getMillis(),
                            record.getLevel().getName(),
                            record.getMessage()
                    );
                }
            });
            consoleHandler.setLevel(java.util.logging.Level.ALL);
            rootLogger.addHandler(consoleHandler);
            rootLogger.setLevel(java.util.logging.Level.INFO);

        } catch (Exception e) {

        }
    }

    public static Logger getLogger(Class <?> clazz){
        return Logger.getLogger(clazz.getName());
    }
    private static void createLogsDirectory() {
        try {
            Path logsPath = Paths.get("logs");
            if (!Files.exists(logsPath)) {
                Files.createDirectories(logsPath);

            }
        } catch (IOException e) {

        }
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    public static void logException (Logger logger, String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }
    public static void debug (Logger logger, String message) {
        if(logger.isLoggable(Level.FINE)) {
            logger.fine(message);
        }
    }
    public static void fine (Logger logger, String message) {
        if(logger.isLoggable(Level.FINER)) {
            logger.finer(message);
        }
    }
}
