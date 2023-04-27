package common.commands.concreteCommands.serverOnly;

import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;

/**
 * filter_contains_name
 */
public class FilterContainsName extends Command {

    private String name;
    @Override
    public Object execute() {
        var list = collection.filterContainsName(name);
        if (list.size() == 0) return"Nothing matches pattern";
        return list;
    }

    @Override
    public void setArgs(Reader from) {
        name = from.readString();
    }

    @Override
    public String toString() {
        String res = "filter_contains_name";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
