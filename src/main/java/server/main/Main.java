package server.main;

import client.connection.ConnectToServer;
import common.StoredClasses.HumanBeing;
import common.commands.abstraction.Command;
import common.commands.concreteCommands.serverOnly.Info;
import server.connection.ConnectToClient;
import server.launcher.CommandsLauncher;
import server.parse.ParseXml;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.TreeSet;

public class Main {
    public static ParseXml XMLInput = ParseXml.getXMLInput("input.xml");

    public static void main(String []args){
        var server = new ConnectToClient(2223);
        var set = new TreeSet<HumanBeing>();
        CommandsLauncher<HumanBeing> collection = new CommandsLauncher<>(set);
        boolean work = true;
        while (work){
            Command command = (Command) server.getCommand();
            command.setCollection(collection);
            Object result = command.execute();
            server.send(result);
        }

    }
}
