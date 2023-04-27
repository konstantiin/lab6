package common.commands.concreteCommands.clientOnly;

import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;

/**
 * exit
 */
public class Exit extends Command {
    private Reader input;
    @Override
    public Object execute() {
        input.closeStream();
        StringBuilder res = new StringBuilder();
        res.append("Exit ");
        if (currentScripts.size() != 0)
            res.append("from ").append(currentScripts.get(currentScripts.size() - 1)).append(" script");
        res.append("complete");
        return res;
    }

    @Override
    public void setArgs(Reader from) {
        input = from;
    }

    @Override
    public String toString() {
        String res = "exit";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}