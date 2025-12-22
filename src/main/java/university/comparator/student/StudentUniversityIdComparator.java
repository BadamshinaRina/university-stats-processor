package university.comparator.student;

import org.apache.commons.lang3.StringUtils;
import university.comparator.StudentComparator;
import university.model.Student;

public class StudentUniversityIdComparator implements StudentComparator {
    @Override
    public int compare(Student s1, Student s2) {
        return StringUtils.compare(s1.getUniversityId(), s2.getUniversityId());
    }
}
