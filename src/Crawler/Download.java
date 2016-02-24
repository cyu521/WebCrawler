package Crawler;


import org.jsoup.Jsoup;

public class Download {

	//using jsoup lib to download page
	public String downloadPage(String link){
		String returnString = null;
		try {
			returnString = Jsoup.connect(link).get().toString();
		} catch (Exception a) {
		}
		return returnString;
	}
}
