package Crawler;

import java.io.File;
import java.util.List;

import Util.ConsoleInput;
import Util.HtmlParser;
import Util.Output;
import Util.TreeNode;
import Util.URLHandler;

public class Crawler {

	private static CrawlerThread ct;
	private static boolean isFinish = false;
	private static boolean isPause = false;
	private static Thread t1;
	private static Thread t2;
	private static Thread thread;
	private static Thread mainThread;
	public static void main(String[] args) {
		mainThread = new Thread(new Runnable() {
            public void run() {
        		menu();
            }
        });
		mainThread.start();
	}
	@SuppressWarnings("deprecation")
	private static void menu(){
		displayMenu();
		switch(getMenuOption()){
		case 1:
			if(ct == null){

		        t2 = new Thread(new Runnable() {
		            public void run() {
		            	startCrawl();
		            }
		        });

		        try {
		            t2.start();
		        } catch (Exception e) {

		        }
			}
			else if(!isFinish){
				System.out.println("Thread already started");
				menu();
			}
			break;
		case 2:
			if(ct == null){
				System.out.println("Can't pause if thread have not start yet");
				menu();
			}else if(!isFinish){
				System.out.println("pausing");
				isPause= true;
				ct.pauseThread();
				menu();
			}else{
				System.out.println("Can't pause thread, thread might be printing");
			}
			break;
		case 3:
			if(ct == null){
				System.out.println("Can't resume if thread have not start yet");
				menu();
			}else if(isPause && !isFinish){
				isPause= false;
				System.out.println("resuming");
		        Thread t1 = new Thread(new Runnable() {
		            public void run() {
		            	menu();
		            }
		        });
	            t1.start();
				ct.resumeThread();
				try {
					thread.join();
					if(!isPause)
						postThread();
				} 
				catch (Exception e){
					System.out.println("Error: There was a problem crawling some website...");
				}
			}
			else{
				System.out.println("Can't resume thread, thread might not be paused or is printing");
			}
			break;
		case 4:
			System.out.println("exiting");
			if(t2 != null)
				t2.stop();
			break;
		}
	}

	private static void startCrawl() {
		//ask user for the link and depth
		String link = getLink();
		int depth = getDepth();
		String[] wordList = getWordList();
		HtmlParser parser = new HtmlParser();
		
		//delete the sitemap directory and re-create an empty directory
		System.out.println("deleting sitemap directory");
		deleteAndMkdir("sitemap");

		ct = new CrawlerThread(link, 0, depth+1,wordList);
		//check robot txt
		if(parser.isUrlAllow(link)){
			  t1 = new Thread(new Runnable() {
		            public void run() {
		            	menu();
		            }
		        });
		        t1.start();
				//create the root crawler thread
				thread = new Thread(ct);
				try{
					thread.start();
					//wait till thread to end before accessing the tree
					System.out.println("Crawling the web... This might take a minute or two...");
					try {
						thread.join();
					} 
					catch (Exception e){
						System.out.println("Error: There was a problem crawling some website...");
					}
				}
				catch (Exception e){
					System.out.println("Error: There was a problem crawling some website...");
				}
		}
       
		if(!isPause)
			postThread();
	}
	@SuppressWarnings("deprecation")
	private static void postThread() {
		isFinish = true;
		//go through all the nodes in a pre-order
		TreeNode<SiteInfo> root = ct.getCurrentTree();
		if(root != null)
			//all the folder and index.html will be store in sitemap folder
			traversalTree(root, "sitemap/");
		else
			System.out.println("Error with link. Might be disallowed from robot.txt");
		//let the user know that the program reaches its last breath.
		System.out.println("Finished crawling the web. Press Enter or return button to exit the Application.");
		mainThread.stop();

	}

	private static void deleteAndMkdir(String path) {
		File dir = new File(path);
		//deletes directory
		deleteDir(dir);
		//create empty directory
		dir.mkdirs();
	}
	
	//recursively delete the directory
	private static boolean deleteDir(File dir) 
	{ 
	  if (dir.isDirectory()) 
	  { 
		  String[] children = dir.list(); 
		  for (int i=0; i<children.length; i++)
		  { 
		    boolean success = deleteDir(new File(dir, children[i])); 
		    if (!success) 
		    {  
		      return false; 
		    } 
		  } 
	  }  
	  // The directory is now empty or this is a file so delete it 
	  return dir.delete(); 
	} 

	private static void traversalTree(TreeNode<SiteInfo> root, String path){
		//get the data of the node
		SiteInfo site = root.getData();
		int depth = site.getDepth();
		String link = site.getLink();
		String body = site.getBody();
		String trimmedBody = site.getTrimmedBody();
		Output output = new Output();
		
		//print out the depth and link.
		//not printing out the body since it is making the console really weird.. cleaner this way..
		//can simply do it by removing the comment below and comment out the next line
		output.consolePrint(depth, link, trimmedBody);
		//output.consolePrint(depth, link);
		//need to trim link inorder to make a directory
		path+=link.replace(":", "").replace("/", "").replace("?", "").replace("\\", "")+"/";
		output.fileOutput(body, path);
		//go through the child of this node to do preorder
		if(root.hasChild())
		{
		    List<TreeNode<SiteInfo>> children = root.getChildren();
		    if(children != null)
				for (TreeNode<SiteInfo> child : children) {
					if(child != null)
						traversalTree(child, path);
				}
		}
		
	}

	
	private static String[] getWordList() {
		int numKeyWords = getNumberOfKeywords();
		ConsoleInput ci = new ConsoleInput();
		String[] wordList = new String[numKeyWords];
		for(int i=0; i<numKeyWords; i++){
			int wordNum = i+1;
			System.out.println("Enter word "+ wordNum);
			wordList[i] = ci.getInputString();
		}
		return wordList;
	}

	private static int getNumberOfKeywords() {

		System.out.println("Enter the # of keywords");
		ConsoleInput ci = new ConsoleInput();
		int depth = ci.getInputInt();
		if(depth < 0){
			System.out.println("Error with input, please try again");
			depth = getDepth();
		}
		return depth;
	}

	private static int getDepth() {
		System.out.println("Enter the depth # for this url search, recommend below 5");
		ConsoleInput ci = new ConsoleInput();
		int depth = ci.getInputInt();
		if(depth < 0){
			System.out.println("Error with input, please try again");
			depth = getDepth();
		}
		return depth;
	}

	private static String getLink() {
		System.out.println("Enter the URL to crawl (ie: http://www.example.com)");
		ConsoleInput ci = new ConsoleInput();
		String link = ci.getInputString();
		URLHandler urlHandler = new URLHandler();
		if(!urlHandler.urlValidator(link))
		{
			System.out.println("Error with url, please try again");
			link = getLink();
		}
		return link;
	}

	private static int getMenuOption(){
		ConsoleInput ci = new ConsoleInput();
		int menuOption = ci.getInputInt();
		if(menuOption < 1 || menuOption > 4){
			System.out.println("Error with input, please try again");
			menuOption = getMenuOption();
		}
		return menuOption;
		
	}
	private static void displayMenu(){
		System.out.println("Select from an option below");
		System.out.println("1. start");
		System.out.println("2. pause");
		System.out.println("3. resume");
		System.out.println("4. quit");
	}
}
