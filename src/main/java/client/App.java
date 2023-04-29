package client;


import client.connection.ConnectToServer;
import client.reading.objectTree.Node;
import client.reading.readers.OnlineReader;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class App {

    /**
     * main method
     * creates managed collection, parses xml file and execute common.commands from System.in
     */
    public static int main(String[] args) throws InterruptedException {
        ConnectToServer server = null;
        String host = "helios.cs.ifmo.ru";
        int port = 2223;
        boolean needConnect = true;
        while(needConnect) {
            try {
                server = new ConnectToServer(InetAddress.getByName("helios.cs.ifmo.ru"), port);
                needConnect = false;
            } catch (IOException e) {
                System.out.println("Connection error. (Retry?(y/n)");
                var s = new Scanner(System.in);
                needConnect= s.next().charAt(0) == 'y';
                s.close();
            }
        }
        if (server == null){
            System.out.println("Connection failed.");
            return 0;
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
        return 0;
    }
}