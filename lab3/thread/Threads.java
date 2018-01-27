package lab3.thread;

public class Threads extends Thread {

  private String name;

  public Threads(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      try {
        sleep((long) Math.random());
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
      System.out.println(name);
    }
  }
  
  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      Threads thread = new Threads("Thread " + (i + 1));
      thread.start();
    }
  }
}
