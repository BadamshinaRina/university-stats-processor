package university.model;

import com.google.gson.annotations.SerializedName;

public class University {

    @SerializedName("id")
    private  String id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("yearsOfFoundation")
    private int yearsOfFoundation;

    @SerializedName("mainProfile")
    private StudyProfile mainProfile;

    public University () {}

    public University(String id, String fullName, String shortName, int yearsOfFoundation, StudyProfile mainProfile) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.yearsOfFoundation = yearsOfFoundation;
        this.mainProfile = mainProfile;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public int getYearsOfFoundation() {
        return yearsOfFoundation;
    }

    public StudyProfile getMainProfile() {
        return mainProfile;
    }

    public University setId(String id) {
        this.id = id;
        return this;
    }

    public University setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public University setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public University setYearsOfFoundation(int yearsOfFoundation) {
        this.yearsOfFoundation = yearsOfFoundation;
        return this;
    }

    public University setMainProfile(StudyProfile mainProfile) {
        this.mainProfile = mainProfile;
        return this;
    }

    @Override
    public String toString() {
        return "University{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", yearsOfFoundation=" + yearsOfFoundation +
                ", mainProfile=" + mainProfile +
                '}';
    }
}
