package client.reading.generators;

import common.StoredClasses.Car;
import common.StoredClasses.Coordinates;
import common.StoredClasses.HumanBeing;
import common.StoredClasses.enums.Mood;
import common.StoredClasses.enums.WeaponType;
import common.StoredClasses.forms.HumanBeingForm;

import java.util.HashMap;

/**
 * class to generate HumanBeing
 */
public class HumanBeingGenerator implements Generator {
    /**
     * @param fields - object fields
     * @return new HumanBeing object
     */
    @Override
    public Object generate(HashMap<String, Object> fields) {
        var form = new HumanBeingForm((String) fields.get("name"), (Coordinates) fields.get("coordinates"), (Boolean) fields.get("realHero"),
                (Boolean) fields.get("hasToothpick"), (Float) fields.get("impactSpeed"), (WeaponType) fields.get("weaponType"),
                (Mood) fields.get("mood"), (Car) fields.get("car"));
        return new HumanBeing(form);
    }
}
