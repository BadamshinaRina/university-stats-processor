package university.util;

import university.model.Statistics;
import university.model.Student;
import university.model.University;
import university.model.xml.StatisticsXml;
import university.model.xml.StudentXml;
import university.model.xml.UniversityDataXml;
import university.model.xml.UniversityXml;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataConverter {

    private static final Logger logger = LoggerUtil.getLogger(DataConverter.class);

    private DataConverter(){
        throw new IllegalStateException("Utility class - создание объектов класса запрещено");
    }

    public static StudentXml convertToStudentXml(Student student) {
        if(student==null) {
            return null;
        }
        return new StudentXml(student.getFullName(),student.getUniversityId(), student.getAvgExamScore());
    }

    public static UniversityXml convertToUniversityXml(University university) {
        if(university==null) {
            return null;
        }
        return new UniversityXml(university.getId(), university.getFullName(), university.getMainProfile().getRussianName());
    }

    public static StatisticsXml convertToStatisticsXml(Statistics statistics) {
        if(statistics==null) {
            return null;
        }
        String mainProfile = statistics.getMainProfile().getRussianName();
        BigDecimal avgScoreValue = statistics.getAvgExamScore();
        double avgScore = 0.0;
        if (avgScoreValue != null) {
            avgScore = avgScoreValue.doubleValue();
        }

        return new StatisticsXml(mainProfile, avgScore);
    }

    public static List<StudentXml> convertStudentListToXml (List <Student> students) {
        logger.fine("Конвертация коллекции студентов в XML формат");
        if(students==null||students.isEmpty()) {
            return Collections.emptyList();
        }
        return students.stream().map(DataConverter::convertToStudentXml).filter(xml->xml!=null).collect(Collectors.toList());
    }

    public static List <UniversityXml> convertUniversityListToXml (List <University> universities) {
        logger.fine("Конвертация коллекции университетов в XML формат");
        if(universities==null||universities.isEmpty()) {
            return Collections.emptyList();
        }
        return universities.stream().map(DataConverter::convertToUniversityXml).filter(xml->xml!=null).collect(Collectors.toList());
    }

    public static List<StatisticsXml> convertStatisticsListToXml (List <Statistics> statistics) {
        logger.fine("Конвертация коллекции статистик в XML формат");
        if(statistics==null||statistics.isEmpty()) {
            return Collections.emptyList();
        }
        return  statistics.stream().map(DataConverter::convertToStatisticsXml).filter(xml->xml!=null).collect(Collectors.toList());
    }
    public static UniversityDataXml createUniversityDataXml(List <Student> students, List <University> universities, List <Statistics> statistics) {
        logger.fine("Создание полного XML объекта из всех данных");
        List <StatisticsXml> statisticsXmlList = convertStatisticsListToXml(statistics);
        List <StudentXml> studentXmlList = convertStudentListToXml(students);
        List <UniversityXml> universityXmlList = convertUniversityListToXml(universities);

        return new UniversityDataXml(studentXmlList,universityXmlList,statisticsXmlList);
    }
}
