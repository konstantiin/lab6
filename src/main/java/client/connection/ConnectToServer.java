package client.connection;

import common.commands.abstraction.Command;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectToServer {

    private final InetSocketAddress socket;
    private final DatagramChannel channel;

    public ConnectToServer(InetAddress host, int port){
        socket = new InetSocketAddress(host, port);
        try {
            channel = DatagramChannel.open();
        } catch (IOException e) {
            throw new RuntimeException(e);// обработать
        }
    }
    public Object getResponse(){
        byte[] data;
        try {
            var len = ByteBuffer.wrap(new byte[4]);
            channel.receive(len);
            len.flip();
            int size = len.getInt();
            data = new byte[size];
            channel.receive(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))){
            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  void sentCommand(Command command){
        var b = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjOut = new ObjectOutputStream(b)){
            ObjOut.writeObject(command);
            var obj = b.toByteArray();
            byte[] size = new byte[4];
            ByteBuffer.wrap(size).putInt(obj.length);

            System.out.println(obj.length);
            channel.send(ByteBuffer.wrap(size), socket);
            ByteBuffer data = ByteBuffer.wrap(obj);
            channel.send(data, socket);
        } catch (IOException e) {
            throw new RuntimeException(e);// надо обработать
        }

    }
}
