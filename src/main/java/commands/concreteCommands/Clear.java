package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * clear command
 */
public class Clear extends Command {
    public Clear(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.clear();
        System.out.println("collection cleared");
    }

    @Override
    public String toString() {
        String res = "clear";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
