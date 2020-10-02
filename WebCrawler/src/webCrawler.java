import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

public class webCrawler {
	// creates hash map for all the links starting from seed and so on
	// and records the url and its outlinks
	private HashMap<String, Integer> links;
	
	// sets max depth to crawl through
	private static final int MAX_DEPTH = 4;
	
	// constructor
	public webCrawler() {
		links = new HashMap<String, Integer>();
	}
	
	// returns the full hash map of all the links the crawler crawled through
	public HashMap<String, Integer> getPageLinks() {
		return links;
	}
	
	public void getLinks(String URL, int depth) {
		// Checks if the URL has already been crawled
		// if the link is not in the hash table, add the link in the table
		if ((!links.containsKey(URL) && (depth < MAX_DEPTH))) {
			try {
				// Fetch HTML code and parse it to find all the outlinks
				// on the current URL
				Document document = Jsoup.connect(URL).get();
				Elements pageLinks = document.select("a[href]");
				
				// increments depth each time after finding all the
				// outlinks from the current url
				depth++;
				// prints out current URL name
				System.out.println(">> URL: " + URL);
				// puts current URL and its outlinks into hash map
				Integer i = Integer.valueOf(pageLinks.size());
				links.put(URL, i);
				
				
				// Connects to current URL and parses it to read through
				// the contents of the page
				Connection.Response response = Jsoup.connect(URL).execute();
				Document responseDoc = response.parse();
				// Writes a html file of the current URL into repository folder
				FileWriter myWriter = new FileWriter("./repository/" + document.title().replace(" ", "").replace(":", "").replace("|", "") + ".html", true);
				myWriter.write(responseDoc.outerHtml());
				myWriter.close();
				System.out.println("Successfully downloaded page");
				
				
				// for loop loops through each outlink found from the url and
				// recursively finds more outlinks 
				for (Element page : pageLinks) {
					getLinks(page.attr("abs:href"), depth);
				}
			} catch (IOException e) {
				// prints out error if the url CANNOT be crawled
				// because of politeness policy or error 
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		webCrawler wc = new webCrawler();
		// set seed URL
		wc.getLinks("https://www.cpp.edu/", 0);
		
		// writes URL and outlinks to csv file
		File csvFile = new File("report.csv");
		try (PrintWriter cFile = new PrintWriter(csvFile)) {
			Map<String, Integer> links = wc.getPageLinks();
			for (Map.Entry<String, Integer> page : links.entrySet()) {
				cFile.append(page.getKey() + ", " + page.getValue() + "\n");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
