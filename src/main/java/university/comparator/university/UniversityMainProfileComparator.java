package university.comparator.university;

import org.apache.commons.lang3.StringUtils;
import university.comparator.UniversityComparator;
import university.model.University;

public class UniversityMainProfileComparator implements UniversityComparator {
    @Override
    public int compare(University u1, University u2) {
        String profile1= u1.getMainProfile()!=null?u1.getMainProfile().getRussianName():"";
        String profile2 = u2.getMainProfile()!=null?u2.getMainProfile().getRussianName():"";
        return StringUtils.compare(profile1, profile2);
    }
}
