package common.commands.abstraction;

import common.commands.launcher.CommandsLauncher;
import client.reading.readers.Reader;

/**
 * Abstract command class
 */
public abstract class Command {
    protected CommandsLauncher<?> collection;
    /**
     * default constructor, initialize fields as null
     */
    public Command() {
    }
    public void setCollection(CommandsLauncher<?> collect){collection=collect;}


    /**
     * executes command
     */
    public abstract Object execute();

    /**
     * @return command name
     */
    public abstract void setArgs(Reader from);
    @Override
    public abstract String toString();
}
