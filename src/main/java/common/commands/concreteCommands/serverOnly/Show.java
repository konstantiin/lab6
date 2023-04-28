package common.commands.concreteCommands.serverOnly;


import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static server.launcher.CommandsLauncher.currentScripts;

/**
 * show command
 */
public class Show extends Command {
    @Override
    public Object execute() {
        return collection.show();
    }

    @Override
    public void setArgs(Reader from) {

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
