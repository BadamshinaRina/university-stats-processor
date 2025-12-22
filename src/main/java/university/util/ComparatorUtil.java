package university.util;

import university.comparator.StudentComparator;
import university.comparator.UniversityComparator;
import university.comparator.enums.StudentComporatorType;
import university.comparator.enums.UniversityComporatorType;
import university.comparator.student.StudentAvgExamScoreComparator;
import university.comparator.student.StudentCurrentCourseNumberComparator;
import university.comparator.student.StudentFullNameComparator;
import university.comparator.student.StudentUniversityIdComparator;
import university.comparator.university.*;

public class ComparatorUtil {
    private ComparatorUtil(){
        throw  new IllegalStateException("Utility class - создание экземпляров запрещено");
    }

    public static StudentComparator getStudentComparator(StudentComporatorType type) {
        switch (type) {
            case FULL_NAME: return new StudentFullNameComparator();
            case UNIVERSITY_ID: return new StudentUniversityIdComparator();
            case AVG_EXAM_SCORE: return new StudentAvgExamScoreComparator();
            case CURRENT_COURSE: return new StudentCurrentCourseNumberComparator();
            default: throw new IllegalArgumentException("Неизвестный тип для компаратора");
        }
    }

    public static UniversityComparator getUniversityComparator(UniversityComporatorType type) {
        switch (type) {
            case ID: return new UniversityIdComparator();
            case FULL_NAME: return new UniversityFullNameComparator();
            case SHORT_NAME: return new UniversityShortNameComparator();
            case MAIN_PROFILE: return new UniversityMainProfileComparator();
            case YEARS_OF_FOUNDATION: return new UniversityYearsOfFoundationComporator();
            default: throw new IllegalArgumentException("Неизвестный тип для компаратора");
        }
    }
}
