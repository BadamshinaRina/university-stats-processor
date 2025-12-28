package university.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import university.model.Student;
import university.model.University;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class JsonUtil {
    private JsonUtil(){
        throw new IllegalStateException("Utility class - создание экземпляров запрещено");
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static String serializeStudent(Student student) {
        return GSON.toJson(student);
    }

    public static String serializeUniversity(University university) {
        return GSON.toJson(university);
    }
    public static String serializeStudentList(List <Student> students) {
        return GSON.toJson(students);
    }

    public static String serializeUniversityList(List <University> universities) {
        return GSON.toJson(universities);
    }

    public static Student deserializeStudent(String json) {
        return GSON.fromJson(json, Student.class);
    }

    public static University deserializeUniversity(String json) {
        return GSON.fromJson(json, University.class);
    }

    public static List<Student> deserializeStudentList(String json) {
        Type studentListType = new TypeToken<List<Student>>(){}.getType();
        return GSON.fromJson(json, studentListType);
    }

    public static List <University> deserializeUniversityList(String json) {
        Type universityListType = new TypeToken<List<University>>(){}.getType();
        return GSON.fromJson(json, universityListType);
    }

    public static void saveJsonToFile(String json, String filePath) {
        try(java.io.FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
            System.out.println("JSON сохранен в файл " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения JSON в файл " + e.getMessage());
        }
    }

    public static String readJsonFromFile(String filePath) {
        try(FileReader reader = new FileReader(filePath)) {
           return new Scanner(reader).useDelimiter("\\A").next();
        }
        catch (IOException e) {
            System.err.println("Ошибка чтения JSON из файла "+ e.getMessage());
            return "";
        }
    }
    public static boolean isValidJson (String json) {
        try{
            GSON.fromJson(json, Object.class);
            return true;
        }
        catch (JsonSyntaxException je) {
            return false;
        }
    }


}
