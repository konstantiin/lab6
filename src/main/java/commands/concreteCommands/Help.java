package commands.concreteCommands;

import commands.abstraction.Command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * help command
 */
public class Help extends Command {
    @Override
    public void execute() {
        try (InputStream inputStream = getClass().getResourceAsStream("/help.txt")) {
            assert inputStream != null;
            Scanner help = new Scanner(inputStream);
            while (help.hasNextLine()) System.out.println(help.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String res = "help";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
