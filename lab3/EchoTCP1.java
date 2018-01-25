package lab3;

import java.io.*;
import java.net.*;

public class EchoTCP1 {
  public static void main(String argv[]) throws Exception {
    int port = Integer.parseInt(argv[0]);
    ServerSocket serverSocket = new ServerSocket(port);

    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println("Connected: " + socket.getInetAddress());
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      OutputStream out = socket.getOutputStream();

      while (!socket.isClosed()) {
        String message = in.readLine();
        if (message == null) {
          System.out.println("Disconnected:" + socket.getInetAddress());
          break;
        }
        System.out.println("Client: " + message);
        String response = message.toUpperCase() + '\n';
        out.write(response.getBytes());
			}

			socket.close();
			in.close();
    }
  }
}