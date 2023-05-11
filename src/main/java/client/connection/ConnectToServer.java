package client.connection;

import common.commands.abstraction.Command;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class ConnectToServer {

    private final InetSocketAddress socket;
    private final DatagramChannel channel;

    private ConnectToServer(InetAddress host, int port) throws IOException {
        socket = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
    }

    private static boolean ifReload() {

        System.out.println("Server error. Retry?(y/n)");
        var s = new Scanner(System.in);

        return s.next().charAt(0) == 'y';
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
    private void send(byte[] obj) throws IOException {
        int packSize = 100;
        int i = 0;
        while (i < obj.length){
            var j = min(i + packSize, obj.length);
            var length = j - i;


            var dataToSend = new byte[length];
            System.arraycopy(obj, i, dataToSend, 0,length);
            channel.send(ByteBuffer.wrap(dataToSend), socket);

            i = j;

        }

    }

    private void receive(ByteBuffer buf) {
        try {
            SocketAddress res = null;
            var cur = LocalDateTime.now();
            while (res == null) {
                if (LocalDateTime.now().getSecond() - cur.getSecond() > 10) {
                    throw new IOException();
                }
                res = channel.receive(buf);

            }
        } catch (IOException e) {
            if (ifReload()) {
                this.receive(buf);
            } else {
                throw new RuntimeException(e);
            }
        }

    }

    public Object getResponse() {
        byte[] data;

        var len = ByteBuffer.wrap(new byte[4]);
        this.receive(len);
        len.flip();
        int size = len.getInt();
        data = new byte[size];
        this.receive(ByteBuffer.wrap(data));
        try (ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            if (ifReload()) return this.getResponse();
            else throw new RuntimeException(e);
        }
    }
    private boolean confirm(){
        byte[] buf = new byte[1];
        try {
            if (channel.receive(ByteBuffer.wrap(buf)) != null)
        } catch (IOException e) {
            return
        }
    }

    public void sendCommand(Command command) throws IOException {
        var b = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjOut = new ObjectOutputStream(b)) {
            ObjOut.writeObject(command);
            var obj = b.toByteArray();
            byte[] size = new byte[4];
            ByteBuffer.wrap(size).putInt(obj.length);

            channel.send(ByteBuffer.wrap(size), socket);
            ByteBuffer data = ByteBuffer.wrap(obj);
            this.send(obj);
        } catch (IOException e) {
            if (ifReload()) {
                this.sendCommand(command);
            } else throw e;
        }

    }
}
