package common.commands.launcher;


import common.StoredClasses.Coordinates;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.IdException;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;
import client.App;
import client.reading.readers.OfflineReader;

import java.io.File;
import java.util.*;


/**
 * realizes all the common.commands
 *
 * @param <T> - stored class
 */
public class CommandsLauncher<T extends Comparable<T>> {
    /**
     * scripts that are currently executing
     */
    public static ArrayList<File> currentScripts = new ArrayList<>();
    /**
     * managed collection
     */
    private final TreeSet<T> collection;

    /**
     * @param collection - managed collection
     */
    public CommandsLauncher(TreeSet<T> collection) {
        this.collection = collection;
    }

    /**
     * adds element to collection
     *
     * @param element - element to add
     */
    @SuppressWarnings("unchecked")
    public void add(Object element) {
        collection.add((T) element);
    }

    /**
     * adds element if it is less than any element in collection
     *
     * @param element - element to add
     * @return true if element was added
     */
    @SuppressWarnings("unchecked")

    public boolean addIfMin(Object element) {
        T value = (T) element;

        if (value.compareTo(collection.first()) < 0) {
            this.add(value);
            return true;
        }
        return false;
    }

    /**
     * returns elements with given substring in their names
     *
     * @param pattern - substring to search in names
     * @return list of elements with give substring in names
     */
    public List<HumanBeing> filterContainsName(String pattern) {
        List<HumanBeing> result = new ArrayList<>();
        for (T element : collection) {
            HumanBeing human = (HumanBeing) element;
            if (human.getName().contains(pattern)) result.add(human);
        }
        return result;
    }

    /**
     * groups HumanBeings by coordinates
     *
     * @return HumanBeings grouped by coordinates
     */
    public HashMap<Coordinates, List<HumanBeing>> groupCountingByCoordinates() {
        HashMap<Coordinates, List<HumanBeing>> groups = new HashMap<>();
        for (T element : collection) {
            HumanBeing human = (HumanBeing) element;
            if (groups.containsKey(human.getCoordinates())) {
                groups.get(human.getCoordinates()).add(human);
                continue;
            }
            var value = new ArrayList<HumanBeing>();
            value.add(human);
            groups.put(human.getCoordinates(), value);
        }
        return groups;

    }

    /**
     * prints information about collection
     */
    public void info() {
        System.out.println("TreeSet " + collection + " of size " + collection.size());// возможно стоит вывести еще какую-то информацию
    }

    /**
     * Deletes element with given id
     *
     * @param id - element with this id will be removed
     * @throws IdException - if element with id does not exist
     */
    public void removeById(long id) throws IdException {
        for (var x : collection) {
            HumanBeing human = (HumanBeing) x;
            if (human.getId() == id) {
                HumanBeing.ids.remove(id);
                collection.remove(x);
                return;
            }
        }
        throw new IdException("Not valid id");
    }

    /**
     * removes all the elements that are less than given element
     *
     * @param element - element to compare
     */    @SuppressWarnings("unchecked")

    public void removeLower(Object element) {
        T value = (T) element;
        T lower = collection.lower(value);
        while (lower != null) {
            if (lower instanceof HumanBeing){
                HumanBeing.ids.remove(((HumanBeing) lower).getId());
            }
            collection.remove(lower);
            lower = collection.lower(value);
        }
    }

    /**
     * removes all the elements that are greater than given element
     *
     * @param element - element to compare
     */    @SuppressWarnings("unchecked")

    public void removeGreater(Object element) {
        T value = (T) element;
        T greater = collection.higher(value);
        while (greater != null) {
            if (greater instanceof HumanBeing){
                HumanBeing.ids.remove(((HumanBeing) greater).getId());
            }
            collection.remove(greater);
            greater = collection.higher(value);
        }
    }

    /**
     * shows elements of collection
     */
    public void show() {
        System.out.println("Collection:");
        for (var item : collection) {
            System.out.println(item);
        }
        System.out.println("Collection ended");
    }

    /**
     * @return sum of impactSpeed
     */
    public double sumOfImpactSpeed() {
        double sum = 0.0;
        for (var element : collection) {
            HumanBeing human = (HumanBeing) element;
            sum += human.getImpactSpeed();
        }
        return sum;
    }

    /**
     * updates element with given id
     *
     * @param id      element id
     * @param element new element
     * @throws IdException - if element with id does not exist
     */
    public void update(long id, Object element) throws IdException {
        for (var item : collection) {
            HumanBeing human = (HumanBeing) item;
            if (human.getId() == id) {
                human.update((HumanBeing) element);
                return;
            }
        }
        throw new IdException("Not valid id");
    }

    /**
     * clears collection
     */
    public void clear() {
        HumanBeing.ids.clear();
        collection.clear();
    }

    /**
     * executes script
     *
     * @param reader - Reader of script
     */

    /**
     * saves collection
     */
    @SuppressWarnings("unchecked")
    public void save() {
        App.XMLInput.writeArr(new ArrayList<>((Collection< HumanBeing>) collection));
    }
}
