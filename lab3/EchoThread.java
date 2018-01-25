package lab3;

import java.io.*;
import java.net.*;

public class EchoThread extends Thread {

  private Socket socket;

  public EchoThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      System.out.println("Connected: " + socket.getInetAddress());
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      OutputStream out = socket.getOutputStream();

      while (!socket.isClosed()) {
        String message = in.readLine();
        if (message == null) {
          System.out.println("Disconnected: " + socket.getInetAddress());
          break;
        }
        System.out.println("Client: " + message);
        String response = message.toUpperCase() + '\n';
        out.write(response.getBytes());
      }

      socket.close();
      in.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
