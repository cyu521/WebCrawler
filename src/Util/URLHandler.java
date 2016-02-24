package Util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLHandler {

	public boolean urlValidator(String link){
		URL u;
		boolean returnValue;
		try {
			u = new URL(link);
			u.toURI(); // does the extra checking required for validation of URI 
			returnValue=true;
		} catch (MalformedURLException e) {
			returnValue=false;
		} // this would check for the protocol
		 catch (URISyntaxException e) {
				returnValue=false;
	 	}
		return returnValue;
		}

}
