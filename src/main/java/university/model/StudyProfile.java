package university.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum StudyProfile {
    @SerializedName("Medicine")
    MEDICINE("Медицина", "Medicine"),
    @SerializedName("Physics")
    PHYSICS("Физика", "Physics"),
    @SerializedName("Mathematics")
    MATHEMATICS("Математика", "Mathematics"),
    @SerializedName("Architecture")
    ARCHITECTURE("Архитектура", "Architecture"),
    @SerializedName("Chemistry")
    CHEMISTRY("Химия", "Chemistry"),
    @SerializedName("Computer_science")
    COMPUTER_SCIENCE("Информатика", "Computer_science"),
    @SerializedName("Linguistics")
    LINGUISTICS("Лингвистика", "Linguistics");

    private final String russianName;
    private final String englishName;
    private static final Map<String, StudyProfile> RUSSIAN_NAME_MAP = new HashMap<>();
    private static final Map<String, StudyProfile> ENGLISH_NAME_MAP = new HashMap<>();

    StudyProfile(String russianName, String englishName) {
        this.russianName = russianName;
        this.englishName = englishName;
    }

    static  {
        for (StudyProfile profile: values()) {
            RUSSIAN_NAME_MAP.put(profile.russianName.toLowerCase(), profile);
            ENGLISH_NAME_MAP.put(profile.englishName.toLowerCase(), profile);
            ENGLISH_NAME_MAP.put(profile.name().toLowerCase(), profile);
        }
    }

    public String getRussianName() {
        return russianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    @Override
    public String toString() {
        return "StudyProfile{" +
                "russianName='" + russianName + '\'' +
                '}';
    }

    public static StudyProfile fromString(String text) {
        if(text==null||text.trim().isEmpty()) {
            return COMPUTER_SCIENCE;
        }
        String cleanText=text.trim().toLowerCase();

        if(RUSSIAN_NAME_MAP.containsKey(cleanText)) {
            return RUSSIAN_NAME_MAP.get(cleanText);
        }
        if(ENGLISH_NAME_MAP.containsKey(cleanText)) {
            return ENGLISH_NAME_MAP.get(cleanText);
        }
        for(StudyProfile profile: values()) {
            if(cleanText.contains(profile.russianName.toLowerCase())||
            profile.russianName.toLowerCase().contains(cleanText)||
            cleanText.contains(profile.englishName.toLowerCase())||
            profile.englishName.toLowerCase().contains(cleanText)) {
                return profile;
            }
        }
        return COMPUTER_SCIENCE;
    }
}
