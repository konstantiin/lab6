package common.commands.concreteCommands.serverOnly;


import client.reading.readers.Reader;
import common.commands.abstraction.Command;

import static server.launcher.CommandsLauncher.currentScripts;

/**
 * remove_greater command
 */
public class RemoveGreater extends Command {

    private Object arg;

    @Override
    public Object execute() {
        collection.removeGreater(arg);
        return "Elements removed";             // mb print amount of deleted elements
    }

    @Override
    public void setArgs(Reader from) {
        arg = from.readObject();
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
