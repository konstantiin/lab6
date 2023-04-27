package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * remove_lower command
 */
public class RemoveLower extends Command {

    public RemoveLower(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.removeLower(input.readObject()); // mb print amount of deleted elements
        System.out.println("Elements removed");
    }

    @Override
    public String toString() {
        String res = "remove_lower";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
