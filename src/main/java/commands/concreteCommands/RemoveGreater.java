package commands.concreteCommands;


import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * remove_greater command
 */
public class RemoveGreater extends Command {
    public RemoveGreater(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.removeGreater(input.readObject());
        System.out.println("Elements removed");             // mb print amount of deleted elements
    }

    @Override
    public String toString() {
        String res = "remove_greater";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
