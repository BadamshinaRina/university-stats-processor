package university.model;

public enum StudyProfile {
    MEDICINE("Медицина"),
    PHYSICS("Физика"),
    MATHEMATICS("Математика"),
    ARCHITECTURE("Архитектура"),
    CHEMISTRY("Химия"),
    COMPUTER_SCIENCE("Информатика");

    private final String profileName;

    StudyProfile (String profileName) {
        this.profileName=profileName;
    }

    public String getProfileName() {
        return profileName;
    }


    @Override
    public String toString() {
        return "StudyProfile {" + "profile name = " + profileName + " }";
    }
}
