package university.util;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class JsonFileWriter {
    private static final Logger logger = LoggerUtil.getLogger(JsonFileWriter.class);

    private JsonFileWriter() {
        throw new IllegalStateException("Utility class - создание объектов запрещено");
    }

    private static void createJsonDirectory(){
        try{
            Path jsonDir = Paths.get("jsonReqs");
            if(!Files.exists(jsonDir)) {
                Files.createDirectories(jsonDir);
                logger.info("Создана директория Json " + jsonDir.toAbsolutePath());
            }
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при создании Json директории", e);
        }
    }

    private static  String generateFileName(String prefix, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timesTamp = dateFormat.format(new Date());
        return String.format(prefix + "_" + timesTamp+ "." + extension);
    }

    public static <T> String writeJsonFile(T object, String filePath) {
        logger.info("Запись объекта в Json файл");
        if(object==null) {
            logger.warning("Попытка записи пустого объекта");
        }
        try{
            String json = JsonUtil.serialize(object);
            JsonUtil.saveJsonToFile(json, filePath);

            File file = new File(filePath);
            if(file.exists()) {
                logger.fine("Файл создан, его размер " + file.length() + " байт");
            }
            return json;
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при попытке записи json файла",e);
            return "";
        }
    }

    public static <T> String writeJsonWithAutoName (T object, String prefix) {
        createJsonDirectory();
        String fileName = generateFileName(prefix, "json");
        String filePath = Paths.get("jsonReqs", fileName).toString();
        String json = writeJsonFile(object, filePath);
        return filePath;
    }

    public static <T> String writeJsonListFile(List<T> list,String filePath, String typeName) {
        logger.info("Запись списка в файл Json");

        if(list==null||list.isEmpty()) {
            logger.warning("Лист пуст, запись не произведена");
        }
        try{
            String json = JsonUtil.serializeList(list);
            JsonUtil.saveJsonToFile(json, filePath);
            return json;
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при записи списка в Json файл", e);
            return "";
        }
    }
}