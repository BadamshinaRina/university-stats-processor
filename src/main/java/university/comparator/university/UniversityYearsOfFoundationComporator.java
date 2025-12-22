package university.comparator.university;

import university.comparator.UniversityComparator;
import university.model.University;

public class UniversityYearsOfFoundationComporator implements UniversityComparator {
    @Override
    public int compare(University u1, University u2) {
        return Integer.compare(u1.getYearsOfFoundation(), u2.getYearsOfFoundation());
    }
}
