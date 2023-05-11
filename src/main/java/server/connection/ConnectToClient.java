package server.connection;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.util.Set;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class ConnectToClient {
    private final DatagramChannel channel;
    private final Selector selector;
    private SocketAddress client;

    public ConnectToClient(int port) {
        SocketAddress addr = new InetSocketAddress(port);
        try {
            channel = DatagramChannel.open();
            channel.bind(addr);
            selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<SelectionKey> getKeys() {
        try {
            selector.select();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return selector.selectedKeys();
    }
    private byte[] receive(int size) throws IOException {
        var data = new byte[size];
        int packSize = 100;
        int i = 0;
        while (i < size){
            var j = min(i + packSize, size);
            var length = j - i;


            var dataToReceive = new byte[length];
            channel.receive(ByteBuffer.wrap(dataToReceive));
            System.arraycopy(dataToReceive, 0, data, i,length);
            i = j;

        }
        return data;
    }

    public Object getCommand() {
        int size;
        try {
            var len = ByteBuffer.wrap(new byte[4]);
            client = channel.receive(len);

            len.flip();
            size = len.getInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data;
        try {
            data = receive(size);
            client = channel.receive(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))) {

            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Object result) {
        var b = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjOut = new ObjectOutputStream(b)) {
            ObjOut.writeObject(result);
            var obj = b.toByteArray();
            byte[] size = new byte[4];

            channel.send(ByteBuffer.wrap(size).putInt(obj.length).flip(), client);
            ByteBuffer data = ByteBuffer.wrap(obj);
            channel.send(data, client);
        } catch (IOException e) {
            throw new RuntimeException(e);// надо обработать
        }
    }

}
