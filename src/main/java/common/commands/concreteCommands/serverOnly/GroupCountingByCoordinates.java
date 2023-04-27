package common.commands.concreteCommands.serverOnly;


import common.StoredClasses.Coordinates;
import common.commands.abstraction.Command;
import client.reading.readers.Reader;

import static common.commands.launcher.CommandsLauncher.currentScripts;

/**
 * group_counting_by_coordinates
 */
public class GroupCountingByCoordinates extends Command {

    @Override
    public Object execute() {
        return collection.groupCountingByCoordinates();
    }

    @Override
    public void setArgs(Reader from) {

    }

    @Override
    public String toString() {
        String res = "group_counting_by_coordinates";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}