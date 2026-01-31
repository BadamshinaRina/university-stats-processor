package university.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="studentEntry")
@XmlAccessorType(XmlAccessType.FIELD)

public class StudentXml {

    @XmlElement(name="studentName", required = true)
    private String studentName;

    @XmlElement(name="universityId", required = true)
    private String universityId;

    @XmlElement(name = "avgExamScore", required = true)
    private double avgExamScore;

    public StudentXml(String studentName, String universityId, double avgExaScore) {
        this.studentName = studentName;
        this.universityId = universityId;
        this.avgExamScore = avgExaScore;
    }

    public StudentXml(){}

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public double getAvgExaScore() {
        return avgExamScore;
    }

    public void setAvgExaScore(double avgExaScore) {
        this.avgExamScore = avgExaScore;
    }

    @Override
    public String toString() {
        return "StudentXml{" +
                "studentName='" + studentName + '\'' +
                ", universityId='" + universityId + '\'' +
                ", avgExamScore=" + avgExamScore +
                '}';
    }
}
