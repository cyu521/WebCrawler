package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser{

	public String parse(String htmlString) {
		Document doc = Jsoup.parse(htmlString);
		Element body = doc.body();
		String returnString = null;
		if(body != null)
			returnString=body.toString();
		return returnString;
	}

	public boolean doesBodyContainWordList(String body, String[] wordList){
		if(wordList.length == 0)
			return true;
		for(int i =0; i<wordList.length; i++){
			if(body.contains(wordList[i]))
				return true;
		}
		return false;
	}
	
	public String[] findLinks(String html){
		if(html == null)
			return new String[0];
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]");
        String[] returnLinkList = new String[links.size()];
        int i =0;
        for( Element link: links){
        	returnLinkList[i] = link.attr("href"); // "http://example.com/"
        	i++;
        }
		return returnLinkList;
	}

	//robot txt
	public boolean isUrlAllow(String url){
		    String strHost;
			try {
				//get the robot txt link IE:http://www.google.com/robots.txt
				strHost = new URL(url).getHost();
			    String strRobot = "http://" + strHost + "/robots.txt"; //robot url
			    //read the robot txt 
			    try(BufferedReader in = new BufferedReader(
			            new InputStreamReader(new URL(strRobot).openStream()))) {
			        String line = null;
			        String mostRecentUserAgent = null;
			        //read each line of robot txt
			        while((line = in.readLine()) != null) { 
			        	//make sure that it has a user-agent.. don't worry about now since this bot dont have any...
			        	if (line.toLowerCase().startsWith("user-agent")) 
				        	{
			                int start = line.indexOf(":") + 1;
			                int end   = line.length();
			                mostRecentUserAgent = line.substring(start, end).trim();
			                //make sure that the most recent user agent is * 
			                if(!mostRecentUserAgent.equals("*"))
			                	mostRecentUserAgent=null;
				        	}
			        	//if the line start with disallow - apply our rule
			        	else if (line.startsWith("Disallow")) {
			        		//make sure that the mostRecentUserAgent is correct
			                if (mostRecentUserAgent != null) {
			                	//+1 for space
			                    int start = line.indexOf(":") + 1;
			                    int end   = line.length();
			                    //get the disallow rule
			                    String rule = line.substring(start, end).trim();
			                    //site is not allow if our url contians this rule
			                    if(url.contains(rule))
			                    	return false;
			                }
			            }
			        }
			        //if everything goes well.. allow this page
			        return true;
			    } catch (IOException e) {
					return false;
			    }
			} catch (MalformedURLException e1) {
				return false;
			}
	}

}
