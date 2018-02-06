package lab3.thread;

public class WriteThread extends Thread {

  private String name;
  private Mailbox mailbox;

  public WriteThread(String name, Mailbox mailbox) {
    this.name = name;
    this.mailbox = mailbox;
  }
  
  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      try {
        sleep((long) Math.random());
        mailbox.set(name + " #" + i);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}