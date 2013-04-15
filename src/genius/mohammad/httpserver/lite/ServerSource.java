package genius.mohammad.httpserver.lite;

public class ServerSource {

	private static String serverSource;

	public static synchronized void setServerSource(String newSource) {
		serverSource = newSource.replaceAll("<html>", "").replaceAll("</html>", "").trim();
	}

	public static synchronized String getServerSource() {
		return "<html>\n" + serverSource + "\n</html>";
	}

}
