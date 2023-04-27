package client;


import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;
import server.parse.ParseXml;

import java.util.concurrent.TimeUnit;


public class App {
    public static ParseXml XMLInput;

    /**
     * gets path to input/output file
     * from the environment variable
     *
     * @return xml file path
     */
    static String getPath() {
        return System.getenv("LAB5");
    }


    /**
     * main method
     * creates managed collection, parses xml file and execute common.commands from System.in
     */
    public static void main(String[] args) throws InterruptedException {

        try {
            while (console.hasNext()) {
                Command met = null;
                try {
                    met = console.readCommand();
                } catch (UnknownCommandException e) {
                    System.out.println("Command not found, type \"help\" for more info");
                } catch (InputException e){
                    console.renewScan(System.in);
                }
                if (met != null) met.execute();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            TimeUnit.SECONDS.sleep(60); // для дебага
        }
    }
}