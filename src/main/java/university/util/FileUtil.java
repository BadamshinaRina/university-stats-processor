package university.util;


import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class FileUtil {

    private static final Logger logger = LoggerUtil.getLogger(FileUtil.class);

    private FileUtil () {
        throw new IllegalStateException("Utility class - создание объктов класса запрещено");
    }

     public static String getFilePath(String fileName){
       String [] searchPaths = {fileName, "data/" + fileName, "src/main/resources " + fileName,
       System.getProperty("user.dir") + "/" + fileName};

       for(String path: searchPaths) {
           Path filePath = Paths.get(path);
           if(Files.exists(filePath)) {
               logger.info("Файл найден " + filePath.toAbsolutePath());
               return filePath.toString();
           }
       }
       logger.warning("Файл не найден " + fileName);
       return fileName;
     }

     public static Path createDirectory (String dirName) throws IOException {
        Path dirPath = Paths.get(dirName);
        if(!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            LoggerUtil.logFileOperation(logger, "Директория создана", dirPath.toString());
        }
        return dirPath;
     }

     public static  void writeFileContent (String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
     }

     public static void copyFile(String sourcePath, String destPath) throws IOException {
        Path source = Paths.get (sourcePath);
        Path dest = Paths.get(destPath);
        Files.copy(source,dest, StandardCopyOption.REPLACE_EXISTING);
        logger.fine("Файл скопирован из " + sourcePath + " в " + destPath);

     }
    }
