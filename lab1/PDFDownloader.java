import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.nio.file.*;

public class PDFDownloader {

  public static void main(String[] args) throws IOException {
    new PDFDownloader(args[0]);
  }

  public PDFDownloader(String website) throws IOException {
    String homepage = downloadPage(website);
    List<String> startlinks = findHyperLinks(homepage);
    startlinks.add(0, website);

    for (String link : startlinks) {
      try {
        String page = downloadPage(link);
        List<String> links = findHyperLinks(page);

        List<String> pdfs = findPDFs(links);
        for(String pdf : pdfs){
          downloadPDF(pdf, link);
        }
      } catch(Exception e) {
        System.out.println(e.getMessage());
      }
    }
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

  public List<String> findPDFs(List<String> links) {
    return links.stream()
    .filter(x -> x.endsWith(".pdf"))
    .collect(Collectors.toList());
  }

  public void downloadPDF(String url, String folder){
    String filename = url.substring(url.lastIndexOf("/") + 1);
    new File("downloads").mkdir();

    try {
      InputStream in = new URL(url).openStream();
      Path path = Paths.get("downloads/" + filename);
      Files.copy(in, path);
      in.close();

      System.out.println("Downloaded " + filename);

    } catch (IOException e) {
      System.out.println(filename + " not found or already existing");
    }
  }
}
