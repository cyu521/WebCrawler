package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleInput{

	/*
	 * Ask the user to input an int
	 */
	public int getInputInt() {
		String line = null;
		int input = 0;
		line = getInputString();
		if (line == null)
			return -1;
		try {
			input = Integer.parseInt(line);

		} catch (NumberFormatException ex) {
			return -1;
		}
		return input;
	}
	

	public String getInputString() {
		String line = null;
        InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader is = new BufferedReader(isr);
		try {
			line = is.readLine();
		} catch (NumberFormatException ex) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return line;
	}
}
