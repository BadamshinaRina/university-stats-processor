package university.comparator.student;

import university.comparator.StudentComparator;
import university.model.Student;

public class StudentAvgExamScoreComparator implements StudentComparator {
    @Override
    public int compare(Student s1, Student s2) {
        return Float.compare(s1.getAvgExamScore(), s2.getAvgExamScore());
    }
}
