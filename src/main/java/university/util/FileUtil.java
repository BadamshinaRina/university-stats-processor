package university.util;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

public class FileUtil {

    private static final Logger logger = LoggerUtil.getLogger(FileUtil.class);

    private FileUtil () {
        throw new IllegalStateException("Utility class - создание объктов класса запрещено");
    }
     public static String getFilePath(String fileName){
        logger.info("Поиск файла "+ fileName);
        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
        if(resource!=null) {
            return resource.getPath();
        }

        File file = new File("src/main/resource/" + fileName);
        if(file.exists()) {
            return file.getAbsolutePath();
        }
        throw new RuntimeException("Файл " + fileName + " не найден");
     }

    }
