package university.util;

import university.model.Statistics;
import university.model.Student;
import university.model.StudyProfile;
import university.model.University;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsUtil {

    public StatisticsUtil () {
        throw new IllegalArgumentException("Utility class - создание объекта класса запрещено");
    }
    private static final int SCALE = 2;

    public static List<Statistics> collectStatistics(List <University> universities, List <Student> students) {
        System.out.println("СБОР СТАТИСТИКИ");
        Map<StudyProfile, List <University>> universitiesByProfile = universities.stream()
                .filter(u->u.getMainProfile()!=null).collect(Collectors.groupingBy(University::getMainProfile));
        System.out.println("\nУниверситеты по профилям: ");
        universitiesByProfile.forEach((profile, univList)->
                System.out.println(" " + profile.getEnglishName() + " : " + univList.size()));

        Map<String, List<Student>> studentsByUniversity = students.stream()
                .filter(s->s.getUniversityId()!=null).collect(Collectors.groupingBy(Student::getUniversityId));

        List <Statistics> statisticsList = new ArrayList<>();

        for(Map.Entry<StudyProfile, List <University>> entry: universitiesByProfile.entrySet()) {
            StudyProfile profile = entry.getKey();
            List <University> profileUniversities = entry.getValue();

            Optional <Statistics> statisticsOptional = calculateStatisticsForProfile (profile, profileUniversities, studentsByUniversity);

            statisticsOptional.ifPresent(statisticsList::add);
        }
        statisticsList.sort(Comparator.comparing(stat->stat.getMainProfile().getEnglishName()));
        System.out.println("Собрано статистик " + statisticsList.size());
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

    public static Statistics getOveralStatistics(List <Student> students, List <University> universities) {
        OptionalDouble overallAvg = students.stream().mapToDouble(Student::getAvgExamScore).average();
        String allUniversityName = universities.stream().map(University::getShortName).sorted().collect(Collectors.joining());

        return new Statistics.Builder().setMainProfile(null).setAvgExamScore(overallAvg.isEmpty()?roundToTwoDecimalPlaces(overallAvg.getAsDouble()):null)
                .setStudentCount(students.size()).setUniversityCount(universities.size()).setUniversityName(allUniversityName).build();
    }

    public static List <Statistics> getTopProfileByScope (List <Statistics> stat, int limit, boolean ascending) {
        return stat.stream().filter(s -> s.getAvgExamScore()!=null).sorted((s1,s2)->{
            int comparison = s1.getAvgExamScore().compareTo(s2.getAvgExamScore());
             return ascending?comparison:-comparison;
        }).limit(limit).collect(Collectors.toList());
    }

    public static List <Statistics> filterByMinStudent(List <Statistics> stat, int minStudent) {
        return stat.stream().filter(s->s.getStudentCount()>=minStudent).collect(Collectors.toList());
    }


}
