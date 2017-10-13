package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileClient {
    private static final Logger LOGGER = Logger.getLogger("client");

    public static void main(String[] args) throws IOException, InterruptedException {
        LOGGER.setLevel(Level.WARNING);
        Scanner scanner = new Scanner(System.in);
        final int SERVER_PORT = 6662;
        String HOST_NAME = "localhost";
        String localPath = "/home/marcin/Downloads/";
        Socket socket = new Socket(HOST_NAME, SERVER_PORT);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        System.out.println("Podaj ścieżkę do pliku na serwerze: ");
        String remotePath = scanner.nextLine();
        printWriter.println("GET " + remotePath + "\n");
        printWriter.flush();
        LOGGER.info("Request send");
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String serverResponse = bufferedReader.readLine();
        System.out.println(String.format("Server response: %s", serverResponse));

        if (serverResponse.equals("ERROR No such file"))
            return;

        if (serverResponse.equals("ERROR File is a directory"))
            return;

        System.out.println("Podaj nazwę pliku docelowego: ");
        String localFileName = scanner.nextLine();
        OutputStream outputStream = new FileOutputStream(localPath + localFileName);
        LOGGER.info("Client socket created");


        if (serverResponse.trim().equals("OK")) {
            byte[] buffer = new byte[1024];
            int part;
            while ((part = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, part);
                System.out.println("0");
            }
            LOGGER.info(String.format("File %s transferred", localFileName));
        } else {
            System.out.println(String.format("Server response: %s", serverResponse));
        }
    }

}
