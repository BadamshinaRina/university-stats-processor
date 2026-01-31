package university.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="statisticsEmpty")
@XmlAccessorType(XmlAccessType.FIELD)

public class StatisticsXml {

    @XmlElement(name="universityProfile",required = true)
    private String universityProfile;

    @XmlElement(name="avgExamScore",required = true)
    private double avgExamScore;

   public StatisticsXml(){}

    public StatisticsXml(String universityProfile, double avgExamScore) {
        this.universityProfile = universityProfile;
        this.avgExamScore = avgExamScore;
    }

    public String getUniversityProfile() {
        return universityProfile;
    }

    public void setUniversityProfile(String universityProfile) {
        this.universityProfile = universityProfile;
    }

    public double getAvgExamScore() {
        return avgExamScore;
    }

    public void setAvgExamScore(double avgExamScore) {
        this.avgExamScore = avgExamScore;
    }

    @Override
    public String toString() {
        return "StatisticsXml{" +
                "universityProfile='" + universityProfile + '\'' +
                ", avgExamScore=" + avgExamScore +
                '}';
    }
}
