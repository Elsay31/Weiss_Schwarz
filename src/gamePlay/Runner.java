package gamePlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

// Version: 2.6.15

public class Runner {

	// public static Connector connector;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		PrintStream debugLog;
		try {
			debugLog = new PrintStream(new File("Debug.llog"));
			 //PrintWriter writer = new PrintWriter(new
			 //OutputStreamWriter(debugLog, "UTF-8"), true);
			 // System.setOut(debugLog);
			 // System.setErr(debugLog);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} // catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// }

		Player player1 = new Player();
		player1.buildAndDisplay();
	}
}
