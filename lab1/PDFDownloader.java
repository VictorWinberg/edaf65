package lab1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.nio.file.*;

import lab2.RunnerThread;
import lab2.RunnerRunnable;
import lab2.RunnerCallable;

public class PDFDownloader {

  private LinkedList<String> pdfs;

  public static void main(String[] args) throws IOException {
    new PDFDownloader(args[0], args.length > 1 ? args[1] : "default");
  }

  public PDFDownloader(String website, String mode) throws IOException {
    String page = downloadPage(website);
    List<String> hrefs = findHyperLinks(page);
    pdfs = findPDFs(hrefs);

    switch (mode) {
      case "thread":
        for (int i = 0; i < 10; i++) {
          RunnerThread thread = new RunnerThread(this);
          thread.start();
        }
        break;
      case "run":
        for (int i = 0; i < 10; i++) {
          RunnerRunnable runnable = new RunnerRunnable(this);
          runnable.run();
        }
        break;
      case "exe":
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for(String pdf : pdfs){
          Runnable task = new RunnerCallable(pdf);
          pool.submit(task);
        }
        pool.shutdown();
        break;
      case "default":
        for (String pdf : pdfs) {
          downloadPDF(pdf);
        }
    }
  }

  public synchronized boolean hasPDF() {
    return !pdfs.isEmpty();
  }

  public synchronized String getPDF() {
    return pdfs.poll();
  }

  public String downloadPage(String url) throws IOException {
    InputStream input = null;
    StringBuilder content = new StringBuilder();

    input = new URL(url).openStream();

    BufferedReader in = new BufferedReader(new InputStreamReader(input));
    String inputLine;

    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    input.close();
    return content.toString();
  }

  public List<String> findHyperLinks(String page) {
    Set<String> links = new HashSet<String>();

    // <a> tags
    Pattern aTags = Pattern.compile("<a(.*?)/a>");
    Matcher aMatcher = aTags.matcher(page);

    while (aMatcher.find()) {
      String aTag = aMatcher.group();

      // hrefs
      Pattern hrefs = Pattern.compile("href=\"(.*?)\"");
      Matcher hrefMatcher = hrefs.matcher(aTag);

      if (hrefMatcher.find()) {
        String link = hrefMatcher.group(1);
        links.add(link);
      }
    }

    List<String> list = new ArrayList<String>(links);
    return list;
  }

  public LinkedList<String> findPDFs(List<String> links) {
    return links.stream()
    .filter(x -> x.endsWith(".pdf"))
    .collect(Collectors.toCollection(LinkedList::new));
  }

  public static void downloadPDF(String url) {
    String filename = url.substring(url.lastIndexOf("/") + 1);
    new File("downloads").mkdir();

    try {
      InputStream in = new URL(url).openStream();
      Path path = Paths.get("downloads/" + filename);
      Files.copy(in, path);
      in.close();

    } catch (IOException e) {
      // File not found or already existing
    }
  }
}
