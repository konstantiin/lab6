package commands.concreteCommands;

import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * exit
 */
public class Exit extends Command {
    public Exit(Reader input) {
        super(input);
    }

    @Override
    public void execute() {
        input.closeStream();
        System.out.print("Exit ");
        if (currentScripts.size() != 0)
            System.out.print("from " + currentScripts.get(currentScripts.size() - 1) + " scrip");
        System.out.println("complete");
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
