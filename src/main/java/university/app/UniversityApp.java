package university.app;


import university.model.Statistics;
import university.model.Student;
import university.model.University;
import university.util.FileUtil;
import university.util.StatisticsUtil;
import university.util.XlsWriter;
import university.util.XlsxReader;

import java.net.Socket;
import java.util.List;

public class UniversityApp {
    public static void main(String[] args) {
        System.out.println("\nСОЗДАНИЕ ОТЧЕТОВ ПО СТАТИСТИКЕ УНИВЕРСИТЕТОВ");

        try{
            String dataFile = FileUtil.getFilePath("universities.xlsx");
            List<University> universities = XlsxReader.readUniversities(dataFile);
            List <Student> students = XlsxReader.readStudents(dataFile);

            System.out.println("\n Прочитано данных");
            System.out.println("Университетов " + universities.size());
            System.out.println("Студентов " + students.size());

            System.out.println("\n СБОР СТАТИСТИКИ");
            List <Statistics> statistics = StatisticsUtil.collectStatistics(universities, students);

            System.out.println("\n Собранная статистика");
            statistics.forEach(System.out::println);

            System.out.println("СОЗДАНИЕ ЕКСЕЛЬ ОТЧЕТА");
            String reportFile = "university_statistics_report.xlsx";
            XlsWriter.generateStatisticsTable(statistics,reportFile);

            System.out.println("\n Создание детализированного отчета");
            String detailReportFile= "university_statistics_detail_report.xlsx";
            XlsWriter.generateDetailReport(statistics,detailReportFile);

            System.out.println("\n Проверка корректности: ");
            System.out.println("Стастик собрано " + statistics.size());
            int totalStudentsInStatistics = statistics.stream().mapToInt(Statistics::getStudentCount).sum();
            System.out.println("Всего студентов в статистике " + totalStudentsInStatistics);
            System.out.println("Отчет создан корректно?... " + (totalStudentsInStatistics==students.size()? "Все корректно ": "Отчет создан неккоректно"));

        }
        catch (Exception e) {
            System.out.println("Ошибка создания отчетов " + e.getMessage());
            e.printStackTrace();
        }

    }
}
