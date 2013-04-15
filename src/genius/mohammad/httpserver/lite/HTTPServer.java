package genius.mohammad.httpserver.lite;

import genius.mohammad.httpserver.lite.R;
import java.net.*;
import android.net.wifi.WifiInfo;
import android.util.Log;

public class HTTPServer {

	int port;
	Thread serverThread;
	private ServerSocket serverSocket;
	private boolean running = true;
	private String ip;

	public HTTPServer(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start() {
		// Server runnable thread
		Runnable serverRunnable = new Runnable() {
			public void run() {
				try {
					InetAddress address = InetAddress.getByName(ip);
					serverSocket = new ServerSocket(port, 0, address);
					Log.d("Socket", "Socket = [" + serverSocket.getLocalSocketAddress().toString() + ":" + serverSocket.getLocalPort() + "]");
					while (running) {
						Socket socket = serverSocket.accept();
						socket.setTcpNoDelay(true);
						if (running) {
							Log.d("Socket", "New Client: " + socket.getInetAddress().getHostAddress().toString());
							SocketListener socketListener = new SocketListener(socket);
							Log.d("Prompt", "Finished!");
							// Listen for data on a seperate thread
							new Thread(socketListener).start();
						}
					}

				} catch (Exception e) {
					// Error occured
					Log.d("Socket", "Error: " + e.getMessage());
				}
			}
		};
		// Run server-side processing on a seperate thread
		serverThread = new Thread(serverRunnable);
		serverThread.start();
	}

	public String getSocketIP(WifiInfo wifiInfo) {
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
	}

	public void stop() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		running = false;
	}
}
