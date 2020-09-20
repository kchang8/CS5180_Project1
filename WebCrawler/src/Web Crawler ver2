/**
 *
 * @author Chau
 * Modification is based on codes from https://mkyong.com/java/jsoup-basic-web-crawler-example/
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class MyWebCrawler {

    private HashSet<String> links;

    public MyWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {                   
                    System.out.println(URL);
                }
                
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                
                String title = document.title();
                System.out.println("title: " + title);
                
                String text = document.body().text();
                System.out.println("text: " + text);
                System.out.println();
                 
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");
                 
            
                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
            
        }
    }
           
    public static void main(String[] args) {
      
        //1. Pick a URL from the frontier
        new MyWebCrawler().getPageLinks("https://www.cpp.edu/");   

    }
