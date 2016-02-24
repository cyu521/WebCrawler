package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Output{

	public void consolePrint(int depth, String link){

		StringBuffer sb = new StringBuffer("");
		//add space to each depth
		for(int i=0; i<depth; i++){
			sb.append(" ");
		}
		sb.append(depth);
		sb.append(" ");
		sb.append(link);
		System.out.println(sb.toString());
	}
	//create a directory
	//create an index under the directory
	public void fileOutput(String body, String path){
		File dir = new File(path);
		dir.mkdirs();
		try {
			PrintWriter out = new PrintWriter(path+"index.html");
			out.println(body);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//print to console the depth, link, and body
	public void consolePrint(int depth, String link, String body) {
		StringBuffer sb = new StringBuffer("");
		//add space to each depth
		for(int i=0; i<depth; i++){
			sb.append(" ");
		}
		sb.append(depth);
		sb.append(" ");
		sb.append(link);
		sb.append(" ");
		sb.append(body);
		System.out.println(sb.toString());
		
	}
}
