package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;


/**
 * add command
 */
public class Add extends Command {


    public Add(Reader Reader) {
        super(Reader);
    }

    @Override
    public void execute() {
        collection.add(input.readObject());
        System.out.println("element added");
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
