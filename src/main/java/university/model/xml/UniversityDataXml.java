package university.model.xml;

import university.model.Statistics;
import university.model.Student;
import university.model.University;

import javax.xml.bind.annotation.*;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name="root")
@XmlAccessorType(XmlAccessType.FIELD)

public class UniversityDataXml {

    @XmlElementWrapper(name="studentsInfo")
    @XmlElement(name="studentEntry",required = true)
    private List<StudentXml> students = new ArrayList<>();

    @XmlElementWrapper(name="universityInfo")
    @XmlElement(name="universityEntry",required = true)
    private List<UniversityXml> universities = new ArrayList<>();

    @XmlElementWrapper(name="statisticsInfo")
    @XmlElement(name="statisticsEntry",required = true)
    private List<StatisticsXml> statistics = new ArrayList<>();

    @XmlElement(name="processedAt",required = true)
    private Date processedAt;

    public UniversityDataXml() {
        this.processedAt = new Date();
    }

    public UniversityDataXml(List<StudentXml> students, List<UniversityXml> universities, List<StatisticsXml> statistics) {
        this.students = students!=null? students:new ArrayList<>();
        this.universities = universities!=null? universities:new ArrayList<>();
        this.statistics = statistics!=null? statistics:new ArrayList<>();
        this.processedAt = new Date();
    }

    public List<StudentXml> getStudents() {
        return students;
    }

    public void setStudents(List<StudentXml> students) {
        this.students = students;
    }

    public List<UniversityXml> getUniversities() {
        return universities;
    }

    public void setUniversities(List<UniversityXml> universities) {
        this.universities = universities;
    }

    public List<StatisticsXml> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<StatisticsXml> statistics) {
        this.statistics = statistics;
    }

    public Date getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Date processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return "UniversityDataXml{" +
                "students=" + students +
                ", universities=" + universities +
                ", statistics=" + statistics +
                ", processedAt=" + processedAt +
                '}';
    }

    public void addStudent(StudentXml stud) {
        if(students==null) {
            students=new ArrayList<>();
        }
        students.add(stud);
    }

    public void universityAdd(UniversityXml univ) {
        if(universities==null) {
            universities=new ArrayList<>();
        }
        universities.add(univ);
    }

    public void statisticsAdd(StatisticsXml stat) {
        if(statistics==null) {
            statistics=new ArrayList<>();
        }
        statistics.add(stat);
    }
}
