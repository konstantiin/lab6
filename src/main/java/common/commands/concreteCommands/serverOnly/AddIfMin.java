package common.commands.concreteCommands.serverOnly;

import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;

/**
 * add_if_min command
 */
public class AddIfMin extends Command {
    private Object arg;
    @Override
    public void setArgs(Reader from){
        arg = from.readObject();
    }


    @Override
    public Object execute() {
        if (collection.addIfMin(arg)) {
            return "element added";
        } else return "element no added";
    }

    @Override
    public String toString() {
        String res = "add_if_min";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}