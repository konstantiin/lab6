package client.connection;

import common.commands.abstraction.Command;
import common.connection.ObjectByteArrays;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class ConnectToServer { // close scanner
    private int timeOutSec = 10;
    private static boolean isOk = true;

    private final InetSocketAddress socket;
    private final DatagramChannel channel;

    private ConnectToServer(InetAddress host, int port) throws IOException {
        socket = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
    }

    private static boolean ifReload() {
        if (!isOk) return false;
        System.out.println("Server error. Retry?(y/n)");
        var s = new Scanner(System.in);
        isOk = s.next().charAt(0) == 'y';
        return isOk;
    }

    public static ConnectToServer getServer(String hostName, int port) {
        boolean needConnect = true;
        ConnectToServer server = null;
        while (needConnect) {
            try {
                InetAddress host;
                if (Objects.equals(hostName, "localhost")) {
                    host = InetAddress.getLocalHost();
                } else {
                    host = InetAddress.getByName(hostName);
                }
                server = new ConnectToServer(host, port);
                needConnect = false;
            } catch (IOException e) {
                needConnect = ifReload();
            }
        }
        return server;
    }

    private void receive(ByteBuffer buf) {
        try {
            SocketAddress res = null;
            var cur = LocalDateTime.now();
            while (res == null) {
                if (LocalDateTime.now().getSecond() - cur.getSecond() > timeOutSec) {// странно
                    throw new IOException();
                }
                res = channel.receive(buf);
            }
            confirm();
        } catch (IOException e) {
            if (ifReload()) {
                this.receive(buf);
            } else {
                throw new RuntimeException(e);
            }
        }

    }
    private ObjectByteArrays receiveArrays(ObjectByteArrays data){
        var cur = data.getNext();
        while (cur != null){
            receive(ByteBuffer.wrap(cur));
            cur = data.getNext();
        }
        return data;
    }

    public Object getResponse() {
        var len = ByteBuffer.wrap(new byte[4]);
        this.receive(len);
        len.flip();
        int size = len.getInt();
        var data = ObjectByteArrays.getEmpty(size);
        data.getNext();// shift pointer
        return this.receiveArrays(data).toObject();
    }
    private boolean isConfirmed() {
        byte[] arr = new byte[1];
        var cur = LocalDateTime.now();
        while (LocalDateTime.now().getSecond() - cur.getSecond() < timeOutSec) {
            try {
                var from = channel.receive(ByteBuffer.wrap(arr));
                if (!socket.equals(from)) throw new IOException();
                return arr[0] == 1;
            } catch (IOException ignored) {}
        }
        return false;
    }
    private void confirm(){
        sendArr(new byte[]{1});
    }

    private void sendArr(byte[] d){
        try{
            channel.send(ByteBuffer.wrap(d), socket);
        } catch (IOException e){
            if (ifReload()) {
                sendArr(d);
            }
            throw new RuntimeException(e);
        }
    }
    private void sendArrays(ObjectByteArrays data) {
            byte[] next = data.getNext();
            while (next.length != 0) {
                sendArr(next);
                if (!isConfirmed()){
                    if (!ifReload()) throw new RuntimeException(new IOException());
                } else {
                    next = data.getNext();
                }
            }
    }
    public void sendCommand(Command command){
        this.sendArrays(ObjectByteArrays.getArrays(command));
    }
}
