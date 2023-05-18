package server.connection;

import common.connection.ObjectByteArrays;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class ConnectToClient {
    private final int timeOutSec = 10;
    private final DatagramChannel channel;
    private final Selector selector;


    private final Map<SocketAddress, ObjectByteArrays> currentInput = new HashMap<>();
    private final List<SocketAddress> complete = new ArrayList<>();
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
    public List<Object> getInputObjects(){
        List<Object> res = new ArrayList<>();
        for (var i: complete){
            res.add(currentInput.get(i).toObject());
            currentInput.remove(i);
        }
        if (res.size() != complete.size()) throw new RuntimeException(complete.toString());
        complete.clear();
        return res;
    }
    public Set<SelectionKey> getKeys() {
        try {
            selector.selectNow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return selector.selectedKeys();
    }
    private void confirm(SocketAddress client){

        try {
            channel.send(ByteBuffer.wrap(new byte[]{1}), client);
        } catch (IOException e) {
            return;//хз что делать
        }
    }
    public void read(SelectionKey key){
        var datagramChannel = (DatagramChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(256);
        SocketAddress client;
        try {
            client = datagramChannel.receive(buf);
        } catch (IOException e) {
            return;// мб надо что-то сделать, но я хз что и надо ли
        }
        buf.flip();
        byte[] in = new byte[buf.remaining()];
        buf.get(in);
        if(currentInput.containsKey(client)){
            boolean ifReady = currentInput.get(client).addNext(in);
            if (ifReady) complete.add(client);
        } else{
            currentInput.put(client, ObjectByteArrays.getEmpty(ByteBuffer.wrap(in).getInt()));

        }
        try {
            datagramChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        }
        confirm(client);
    }
}
