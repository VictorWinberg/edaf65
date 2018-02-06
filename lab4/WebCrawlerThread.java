package lab4;

import java.io.IOException;
import java.util.HashSet;

public class WebCrawlerThread extends Thread {

    private WebCrawler crawler;

    public WebCrawlerThread(WebCrawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void run() {
        while(crawler.hasWork()) {
            try {
                String href = crawler.getHref();
                HashSet<String> hrefs = WebCrawler.parse(href);
                crawler.addHrefs(hrefs);
            } catch (IOException | InterruptedException e) {
                // continue
            }
        }
        crawler.print();
    }
}
