package university.app;


import university.model.Student;
import university.model.University;
import university.util.FileUtil;
import university.util.JsonUtil;
import university.util.XlsxReader;

import java.net.Socket;
import java.util.List;

public class UniversityApp {
    public static void main(String[] args) {
        System.out.println("\nJSON СЕРИАЛИЗАЦИЯ И ДЕСЕРИАЛИЗАЦИЯ ");
        try {
            String filePath = FileUtil.getFilePath("universities.xlsx");
            List<University>  universities = XlsxReader.readUniversities(filePath);
            List<Student>  students = XlsxReader.readStudents(filePath);

            System.out.println("\nДанные прочитаны: ");
            System.out.println("Найдено университетов: " + universities.size());
            System.out.println("Найдено студентов: " + students.size());

            System.out.println("\nСЕРИАЛИЗАЦИЯ КОЛЛЕКЦИЙ");

            String universitiesJson = JsonUtil.serializeUniversityList(universities);
            System.out.println("Json коллекции университетов");
            System.out.println(universitiesJson);

            String studentsJson = JsonUtil.serializeStudentList(students);
            System.out.println("Json колллекции студентов");
            System.out.println(studentsJson);

            System.out.println("\nДЕСЕРИАЛИЗАЦИЯ И ПРОВЕРКА");

            List <University> deserializedUniversities = JsonUtil.deserializeUniversityList(universitiesJson);
            List <Student> deserializedStudents = JsonUtil.deserializeStudentList(studentsJson);
            System.out.println("Проверка сериализации и десериализации университетов ->"  + (universities.size()==deserializedUniversities.size() ?
                    " Десериализация университетов прошла успешно": "Десериализация университетов прошла некоректно"));
            System.out.println("Проверка сериализации и десериализации студентов ->"  + (students.size()==deserializedStudents.size() ?
                    " Десериализация студентов прошла успешно": "Десериализация студентов прошла некоректно"));

            System.out.println("\nSTREAM API ОБРАБОТКА");
            System.out.println("\n Потоковая обработка университетов");
            universities.stream().limit(2).forEach(univer -> {
                String json = JsonUtil.serializeUniversity(univer);
                System.out.println("\nСериализовано " + json);

                University deserialized = JsonUtil.deserializeUniversity(json);
                System.out.println("Десериализовано " + deserialized);
            });

            System.out.println("Потоковая обработка студентов");
            students.stream().limit(2).forEach(stud ->{
                String json = JsonUtil.serializeStudent(stud);
                System.out.println("\nСериализовано " + json);

                Student deserialized = JsonUtil.deserializeStudent(json);
                System.out.println("Десериализовано " + deserialized);
            });

            System.out.println("\nПРОВЕРКА АННОТАЦИЯ @SerializedName");

            if(!students.isEmpty()) {
                Student testStudent = students.get(0);
                String json = JsonUtil.serializeStudent(testStudent);
                System.out.println("Json c аннотациями");
                System.out.println(json);

                Student fromJson= JsonUtil.deserializeStudent(json);
                System.out.println("Проверка работы аннотаций ->" + (testStudent.getFullName().equals(fromJson.getFullName())?
                        " Аннотации работают верно":"Аннотации работают некорректно"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
