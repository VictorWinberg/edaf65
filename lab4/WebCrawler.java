package lab4;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

public class WebCrawler {

  private HashSet<String> traversedURLs;
  private BlockingQueue<String> remainingURLs;

  private List<String> urls;
  private List<String> emails;

  public static void main(String[] args) throws IOException, InterruptedException {
    new WebCrawler(args[0], args.length > 1 ? args[1] : "default");
  }

  public WebCrawler(String website, String mode) throws IOException, InterruptedException {
    traversedURLs = new HashSet<>();
    remainingURLs = new LinkedBlockingQueue<>();
    traversedURLs.add(website);
    remainingURLs.add(website);

    switch (mode) {
      case "thread":
        for (int i = 0; i < 10; i++) {
          WebCrawlerThread thread = new WebCrawlerThread(this);
          thread.start();
        }
        break;
      case "default":
        while(hasWork()) {
          String href = getHref();
          HashSet<String> hrefs = WebCrawler.parse(href);
          addHrefs(hrefs);
        }
        print();
        break;
      default:
        System.out.println("Incorrect mode: " + mode);
    }
  }

  public synchronized boolean hasWork() {
    return traversedURLs.size() < 8000;
  }

  public synchronized String getHref() throws InterruptedException {
    while(remainingURLs.isEmpty()) {
      wait();
    }
    return remainingURLs.take();
  }

  public synchronized void addHrefs(HashSet<String> hrefs) {
    for (String href : hrefs) {
      if(!traversedURLs.contains(href)) {
        traversedURLs.add(href);
        remainingURLs.add(href);
      }
    }
    notifyAll();
  }

  public synchronized void print() {
    if (emails != null || urls != null) {
      return;
    }
    emails = traversedURLs.stream()
      .filter(x -> x.startsWith("mailto"))
      .collect(Collectors.toList());
    urls = traversedURLs.stream()
      .filter(x -> !x.startsWith("mailto"))
      .collect(Collectors.toList());
    simplePrint(urls);
    simplePrint(emails);
  }

  private void simplePrint(List<String> hrefs) {
    int i = 0;
    for (String href : hrefs) {
      System.out.println(++i + " " + href);
    }
  }

  public static HashSet<String> parse(String href) throws IOException {
    HashSet<String> hrefs = new HashSet<>();
    URL url = null;
    InputStream is = null;
    try {
      url = new URL(href);
      is = url.openStream();
    } catch (IOException e) {
      return hrefs;
    }
    String baseUrl = url.getProtocol() + "://" + url.getHost();
    Document doc = Jsoup.parse(is, "UTF-8", baseUrl);
    Elements base = doc.getElementsByTag("base");
    Elements links = doc.getElementsByTag("a");
    for (Element link : links) {
      String linkHref = link.attr("href");
      String linkAbsHref = link.attr("abs:href");
      String linkText = link.text();
      hrefs.add(linkAbsHref);
      //System.out.println("href: " + linkHref + "abshref: " + linkAbsHref + " text: " + linkText);
    }
    is.close();
    return hrefs;
  }
}
