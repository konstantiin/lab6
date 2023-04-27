package common.StoredClasses.enums;

/**
 * stored class
 */
public enum Mood {
    SORROW,
    LONGING,
    GLOOM,
    CALM,
    FRENZY;
    @SuppressWarnings("unused")
    public static Mood valueOf(int i){
        return Mood.values()[i-1];
    }
}