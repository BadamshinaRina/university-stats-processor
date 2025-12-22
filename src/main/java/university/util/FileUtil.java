package university.util;

import java.io.File;
import java.net.URL;

public class FileUtil {

    private FileUtil () {
        throw new IllegalStateException("Utility class - создание объктов класса запрещено");
    }
     public static String getFilePath(String fileName){
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

     public static boolean fileExists (String filePath) {
        return new File(filePath).exists();
     }
}
