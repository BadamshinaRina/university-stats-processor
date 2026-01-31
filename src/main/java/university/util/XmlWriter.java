package university.util;

import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import university.model.xml.UniversityDataXml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class XmlWriter {

    private static final Logger logger = LoggerUtil.getLogger(XmlWriter.class);
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(XmlWriter.class);

    private XmlWriter() {
        throw new IllegalStateException("Utility class -  создание объектов класса запрещено");
    }

    private static void createXmlDirectory() {
        try {
            Path xmlDir = Paths.get("xmlReqs");
            if (!Files.exists(xmlDir)) {
                Files.createDirectories(xmlDir);
                logger.info("Создана директория для XML файлов " + xmlDir.toAbsolutePath());
            }
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при создании директория", e);
        }
    }

    private static  String generateFileName(String prefix, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timesTamp = dateFormat.format(new Date());
        return String.format(prefix + "_" + timesTamp+ "." + extension);
    }

    public static void writeXmlFile(UniversityDataXml data, String filePath) {
        logger.info("Начало записи Xml файла в " +filePath);
        if(data==null) {
            logger.warning("Попытка записи пустых данных");
            return;
        }
        try{
            JAXBContext context = JAXBContext.newInstance(UniversityDataXml.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            File file = new File(filePath);
            marshaller.marshal(data, file);

            logger.info("Файл XML успешно создан ");
            logger.fine("Размер файла " + file.length() + " байт");
        } catch (JAXBException jaxbException) {
            LoggerUtil.logException(logger, "Ошибка JAXB при записи файла", jaxbException);
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Неожиданная ошибка при записи файла", e);
        }
    }

    public static String writeXmlWithAutoName(UniversityDataXml data) {
        createXmlDirectory();
        String fileName = generateFileName("university_data", "xml");
        String filePath = Paths.get("xmlReqs", fileName).toString();

        writeXmlFile(data, filePath);
        return filePath;
    }
    public static String writeXmlToString(UniversityDataXml data){
        logger.fine("Маршалинг XML в строку");

        try{
            JAXBContext context = JAXBContext.newInstance(UniversityDataXml.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(data, writer);

            String xmlString = writer.toString();
            logger.fine("XML строка сгенерирована, ее длина  " + xmlString.length()+  " символов");
            return xmlString;
        }
        catch (JAXBException e) {
            LoggerUtil.logException(logger, "Ошибка JAXB при маршалинге в строку", e);
            return "";
        }
    }
    public static void writeMultipleXmlFiles(UniversityDataXml data, int count) {
        logger.info("Пакетная запись "+ count+" файлов");
        createXmlDirectory();

        for(int i=0; i<count;i++) {
            String fileName = String.format("university_data_%02d_%d.xml", i, System.currentTimeMillis());
            String filePath = Paths.get("xmlReqs", fileName).toString();

            UniversityDataXml dataCopy = new UniversityDataXml(data.getStudents(), data.getUniversities(), data.getStatistics());
            writeXmlFile(dataCopy,filePath );

            if(i%5==0) {
                logger.fine("Записано " + i + " из " + count+ "файлов ");
            }
        }
        logger.info("Пакетная запись XML файлов завершена");

    }

}
