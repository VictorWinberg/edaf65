package lab3;

import java.io.*;
import java.net.*;

public class ChatClient {
  public static void main(String argv[]) throws Exception {
    String machine = argv[0];
    int port = Integer.parseInt(argv[1]);
    BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
    Socket socket = new Socket(machine, port);
    OutputStream out = socket.getOutputStream();
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    while (!socket.isClosed()) {
      String input = scanner.readLine();
      out.write((input + '\n').getBytes());
      String response = in.readLine();
      System.out.println("Server: " + response);
    }

    socket.close();
    in.close();
  }
}