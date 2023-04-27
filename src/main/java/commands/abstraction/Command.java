package commands.abstraction;

import commands.launcher.CommandsLauncher;
import reading.readers.Reader;

/**
 * Abstract command class
 */
public abstract class Command {
    protected final CommandsLauncher<?> collection;
    protected final Reader input;

    /**
     * @param reader - reader, from which command will get data
     */
    public Command(Reader reader) {
        collection = reader.getCollection();
        input = reader;
    }

    /**
     * default constructor, initialize fields as null
     */
    public Command() {
        collection = null;
        input = null;
    }

    /**
     * executes command
     */
    public abstract void execute();

    /**
     * @return command name
     */
    @Override
    public abstract String toString();
}
