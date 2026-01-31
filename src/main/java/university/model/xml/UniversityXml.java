package university.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="universityEmpty")
@XmlAccessorType(XmlAccessType.FIELD)

public class UniversityXml {
    @XmlElement(name="universityId", required = true)
    private String universityId;

    @XmlElement(name="universityName", required = true)
    private String universityName;

    @XmlElement(name="universityProfile", required = true)
    private String universityProfile;

   public UniversityXml(){}

    public UniversityXml(String universityId, String universityName, String universityProfile) {
        this.universityId = universityId;
        this.universityName = universityName;
        this.universityProfile = universityProfile;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityProfile() {
        return universityProfile;
    }

    public void setUniversityProfile(String universityProfile) {
        this.universityProfile = universityProfile;
    }

    @Override
    public String toString() {
        return "UniversityXml{" +
                "universityId='" + universityId + '\'' +
                ", universityName='" + universityName + '\'' +
                ", universityProfile='" + universityProfile + '\'' +
                '}';
    }
}
