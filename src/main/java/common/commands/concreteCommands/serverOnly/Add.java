package common.commands.concreteCommands.serverOnly;

import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;


/**
 * add command
 */
public class Add extends Command {

    private Object arg;

    @Override
    public void setArgs(Reader from){
        arg = from.readObject();
    }

    @Override
    public Object execute() {
        collection.add(arg);
        return "element added";
    }

    @Override
    public String toString() {
        String res = "add";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }


}
