package genius.mohammad.httpserver.lite;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity {

	HTTPServer server;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// initialize
		server = new HTTPServer("0.0.0.0", 8080);
		Log.d("Socket", "Server started");
		server.start();
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		TextView step3 = (TextView) findViewById(R.id.step3label);
		step3.setText("3. Access the URL: http://" + server.getSocketIP(wifiInfo) + ":" + server.port);
		buttonInit();
		updatePrefs();
	}

	private void updatePrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		TextView sourceTV = (TextView) findViewById(R.id.htmlSourceTextView);
		String source = prefs.getString("source", "<html>\n" + "<title>\n" + "Android HTTP Server\n" + "</title>\n" + "<body>\n" + "Welcome to Android HTTP Server!\n" + "</body>\n" + "</html>");
		sourceTV.setText(source);
		ServerSource.setServerSource(source);
	}

	private void updateEditor() {
		TextView tv = (TextView) findViewById(R.id.htmlSourceTextView);
		tv.setText(ServerSource.getServerSource());
	}

	private void buttonInit() {
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TextView tv = (TextView) findViewById(R.id.htmlSourceTextView);
				String source = tv.getText().toString();
				ServerSource.setServerSource(source);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("source", source);
				editor.commit();
			}

		});
		Button discardButton = (Button) findViewById(R.id.discardButton);
		discardButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TextView tv = (TextView) findViewById(R.id.htmlSourceTextView);
				tv.setText("");
			}

		});
		Button importButton = (Button) findViewById(R.id.importButton);
		importButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				importHTMLFile();
			}

		});

		Button exportButton = (Button) findViewById(R.id.exportButton);
		exportButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (exportHTMLFile()) {
					Toast.makeText(MainActivity.this.getApplicationContext(), "Successfully exported HTML file " + Environment.getExternalStorageDirectory() + File.separator + "html" + File.separator + "index.html", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this.getApplicationContext(), "Failed to export HTML file", Toast.LENGTH_SHORT).show();
				}
			}

		});

		Button rebootButton = (Button) findViewById(R.id.rebootServerButton);
		rebootButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				rebootServer();
				Toast.makeText(MainActivity.this, "Server rebooted", Toast.LENGTH_SHORT).show();
			}

		});
		Button moreInfoButton = (Button) findViewById(R.id.moreInfoButton);
		moreInfoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(i);
			}

		});
	}

	private boolean exportHTMLFile() {
		try {
			File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "html");
			File htmlfile = new File(Environment.getExternalStorageDirectory() + File.separator + "html" + File.separator + "index.html");
			dir.mkdirs();
			htmlfile.createNewFile();
			FileOutputStream out = new FileOutputStream(htmlfile);
			out.write(ServerSource.getServerSource().getBytes());
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void rebootServer() {
		updateServer("0.0.0.0", 8080);
	}

	private void updateServer(String ip, int port) {
		server.stop();
		server = new HTTPServer(ip, port);
		server.start();
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		TextView step3 = (TextView) findViewById(R.id.step3label);
		step3.setText("3. Access the URL: http://" + server.getSocketIP(wifiInfo) + ":" + server.port);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String filePath = uri.getPath();
				Log.d("IO", filePath);
				try {
					FileInputStream fstream = new FileInputStream(filePath);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String strLine = "";
					String code = "";
					while ((strLine = br.readLine()) != null) {
						code += strLine + "\n";
					}
					in.close();
					ServerSource.setServerSource(code);
					updateEditor();
				} catch (Exception e) {// Catch exception if any
					e.printStackTrace();
				}
			}
		}
	}

	private void importHTMLFile() {
		Intent chooseFile;
		Intent intent;
		chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
		chooseFile.setType("file/*");
		intent = Intent.createChooser(chooseFile, "Choose a file");
		startActivityForResult(intent, 1);
	}

	public void onDestroy() {
		super.onDestroy();
		server.stop();
	}
}