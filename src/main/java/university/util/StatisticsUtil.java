package university.util;


import university.model.Statistics;
import university.model.Student;
import university.model.StudyProfile;
import university.model.University;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatisticsUtil {

    public StatisticsUtil () {
        throw new IllegalArgumentException("Utility class - создание объекта класса запрещено");
    }
    private static final Logger logger = LoggerUtil.getLogger(StatisticsUtil.class);
    private static final int SCALE = 2;

    public static List<Statistics> collectStatistics(List <University> universities, List <Student> students) {
        logger.info("Начало сбора статистики");
        Map<StudyProfile, List <University>> universitiesByProfile = universities.stream()
                .filter(u->u.getMainProfile()!=null).collect(Collectors.groupingBy(University::getMainProfile));
        logger.fine("\nУниверситеты по профилям: ");

        Map<String, List<Student>> studentsByUniversity = students.stream()
                .filter(s->s.getUniversityId()!=null).collect(Collectors.groupingBy(Student::getUniversityId));

        List <Statistics> statisticsList = new ArrayList<>();

        for(Map.Entry<StudyProfile, List <University>> entry: universitiesByProfile.entrySet()) {
            StudyProfile profile = entry.getKey();
            List <University> profileUniversities = entry.getValue();

            logger.fine("Обработка профиля " + profile.getRussianName() + "( университетов " + profileUniversities.size()+ " )");

            Optional <Statistics> statisticsOptional = calculateStatisticsForProfile (profile, profileUniversities, studentsByUniversity);

            statisticsOptional.ifPresent(statisticsList::add);
        }
        statisticsList.sort(Comparator.comparing(stat->stat.getMainProfile().getEnglishName()));
       logger.info("Собрано статистик " + statisticsList.size());
        return statisticsList;
    }

    private static Optional <Statistics> calculateStatisticsForProfile(StudyProfile profile, List <University> profileUniversities, Map <String, List <Student>> studentsByUniversity) {
        if(profileUniversities.isEmpty()) {
            return Optional.empty();
        }
        List <Student> profileStudents = new ArrayList<>();

        for(University universities: profileUniversities) {
            List<Student> universityStudents = studentsByUniversity.get(universities.getId());
            if(universityStudents!=null) {
                profileStudents.addAll(universityStudents);
            }
        }

        Optional <BigDecimal> avgScopeOptional = calculateAverageScore(profileStudents);
        List <String> universityNames = profileUniversities.stream().map(University::getShortName).sorted().collect(Collectors.toList());

        Statistics statistics = new Statistics.Builder().setMainProfile(profile).setAvgExamScore(avgScopeOptional.orElse(null)).setStudentCount(profileStudents.size())
                .setUniversityName(universityNames.toString()).setUniversityCount(profileUniversities.size()).build();

        return Optional.of(statistics);

    }

    private static Optional<BigDecimal>calculateAverageScore (List <Student> students) {
        if(students.isEmpty()||students==null) {
            return Optional.empty();
        }
        OptionalDouble average = students.stream().mapToDouble(Student::getAvgExamScore).average();

        return average.isPresent() ? Optional.of(roundToTwoDecimalPlaces(average.getAsDouble())): Optional.empty();
    }

    public static BigDecimal roundToTwoDecimalPlaces(double value) {
        return BigDecimal.valueOf(value).setScale(SCALE, RoundingMode.HALF_UP);
    }


}
