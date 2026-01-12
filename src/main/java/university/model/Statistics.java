package university.model;

import java.math.BigDecimal;
import java.util.Optional;

public class Statistics {

    private StudyProfile mainProfile;
    private BigDecimal avgExamScore;
    private int studentCount;
    private int universityCount;
    private String universityName;

    public Statistics(String universityName, int universityCount, int studentCount, BigDecimal avgExamScore, StudyProfile mainProfile) {
        this.universityName = universityName;
        this.universityCount = universityCount;
        this.studentCount = studentCount;
        this.avgExamScore = avgExamScore;
        this.mainProfile = mainProfile;
    }

    public StudyProfile getMainProfile() {
        return mainProfile;
    }

    public void setMainProfile(StudyProfile mainProfile) {
        this.mainProfile = mainProfile;
    }

    public BigDecimal getAvgExamScore() {
        return avgExamScore;
    }

    public void setAvgExamScore(BigDecimal avgExamScore) {
        this.avgExamScore = avgExamScore;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getUniversityCount() {
        return universityCount;
    }

    public void setUniversityCount(int universityCount) {
        this.universityCount = universityCount;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "mainProfile=" + mainProfile +
                ", avgExamScore=" + avgExamScore +
                ", studentCount=" + studentCount +
                ", universityCount=" + universityCount +
                ", universityName='" + universityName + '\'' +
                '}';
    }

    public Optional<BigDecimal> getAvgExamScoreOptional() {
        return Optional.ofNullable(avgExamScore);
    }

    public static class Builder{
        private StudyProfile mainProfile;
        private BigDecimal avgExamScore;
        private int studentCount;
        private int universityCount;
        private String universityName;

        public Builder setUniversityName(String universityName) {
            this.universityName = universityName;
            return this;
        }

        public Builder setUniversityCount(int universityCount) {
            this.universityCount = universityCount;
            return this;
        }

        public Builder setStudentCount(int studentCount) {
            this.studentCount = studentCount;
            return this;
        }

        public Builder setAvgExamScore(BigDecimal avgExamScore) {
            this.avgExamScore = avgExamScore;
            return this;
        }

        public Builder setAvgExamScore(double avgExamScore) {
            this.avgExamScore = BigDecimal.valueOf(avgExamScore);
            return this;
        }

        public Builder setMainProfile(StudyProfile mainProfile) {
            this.mainProfile = mainProfile;
            return this;
        }

        public Statistics build() {
            return new Statistics(universityName, universityCount,studentCount,avgExamScore, mainProfile);
        }
    }

}
