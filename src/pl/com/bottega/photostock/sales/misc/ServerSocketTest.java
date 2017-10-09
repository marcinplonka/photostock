package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket socket = ss.accept();
            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while (!(line = br.readLine()).trim().equals("")) {
                        System.out.println(line);
                    }
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);
                    pw.println("HTTP1.1 200 OK");
                    pw.println("Content-Type: text/html, charset=utf-8");
                    pw.println("\r\n");
                    pw.println("Hello World");
                    pw.println("Zażółć gęślą jaźń");
                    pw.flush();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }
}
