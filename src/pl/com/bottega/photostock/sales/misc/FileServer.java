package pl.com.bottega.photostock.sales.misc;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

    private BlockingQueue<Socket> sockets = new LinkedBlockingDeque<>(5);

    public void work() throws Exception {
        ServerSocket serverSocket = new ServerSocket(6662);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.info(String.format("Client socket %s created", clientSocket.toString()));
            sockets.offer(clientSocket);
            Client client = new Client(clientSocket.getInputStream(), clientSocket.getOutputStream());
            new Thread(client).start();
        }

    }

    static class Client implements Runnable {

        private final PrintWriter printWriter;
        private InputStreamReader inputStreamReader;
        private File file;
        private FileInputStream fileInputStream;
        private OutputStream outputStream;

        public void setFileInputStream(String path) throws FileNotFoundException {
            this.fileInputStream = new FileInputStream(path);
        }

        public Client(InputStream inputStream, OutputStream outputStream) throws IOException {
            this.inputStreamReader = new InputStreamReader(inputStream);
            this.printWriter = new PrintWriter(outputStream);
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            try {
                String request = getCommandFromClient(inputStreamReader);
                LOGGER.info(String.format("Clients request: %s", request));
                String path = request.substring(4).trim();
                file = new File(path);
                if (!file.exists() && !file.isDirectory()) {
                    sendResponse("ERROR No such file\n");
                    return;
                }
                if (file.isDirectory()) {
                    sendResponse("ERROR File is a directory\n");
                    return;
                }
                setFileInputStream(path);
                printWriter.print("OK\n");
                printWriter.flush();
                sendFile();
            } catch (IOException e) {
                sendResponse("ERROR No such file\n");
            }
        }

        private static String getCommandFromClient(InputStreamReader inputStream) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
                char c;
                do {
                    c = (char) inputStream.read();
                    stringBuilder.append(c);
                } while (c != '\n');

            return stringBuilder.toString();
        }


        private void sendFile() throws IOException {
            byte[] buffer = new byte[1024];
            int part;
            while ((part = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, part);
            }
            outputStream.flush();
            outputStream.close();

            LOGGER.info(String.format("File %s sended", fileInputStream.getFD().toString()));
        }

        private void sendResponse(String response) {
            printWriter.print(response);
            printWriter.flush();
            printWriter.close();
        }

    }


}
