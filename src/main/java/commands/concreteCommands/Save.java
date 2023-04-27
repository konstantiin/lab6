package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * save command
 */
public class Save extends Command {
    public Save(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        collection.save();
        System.out.println("Collection was saved");
    }

    @Override
    public String toString() {
        String res = "save";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
