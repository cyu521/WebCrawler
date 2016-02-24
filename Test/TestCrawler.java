import static org.junit.Assert.*;

import org.junit.Test;

import Util.HtmlParser;
import Util.URLHandler;
import Crawler.Download;


public class TestCrawler<assertArrayEquals> {

	@Test
	public void testDownloadPage() {
		Download pageDownloader = new Download();
		String pageHtml = pageDownloader.downloadPage("http://www.cnn.com");
		assertNotEquals("", pageHtml);
	}
	private String MOCK_HTML_STRING = "<html><head><body><a href='http://www.notreallinks.com'></a></body></head></html>";
	private String MOCK_ACTUAL_BODY = "<body>\n <a href=\"http://www.notreallinks.com\"></a>\n</body>";
	@Test
	public void testParseBody() {
		HtmlParser htmlParser = new HtmlParser();
		String body = htmlParser.parse(MOCK_HTML_STRING);
		//Should print out a well-formated body
		assertEquals(MOCK_ACTUAL_BODY, body);
	}

	private String[] MOCK_ACTUAL_LINK = {"http://www.notreallinks.com"};

	@Test
	public void testFindLink() {
		HtmlParser htmlParser = new HtmlParser();
		String[] links = htmlParser.findLinks(MOCK_HTML_STRING);
		//Should print out a well-formated body
		assertArrayEquals(MOCK_ACTUAL_LINK, links);
	}
	
	@Test
	public void testURLHandler() {
		URLHandler handler = new URLHandler();
		assertTrue(handler.urlValidator("http://society.cs.drexel.edu/"));
	}

}
