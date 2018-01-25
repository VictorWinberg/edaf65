package lab2;

import lab1.PDFDownloader;

public class RunnerRunnable implements Runnable {

  private PDFDownloader downloader;

  public RunnerRunnable(PDFDownloader downloader) {
    this.downloader = downloader;
  }

  public void run() {
    while(downloader.hasPDF()) {
      String pdf = downloader.getPDF();
      PDFDownloader.downloadPDF(pdf);
    }
  }
}
