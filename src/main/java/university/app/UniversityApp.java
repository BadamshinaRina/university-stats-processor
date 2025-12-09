package university.app;
import university.model.University;
import university.model.Student;
import university.model.StudyProfile;

public class UniversityApp {
    public static void main(String[] args) {
        University university1 = new University("U-001", "Казанский химико-технологический университет", "КХТИ", 1890, StudyProfile.CHEMISTRY);
        University university2 = new University("U-002", "Казанский государственный медицинский университет", "КГМУ", 1814, StudyProfile.MEDICINE);
        University university3 = new University("U-003", "Казанский государственный архитектурно-строительный университет", "КГАСУ", 1999, StudyProfile.ARCHITECTURE);

        Student student1 = new Student("Петр Иванов", "U-001", 1, 8.99f);
        Student student2 = new Student("Мария Иванова", "U-002", 2, 9.98f);
        Student student3 = new Student("Федор Петров", "U-003", 3, 9.33f);

        System.out.println("===Университеты===");
        System.out.println(university1);
        System.out.println(university2);
        System.out.println(university3);

        System.out.println("===Студенты===");
        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);

            }
}
