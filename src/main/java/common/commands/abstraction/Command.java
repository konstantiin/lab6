package common.commands.abstraction;

import server.launcher.CommandsLauncher;
import client.reading.readers.Reader;

import java.io.Serializable;

/**
 * Abstract command class
 */
public abstract class Command implements Serializable {
    protected CommandsLauncher<?> collection;
    protected boolean send = true;
    /**
     * default constructor, initialize fields as null
     */
    public boolean ifSend(){ return  send;}
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
