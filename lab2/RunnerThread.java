package lab2;

import lab1.PDFDownloader;

public class RunnerThread extends Thread {

  private PDFDownloader downloader;

  public RunnerThread(PDFDownloader downloader) {
    this.downloader = downloader;
  }

  @Override
  public void run() {
    while(downloader.hasPDF()) {
      String pdf = downloader.getPDF();
      PDFDownloader.downloadPDF(pdf);
    }
  }
}
