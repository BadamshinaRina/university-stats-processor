package university.app;


import university.comparator.StudentComparator;
import university.comparator.UniversityComparator;
import university.comparator.enums.StudentComporatorType;
import university.comparator.enums.UniversityComporatorType;
import university.model.Student;
import university.model.University;
import university.util.ComparatorUtil;
import university.util.FileUtil;
import university.util.XlsxReader;

import java.util.List;

public class UniversityApp {
    public static void main(String[] args) {
        System.out.println("Демонстрация компараторов и STREAM API");
        try {
            String filePath = FileUtil.getFilePath("universities.xlsx");
            List<University> universities = XlsxReader.readUniversities(filePath);
            List<Student> students = XlsxReader.readStudents(filePath);

            StudentComparator studentComparator1 = ComparatorUtil.getStudentComparator(StudentComporatorType.AVG_EXAM_SCORE);
            StudentComparator studentComparator2 = ComparatorUtil.getStudentComparator(StudentComporatorType.CURRENT_COURSE);
            UniversityComparator universityComparator1 = ComparatorUtil.getUniversityComparator(UniversityComporatorType.FULL_NAME);
            UniversityComparator universityComparator2 = ComparatorUtil.getUniversityComparator(UniversityComporatorType.YEARS_OF_FOUNDATION);

            System.out.println("Сортировка студентов по среднему баллу");
            students.stream().sorted(studentComparator1).forEach(System.out::println);

            System.out.println("Сортировка студентов по номеру курса");
            students.stream().sorted(studentComparator2).forEach(System.out::println);

            System.out.println("Сортировка университетов по полному названию: ");
            universities.stream().sorted(universityComparator1).forEach(System.out::println);

            System.out.println("Сетировка униеврситетов по году основания");
            universities.stream().sorted(universityComparator2).forEach(System.out::println);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
