package StoredClasses;

import StoredClasses.annotations.Boundaries;
import StoredClasses.annotations.NotNull;
import exceptions.inputExceptions.NullObjectException;
import exceptions.inputExceptions.OutOfBoundsException;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * checkable interface
 */
public interface Checkable {
    /**
     * check if all field are correct. If not throws Exceptions
     */
    default void check() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(NotNull.class)) {
                try {
                    f.setAccessible(true);
                    if (f.get(this) == null)
                        throw new NullObjectException("Field \"" + f.getName() + "\" can't be null!");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (f.isAnnotationPresent(Boundaries.class)) {
                var bounds = f.getAnnotation(Boundaries.class);
                BigDecimal l = new BigDecimal(bounds.lowerBound());
                BigDecimal u = new BigDecimal(bounds.upperBound());
                try {
                    f.setAccessible(true);
                    if (l.compareTo(new BigDecimal(f.get(this).toString())) <= 0 && u.compareTo(new BigDecimal(f.get(this).toString())) >= 0)
                        continue;
                    throw new OutOfBoundsException("Field \"" + f.getName() + "\" must be between " + l + " and " + u);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
