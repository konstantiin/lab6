package common.commands.concreteCommands.serverOnly;

import client.reading.readers.Reader;
import common.commands.abstraction.Command;

import java.io.Serializable;

import static server.launcher.CommandsLauncher.currentScripts;

/**
 * remove_lower command
 */
public class RemoveLower extends Command implements Serializable {

    private Object arg;

    @Override
    public Object execute() {
        collection.removeLower(arg); // mb print amount of deleted elements
        return "Elements removed";
    }

    @Override
    public void setArgs(Reader from) {
        arg = from.readObject();
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
