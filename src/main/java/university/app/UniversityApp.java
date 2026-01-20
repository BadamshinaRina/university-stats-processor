package university.app;


import university.comparator.StudentComparator;
import university.comparator.UniversityComparator;
import university.comparator.enums.StudentComporatorType;
import university.comparator.enums.UniversityComporatorType;
import university.model.Statistics;
import university.model.Student;
import university.model.University;
import university.util.*;


import java.util.List;
import java.util.logging.Logger;

public class UniversityApp {
    private static final Logger logger = LoggerUtil.getLogger(UniversityApp.class);
    public static void main(String[] args) {

        try{
            LoggerUtil.initializeLogging();
            logger.info("ЗАПУСК СИСТЕМЫ ОБРАБОТКИ ДАННЫХ УНИВЕРСИТЕТОВ");

            logger.info("1 Этап: Чтение данных. ");
            String dataFile = FileUtil.getFilePath("universities.xlsx");
            List<University> universities = XlsxReader.readUniversities(dataFile);
            List <Student> students = XlsxReader.readStudents(dataFile);

            logger.info(String.format("\n Прочитано данных: Университетов=%d, Студентов=%d ", universities.size(), students.size()));

            logger.info("2 Этап: Сортировка данных. ");
            StudentComparator studentComparator = ComparatorUtil.getStudentComparator(StudentComporatorType.AVG_EXAM_SCORE);
            UniversityComparator universityComparator = ComparatorUtil.getUniversityComparator(UniversityComporatorType.FULL_NAME);
            logger.fine("Компараторы получены " + studentComparator.getClass().getSimpleName() + universityComparator.getClass().getSimpleName());
            universities.sort(universityComparator);
            students.sort(studentComparator);

            logger.info("3 Этап: Сбор статистики. ");
            List <Statistics> statistics = StatisticsUtil.collectStatistics(universities, students);
            logger.info("Собрано статистик " + statistics.size());
            statistics.forEach(stat-> logger.fine(String.format("Статистика: %s - студенты: %d, университеты: %d", stat.getMainProfile().getRussianName(),
                    stat.getStudentCount(), stat.getUniversityCount())));

            logger.info("4 Этап: Создание Excel отчета");
            String reportFile = "university_statistics_report.xlsx";
            XlsWriter.generateStatisticsTable(statistics,reportFile);
            logger.info("Отчет создан " + reportFile);

            logger.info("5 Этап: Проверка корректности: ");
            int totalStudentsInStatistics = statistics.stream().mapToInt(Statistics::getStudentCount).sum();
            boolean studentsMatch = totalStudentsInStatistics==students.size();
            if(studentsMatch) {
                logger.info("Проверка пройдена, статистика собрана верно");
            }
            else {
                logger.warning("Несоответствие количества статистик и количества студентов");
            }

        }
        catch (Exception e) {
            LoggerUtil.logException(logger, "Критическая ошибка при выполнении программы", e);
            logger.info("Подробности в лог-файле: logs/university_0.log\"");

        }

    }
}
