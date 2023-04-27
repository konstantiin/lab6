package commands.concreteCommands;


import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * show command
 */
public class Show extends Command {
    public Show(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.show();
    }

    @Override
    public String toString() {
        String res = "show";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
