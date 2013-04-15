package genius.mohammad.httpserver.lite;

import genius.mohammad.httpserver.lite.R;
import java.net.*;
import java.io.*;

public class SocketSpeaker implements Runnable {

	private Socket socket;

	public SocketSpeaker(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
			String data = ServerSource.getServerSource();
			socketWriter.write("HTTP/1.1 200 OK\n");
			socketWriter.write("Content-Type: text/html\n");
			socketWriter.write("Connnection: close\n");
			socketWriter.write("<!DOCTYPE HTML>\n");
			socketWriter.write("\n");
			socketWriter.write(data);
			socketWriter.flush();
			socketWriter.close();
			socket.shutdownOutput();
			socket.close();
		} catch (Exception e) {
			// Client disconnected
			System.out.println("Client disconnected.");
		}
	}
}
