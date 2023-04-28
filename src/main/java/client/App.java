package client;


import client.connection.ConnectToServer;
import client.reading.objectTree.Node;
import client.reading.readers.OnlineReader;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


public class App {

    /**
     * main method
     * creates managed collection, parses xml file and execute common.commands from System.in
     */
    public static void main(String[] args) throws InterruptedException {
        ConnectToServer server;
        String host = "local";
        int port = 2223;
        try {
            server = new ConnectToServer(InetAddress.getLocalHost(), 2223);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


        OnlineReader console = new OnlineReader(System.in, Node.generateTree(HumanBeing.class, "HumanBeing"));
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
                if (met != null){
                    if (met.ifSend()){
                        server.sentCommand(met);
                        System.out.println(server.getResponse());
                    } else {
                        met.execute();
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            TimeUnit.SECONDS.sleep(60); // для дебага
        }
    }
}