package commands.concreteCommands;


import commands.abstraction.Command;
import reading.readers.Reader;

import static commands.launcher.CommandsLauncher.currentScripts;

/**
 * sum_of_impact_speed command
 */
public class SumOfImpactSpeed extends Command {
    public SumOfImpactSpeed(Reader reader) {
        super(reader);
    }

    @Override
    public void execute() {
        System.out.println(collection.sumOfImpactSpeed());
    }

    @Override
    public String toString() {
        String res = "sum_of_impact_speed";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
