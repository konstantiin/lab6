package commands.concreteCommands;

import commands.abstraction.Command;
import exceptions.inputExceptions.IdException;
import reading.readers.Reader;

import java.math.BigInteger;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * remove_by_id command
 */
public class RemoveById extends Command {

    public RemoveById(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        try {
            collection.removeById(input.readInt(BigInteger.ZERO, BigInteger.valueOf(Long.MAX_VALUE)).longValue());
            System.out.println("Element removed");
        } catch (IdException e) {
            System.out.println("Element with this id does not exist");
        }
    }

    @Override
    public String toString() {
        String res = "remove_by_id";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
