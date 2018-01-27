package lab3.thread;

import lab3.thread.WriteThread;
import lab3.thread.ReadThread;

public class Mailbox {
  
  private String value;
  
  public Mailbox() {
    value = "Initialized";
  }
  
  public synchronized void set(String value) throws InterruptedException {
    while(this.value != null) {
      wait();
    }
    this.value = value;
  }
  
  public synchronized String get() throws InterruptedException {
    while(value == null) {
      wait();
    }
    String temp = value;
    value = null;
    notifyAll();
    return temp;
  }
  
  public static void main(String[] args) {
    Mailbox mailbox = new Mailbox();
    for (int i = 0; i < 10; i++) {
      WriteThread thread = new WriteThread("Thread " + (i + 1), mailbox);
      thread.start();
    }
    ReadThread read = new ReadThread(mailbox);
    read.start();
  }
}