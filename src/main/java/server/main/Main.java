package server.main;


import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import server.connection.ConnectToClient;
import server.launcher.CommandsLauncher;
import server.parse.ParseXml;

import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    public static ParseXml XMLInput = ParseXml.getXMLInput("input.xml");

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        var server = new ConnectToClient(port);
        System.out.println("Server is running. Port " + port);
        var set = new TreeSet<>(XMLInput.getArr());
        CommandsLauncher<HumanBeing> collection = new CommandsLauncher<>(set);
        boolean work = true;
        Scanner console = new Scanner(System.in);
        while (work) {
            var keys = server.getKeys();

            for (var x : keys) {
                if (x.isValid()) {
                    Command command = (Command) server.getCommand();
                    command.setCollection(collection);
                    Object result = command.execute();
                    server.send(result);
                }
            }

            try {
                int b = System.in.available();
                if (b > 0) {
                    work = collection.runServerCommand(console.next().trim().toLowerCase());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
