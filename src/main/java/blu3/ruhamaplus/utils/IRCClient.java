package blu3.ruhamaplus.utils;

import java.io.*;
import java.net.*;


public abstract class IRCClient implements Runnable {

    private String host;
    private int port;
    private String username;

    private boolean connected;
    private boolean active;

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public IRCClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public BufferedReader getIn() {
        return in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public boolean isActive() {
        return active;
    }

    public void start() throws UnknownHostException, IOException {
        this.socket = new Socket(getHost(), getPort());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if (socket.isConnected()) {
            this.active = true;
            new Thread(this).start();
            send("NICK " + getUsername());
            send("USER " + getUsername() + " 127.0.0.1 localhost :" + getUsername());
        }
    }

    public void run() {
        String buffer;
        while (isActive()) {
            try {
                while ((buffer = in.readLine()) != null) {
                    listener(buffer);
                    if (buffer.startsWith("PING")) {
                        send("PONG " + buffer.substring(5));
                        if (connected == false) {
                            connected = true;
                            onConnect();
                        }
                    }
                }
            } catch (IOException e) {
                exception(e);
            }
        }
    }

    public abstract void listener(String line);

    public abstract void onConnect();

    public abstract void exception(Exception e);

    public void send(String line) throws IOException {
        out.write(line + "\r\n");
        out.flush();
    }

    public void sendMessage(String channel, String message) throws IOException {
        send("PRIVMSG " + channel + " :" + message.replace("<br>", " "));
    }

    public void joinChannel(String channel) throws IOException {
        send("JOIN " + channel);
    }

    public void quit(String reason) throws IOException {
        send("QUIT (" + reason + ")");
        this.active = false;
        connected = false;

    }

    public String getIP() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        String ip = localhost.getHostAddress();
        return (ip);
    }

}
