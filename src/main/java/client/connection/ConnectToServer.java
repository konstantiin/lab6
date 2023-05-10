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

    public void sentCommand(Command command) throws IOException {
        var b = new ByteArrayOutputStream();
        try (ObjectOutputStream ObjOut = new ObjectOutputStream(b)) {
            ObjOut.writeObject(command);
            var obj = b.toByteArray();
            byte[] size = new byte[4];
            ByteBuffer.wrap(size).putInt(obj.length);

            channel.send(ByteBuffer.wrap(size), socket);
            ByteBuffer data = ByteBuffer.wrap(obj);
            channel.send(data, socket);
        } catch (IOException e) {
            if (ifReload()) {
                this.sentCommand(command);
            } else throw e;
        }

    }
}
