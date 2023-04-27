package common.commands.concreteCommands.serverOnly;


import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;

/**
 * remove_greater command
 */
public class RemoveGreater extends Command {

    @Override
    public Object execute() {
        collection.removeGreater(arg);
        return "Elements removed";             // mb print amount of deleted elements
    }

    private Object arg;

    @Override
    public void setArgs(Reader from){
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
