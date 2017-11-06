package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ChatServer {

    public static void main(String[] args) throws Exception {
        new ChatServer().work();
    }

    private List<Client> clients = new Vector<>();

    public void work() throws Exception {
        ServerSocket serverSocket = new ServerSocket(6661);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected " + clientSocket.getInetAddress());
            Client client = new Client(this, clientSocket.getInputStream(), clientSocket.getOutputStream());
            clients.add(client);
            new Thread(client).start();
        }
    }

    static class Client implements Runnable {

        private final ChatServer server;
        private final PrintWriter printWriter;
        private BufferedReader bufferedReader;
        private String name;

        public Client(ChatServer server, InputStream inputStream, OutputStream outputStream) {
            this.server = server;
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.printWriter = new PrintWriter(outputStream);
        }

        @Override
        public void run() {
            try {
                name = bufferedReader.readLine();
                if (name == null) {
                    server.clientDisconnected(this);
                    return;
                }
                System.out.println("Client name is " + name);
                while (true) {
                    String msg = bufferedReader.readLine();
                    if(msg == null) {
                        server.clientDisconnected(this);
                        return;
                    }
                    System.out.println("Client " + name + " sent message " + msg);
                    server.newMessage(msg, this);
                }
            } catch (IOException e) {
                server.clientDisconnected(this);
            }
        }

        public void sendMessage(String msg, Client author) {
            try {
                printWriter.println(author.name + "> " + msg);
                printWriter.flush();
            } catch (Exception ex) {

            }
        }
    }

    private void clientDisconnected(Client client) {
        System.out.println("Client " + client.name + " disconnected");
        this.clients.remove(client);
    }

    private void newMessage(String msg, Client author) {
        for (Client otherClient : clients) {
            if (author != otherClient) {
                otherClient.sendMessage(msg, author);
            }
        }
    }

}
