package client.connection;

import common.commands.abstraction.Command;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
        boolean wait = s.next().charAt(0) == 'y';
        s.close();
        return wait;
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

    private void receive(ByteBuffer buf, int tries) throws IOException {
        try {
            var res = channel.receive(buf);
            if (res == null) {
                if (tries > 5) {
                    throw new IOException();
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                receive(buf, tries + 1);
            }
        } catch (IOException e) {
            if (ifReload()) this.receive(buf, tries + 1);
            else {
                throw e;
            }
        }

    }

    public Object getResponse() throws IOException, ClassNotFoundException {
        byte[] data;

        var len = ByteBuffer.wrap(new byte[4]);
        this.receive(len, 0);
        len.flip();
        int size = len.getInt();
        data = new byte[size];
        this.receive(ByteBuffer.wrap(data), 0);
        try (ObjectInput ObjIn = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ObjIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            if (ifReload()) return this.getResponse();
            else throw e;
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
