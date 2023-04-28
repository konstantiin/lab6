package server.connection;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ConnectToClient {
    private DatagramChannel server = null;
    private SocketAddress addr, client;
    public ConnectToClient(int port){
        addr = new InetSocketAddress(2223);
        try {
            server = DatagramChannel.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            server.bind(addr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Object getCommand() {
        int size = 0;
        try {
            var len = ByteBuffer.wrap(new byte[4]);
            client = server.receive(len);

            len.flip();
            size = len.getInt();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        byte[] data = new byte[size];
        try {
            client = server.receive(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))){

            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(Object result ){
        var b = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjOut = new ObjectOutputStream(b)){
            ObjOut.writeObject(result);
            var obj = b.toByteArray();
            byte[] size = new byte[4];

            server.send(ByteBuffer.wrap(size).putInt(obj.length).flip(), client);
            ByteBuffer data = ByteBuffer.wrap(obj);
            server.send(data, client);
        } catch (IOException e) {
            throw new RuntimeException(e);// надо обработать
        }
    }

}
