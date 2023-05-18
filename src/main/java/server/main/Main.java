package server.main;


import ch.qos.logback.core.read.ListAppender;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import server.connection.ConnectToClient;
import server.launcher.CommandsLauncher;
import server.parse.ParseXml;

import java.io.IOException;

import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Main {
    public static final ParseXml XMLInput = ParseXml.getXMLInput("input.xml");
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        var server = new ConnectToClient(port);
        System.out.println("Server is running. Port " + port);
        logger.info("Server is running. Port " + port);
        var set = new TreeSet<>(XMLInput.getArr());
        CommandsLauncher<HumanBeing> collection = new CommandsLauncher<>(set);
        boolean work = true;
        Scanner console = new Scanner(System.in);
        while (work) {
            var keys = server.getKeys();
            for (var iter = keys.iterator(); iter.hasNext();) {
                var key = iter.next();
                iter.remove();
                if (key.isValid()) {
                    logger.info("Found input data from" + key);// хрень
                    try{
                        server.read(key);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            List<Object> input = server.getInputObjects();
            for (Object obj: input){
                Command c = (Command) obj;
                System.out.println(c);//execute command
            }

            try {
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

    }
}
