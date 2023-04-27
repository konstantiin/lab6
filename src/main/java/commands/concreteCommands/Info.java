package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * info command
 */
public class Info extends Command {

    public Info(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.info();
    }

    @Override
    public String toString() {
        String res = "info";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
