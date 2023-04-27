package common.StoredClasses.enums;

/**
 * stored class
 */
public enum WeaponType {
    PISTOL,
    SHOTGUN,
    RIFLE,
    MACHINE_GUN;
    @SuppressWarnings("unused")
    public static WeaponType valueOf(int i){
        return WeaponType.values()[i-1];
    }
}