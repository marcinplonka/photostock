package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj imię: ");
        String name = scanner.nextLine();
        final int SERVER_PORT = 6661;
        String HOST_NAME = "localhost";
        Socket socket = new Socket(HOST_NAME, SERVER_PORT);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        printWriter.println(name);
        printWriter.flush();


        new Thread(() -> {
            String message;
            try {
                while (true) {
                    message = bufferedReader.readLine();
                    if (message != null) {
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String message;
        while (true) {
            message = scanner.nextLine();
            printWriter.println(message);
            printWriter.flush();
        }

    }
}
