package commands.concreteCommands;

import StoredClasses.HumanBeing;
import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * filter_contains_name
 */
public class FilterContainsName extends Command {

    public FilterContainsName(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        var list = collection.filterContainsName(input.readString());
        for (HumanBeing item : list) System.out.println(item);
        if (list.size() == 0) System.out.println("Nothing matches pattern");
    }

    @Override
    public String toString() {
        String res = "filter_contains_name";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
