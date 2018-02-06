package lab3.echo;

import java.io.*;
import java.net.*;

public class EchoTCP2 {
  public static void main(String args[]) throws IOException {
    int port = Integer.parseInt(args[0]);
    ServerSocket serverSocket = new ServerSocket(port);

    while (true) {
      Socket socket = serverSocket.accept();
      EchoThread thread = new EchoThread(socket);
      thread.start();
    }
  }
}