package client;


import client.connection.ConnectToServer;
import client.reading.objectTree.Node;
import client.reading.readers.OnlineReader;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;

import java.io.IOException;
import java.net.UnknownHostException;


public class App {

    /**
     * main method
     * creates managed collection, parses xml file and execute commands from System.in
     */
    public static void main(String[] args) throws InterruptedException, UnknownHostException {

        int port = Integer.parseInt(args[1]);
        ConnectToServer server = ConnectToServer.getServer(args[0], port);
        if (server == null) {
            System.out.println("Connection failed.");
            return;
        }

        OnlineReader console = new OnlineReader(System.in, Node.generateTree(HumanBeing.class, "HumanBeing"));
        while (console.hasNext()) {
            Command met = null;
            try {
                met = console.readCommand();
            } catch (UnknownCommandException e) {
                System.out.println("Command not found, type \"help\" for more info");
            } catch (InputException e) {
                console.renewScan(System.in);
            }
            if (met != null) {
                if (met.ifSend()) {
                    try {
                        server.sentCommand(met);
                        System.out.println(server.getResponse());
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Execution ended");
                        console.closeStream();
                    }

                } else {
                    met.execute();
                }
            }
        }

    }
}