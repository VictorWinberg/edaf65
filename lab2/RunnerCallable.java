package lab2;

import lab1.PDFDownloader;

public class RunnerCallable implements Runnable {

  private String url;

  public RunnerCallable(String url) {
    this.url = url;
  }

  public void run() {
    PDFDownloader.downloadPDF(url);
  }
}
