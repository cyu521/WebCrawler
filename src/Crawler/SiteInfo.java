package Crawler;

public class SiteInfo {

	private int depth;
	private String link;
	private String body;
	
	public SiteInfo(int depth, String link, String body){
		this.depth = depth;
		this.link = link;
		this.body = body;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getBody() {
		return body;
	}

	public String getTrimmedBody(){
		return body.trim().replace("\n", "");
	}
	public void setBody(String body) {
		this.body = body;
	}

}
