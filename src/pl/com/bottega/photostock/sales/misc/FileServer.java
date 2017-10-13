package pl.com.bottega.photostock.sales.misc;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServer {
    static final Logger LOGGER = Logger.getLogger("server");


    public static void main(String[] args) throws Exception {
        LOGGER.setLevel(Level.WARNING);
        new FileServer().work();

    }

    private List<Client> clients = new Vector<>();
    BlockingQueue<Socket> sockets = new LinkedBlockingDeque<>(5);

    public void work() throws Exception {
        ServerSocket serverSocket = new ServerSocket(6662);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.info(String.format("Client socket %s created", clientSocket.toString()));
            sockets.offer(clientSocket);
            Client client = new Client(this, clientSocket.getInputStream(), clientSocket.getOutputStream());
            clients.add(client);
            LOGGER.info(String.format("Current number of clients: %d", clients.size()));
            client.run();
        }

    }

    static class Client implements Runnable {

        private final FileServer server;
        private final PrintWriter printWriter;
        private BufferedReader bufferedReader;
        private File file;
        private FileInputStream fileInputStream;
        private OutputStream outputStream;

        public void setFileInputStream(String path) throws FileNotFoundException {
            this.fileInputStream = new FileInputStream(path);
        }

        public Client(FileServer server, InputStream inputStream, OutputStream outputStream) throws IOException {
            this.server = server;
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.printWriter = new PrintWriter(outputStream);
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            try {
                String request = bufferedReader.readLine();
                LOGGER.info(String.format("Clients request: %s", request));
                if (request == null) {
                    server.clientDisconnected(this);
                } else {
                    String path = request.substring(4).trim();
                    file = new File(path);
                    setFileInputStream(path);
                    if (!file.exists()) {
                        sendResponse("ERROR No such file");
                        server.clientDisconnected(this);
                        LOGGER.info("Client disconnected");
                        return;
                    }
                    if (file.isDirectory()) {
                        sendResponse("ERROR File is a directory");
                        server.clientDisconnected(this);
                        return;
                    }
                    printWriter.println("OK");
                    printWriter.flush();
                    sendFile();
                    outputStream.close();
                }

            } catch (IOException e) {
                server.clientDisconnected(this);
                sendResponse("ERROR No such file");
            }
        }

        private void sendFile() throws IOException {
            byte[] buffer = new byte[1024];
            int part;
            while ((part = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, part);
            }

            byte[] bytes = fileInputStream.readAllBytes();
            outputStream.write(bytes);
            server.clientDisconnected(this);

            LOGGER.info(String.format("File %s sended", fileInputStream.getFD().toString()));
        }

        private void sendResponse(String response) {
            printWriter.println(response);
            printWriter.flush();
        }
    }

    private void clientDisconnected(Client client) {
        this.clients.remove(client);
    }


}
