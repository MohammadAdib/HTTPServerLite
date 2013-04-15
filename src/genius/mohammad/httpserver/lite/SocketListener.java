package genius.mohammad.httpserver.lite;

import genius.mohammad.httpserver.lite.R;
import java.net.*;
import java.io.*;

import android.util.Log;

public class SocketListener implements Runnable {

	private Socket socket;

	public SocketListener(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str = socketReader.readLine();
			// Message received successfully
			Log.d("Socket", "New Message: " + str);
			if (str.contains("HTTP")) {
				SocketSpeaker speaker = new SocketSpeaker(socket);
				new Thread(speaker).start();
			}
		} catch (Exception e) {
			// Client disconnected
			e.printStackTrace();
		}
	}
}
