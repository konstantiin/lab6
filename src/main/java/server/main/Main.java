package server.main;



import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.connection.ObjectByteArrays;
import server.connection.ConnectToClient;
import server.launcher.CommandsLauncher;
import server.parse.ParseXml;

import java.io.IOException;

import java.io.Serializable;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Main {
    public static final ParseXml XMLInput = ParseXml.getXMLInput("input.xml");
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int port = Integer.parseInt(args[0]);
        var server = new ConnectToClient(port);
        System.out.println("Server is running. Port " + port);
        logger.info("Server is running. Port " + port);
        var set = new TreeSet<>(XMLInput.getArr());
        CommandsLauncher<HumanBeing> collection = new CommandsLauncher<>(set);
        boolean work = true;
        Scanner console = new Scanner(System.in);
        while (work) {
            try{
                ConnectToClient.readAll(server);
                var input = server.getInputObjects();
                input.forEach((key, value) ->{
                    Command c = (Command) value.toObject();
                    c.setCollection(collection);
                    server.setAnswer(key, ObjectByteArrays.getArrays((Serializable) c.execute()));
                });
                ConnectToClient.writeAll(server);

                int b = System.in.available();
                if (b > 0) {
                    logger.info("Server command was found.");
                    work = collection.runServerCommand(console.next().trim().toLowerCase());
                    logger.info("Command compiled");
                }

            } catch (IOException e) {
                logger.error("Unknown error" + e.getMessage());
                throw new RuntimeException(e);

            }

        }
        // close
    }
}
