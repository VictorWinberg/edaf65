package lab3.thread;

public class ReadThread extends Thread {

  private Mailbox mailbox;

  public ReadThread(Mailbox mailbox) {
    this.mailbox = mailbox;
  }
  
  @Override
  public void run() {
    while(true) {
      try {
        sleep((long) Math.random());
        String value = mailbox.get();
        System.out.println(value);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}