package university.app;


import university.comparator.StudentComparator;
import university.comparator.UniversityComparator;
import university.comparator.enums.StudentComporatorType;
import university.comparator.enums.UniversityComporatorType;
import university.model.Statistics;
import university.model.Student;
import university.model.University;
import university.service.ExportService;
import university.util.*;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class UniversityApp {
    private static final Logger logger = LoggerUtil.getLogger(UniversityApp.class);
    public static void main(String[] args) {

        try{

            initializeApplication();

            logger.info("ЗАПУСК СИСТЕМЫ ОБРАБОТКИ ДАННЫХ УНИВЕРСИТЕТОВ");

            logger.info("1 Этап: Чтение данных. ");
            String filePath = getDataFilePath();
            List<University> universities = XlsxReader.readUniversities(filePath);
            List <Student> students = XlsxReader.readStudents(filePath);

            logDataInfo(students,universities);

            logger.info("2 Этап: Сортировка данных. ");
            sortData(students,universities);


            logger.info("3 Этап: Сбор статистики. ");
            List <Statistics> statistics = StatisticsUtil.collectStatistics(universities, students);
            logger.info("Собрано статистик " + statistics.size());
            statistics.forEach(stat-> logger.fine(String.format("Статистика: %s - студенты: %d, университеты: %d", stat.getMainProfile().getRussianName(),
                    stat.getStudentCount(), stat.getUniversityCount())));

            logger.info("4 Этап: Полный экспорт данных ");
            ExportService.ExportResult result = ExportService.exportAllData(students,universities,statistics);

            logger.info("4 Этап: Результаты экспорта ");
            printExportResult(result);

            logger.info("Программа завершена успешно");
        }
        catch (Exception e) {
            LoggerUtil.logException(logger, "Критическая ошибка при выполнении программы", e);
            logger.info("Подробности в лог-файле: logs/university_0.log\"");

        }
    }
    private static void initializeApplication() {
        LoggerUtil.initializeLogging();
        try{
            Files.createDirectories(Paths.get("data"));
            Files.createDirectories(Paths.get("logs"));
            Files.createDirectories(Paths.get("exports"));
            logger.fine("Директории приложения инициализированы");
        } catch (Exception e) {
            logger.warning("Не удалось создать директории " + e.getMessage());
        }
    }
    private static String getDataFilePath(){
        String [] possiblePath = {"universities.xlsx",
                "data/universities.xlsx",
                "src/main/resources/universities.xlsx",
                System.getProperty("user.dir") + "/universities.xlsx"
        };
        for(String path:possiblePath) {
            if(Files.exists(Paths.get(path))) {
                logger.info("Найден файл данных " + path);
                return path;
            }
        }
        throw new RuntimeException("Файл universities.xlsx не найден");
    }

    private static void logDataInfo(List <Student> students, List <University> universities) {
        logger.info(String.format("Прочитано данных:\n" + "  • Студентов: %d\n" + "  • Университетов: %d",
                students.size(), universities.size()));

        if (!students.isEmpty()) {
            logger.fine("Пример студента: " + students.get(0));
        }
        if (!universities.isEmpty()) {
            logger.fine("Пример университета: " + universities.get(0));
        }

    }

    private static void sortData(List <Student> students, List <University> universities) {
        StudentComparator studentComparator = ComparatorUtil.getStudentComparator(StudentComporatorType.AVG_EXAM_SCORE);
        UniversityComparator universityComparator = ComparatorUtil.getUniversityComparator(UniversityComporatorType.FULL_NAME);
        logger.fine("Компараторы получены " + studentComparator.getClass().getSimpleName() + universityComparator.getClass().getSimpleName());
        universities.sort(universityComparator);
        students.sort(studentComparator);
    }

    private static void printExportResult(ExportService.ExportResult result) {
        if (result.isSuccess()) {
            logger.info("Экспорт успешно завершен!");
            logger.info("Созданные файлы:");
            logger.info("  • XML:  " + result.getXmlFilePath());
            logger.info("  • JSON: " + result.getJsonFilePath());
            logger.info("  • Excel: " + result.getExcelFilePath());
            logger.info("  • Отчет: " + result.getReportPath());
            logger.info("  • Архив: " + result.getArchivePath());
        } else {
            logger.severe("Экспорт завершился с ошибкой!");
            logger.severe("Причина: " + result.getErrorMessage());
        }
    }
}
