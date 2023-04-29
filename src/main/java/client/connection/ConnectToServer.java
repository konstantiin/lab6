package client.connection;

import common.commands.abstraction.Command;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class ConnectToServer {

    private final InetSocketAddress socket;
    private final DatagramChannel channel;

    public ConnectToServer(InetAddress host, int port) throws IOException {
        socket = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
    }
    private boolean ifReload(){

        System.out.println("Server error. Retry?(y/n)");
        var s = new Scanner(System.in);
        boolean wait = s.next().charAt(0) == 'y';
        s.close();
        return wait;
    }
    public Object getResponse() throws IOException, ClassNotFoundException {
        byte[] data;
        try {
            var len = ByteBuffer.wrap(new byte[4]);
            channel.receive(len);
            len.flip();
            int size = len.getInt();
            data = new byte[size];
            channel.receive(ByteBuffer.wrap(data));
        } catch (IOException e) {

            if (ifReload()) return this.getResponse();
            else{
                throw e;
            }
        }
        try(ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))){
            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            if (ifReload()) return this.getResponse();
            else throw e;
        }
    }
    public void sentCommand(Command command) throws IOException {
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
            if (ifReload()) {
                this.sentCommand(command);
            }
            else throw e;
        }

    }
}
