package Crawler;

import java.util.ArrayList;

import Util.HtmlParser;
import Util.TreeNode;
import Util.URLHandler;

public class CrawlerThread implements Runnable {
	private String link;
	private int start;
	private int end;
	private String[] wordList;
	private TreeNode<SiteInfo> currentTree;
	private ArrayList<CrawlerThread> crawlerThreadList = new ArrayList<CrawlerThread>();
	private boolean pauseThread = false;

	private final HtmlParser parser = new HtmlParser();

	private ArrayList<String> resumeStringList = new ArrayList<String>();

	public CrawlerThread(String link, int start, int end, String[] wordList) {
		this.link = link;
		this.start = start;
		this.end = end;
		this.wordList = wordList;
	}

	public TreeNode<SiteInfo> getCurrentTree() {
		for (int i = 0; i < crawlerThreadList.size(); i++) {
			currentTree.addChild(crawlerThreadList.get(i).getCurrentTree());
		}
		return currentTree;
	}

	public void pauseThread() {

		if (pauseThread) {
			System.out.println("Error: Thread is already paused, this can happen if thread have not done resuming");
		} else {
			pauseThread = true;
			for (int i = 0; i < crawlerThreadList.size(); i++) {
				crawlerThreadList.get(i).pauseThread();
			}
		}
		
	}

	public void resumeThread() {
		if (pauseThread) {
			pauseThread = false;
			for (int i = 0; i < crawlerThreadList.size(); i++) {
				crawlerThreadList.get(i).resumeThread();
			}
			if (resumeStringList.size() != 0) {
				String[] stringArray = new String[resumeStringList.size()];
				resumeStringList.toArray(stringArray);
				createChildrenThread(stringArray);
			}
		} else {
			System.out.println("Race condition error: Please wait a few seconds between each pauses");
		}

	}

	@Override
	public void run() {

		try {
			Download pageDownloader = new Download();
			// gets the html from the link
			String html = pageDownloader.downloadPage(link);
			// end thread if html is null
			if (html == null)
				return;
			// get the body html
			String body = parser.parse(html);
			if (body == null)
				return;
			// this make sure that the body have the word
			if (!parser.doesBodyContainWordList(body, wordList))
				return;
			SiteInfo currentSite = new SiteInfo(start, link, body);
			// current tree is root if no parent, else add as a child
			currentTree = new TreeNode<SiteInfo>(currentSite);

			// increment depth
			start++;

			// end the recursive if depth meet
			if (start == end)
				return;

			String[] linkList = parser.findLinks(html);
			createChildrenThread(linkList);
			return;
		} catch (Exception e) {
			System.out.println("Error with thread. exiting");
			return;
		}
	}

	private void createChildrenThread(String[] linkList) {
		int length = linkList.length;
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		for (int i = 0; i < length; i++) {
			String htmlLink = linkList[i];
			URLHandler urlHandler = new URLHandler();
			// make sure this is a good url
			if (urlHandler.urlValidator(htmlLink)) {
				if (!pauseThread) {
					//check robot txt
					if(parser.isUrlAllow(htmlLink)){
						// starts a recursive thread with currentTree
						CrawlerThread runnable = new CrawlerThread(htmlLink.trim(),
								start, end, wordList);
						Thread thread = new Thread(runnable);
						thread.start();
						threadList.add(thread);
						crawlerThreadList.add(runnable);
					}
				} else {
					resumeStringList.add(htmlLink);
				}
			}
		}
		// wait for all threads to end.
		for (int i = 0; i < threadList.size(); i++) {
			try {
				threadList.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
