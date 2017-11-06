package pl.com.bottega.photostock.sales.misc;

import java.io.*;
import java.net.Socket;

public class SocketTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("www.pollub.pl",80);
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("GET / HTTP1.1");
        pw.println("Host: www.pollub.pl");
        pw.print("\r\n");
        pw.flush();
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);

        }
    }
}
