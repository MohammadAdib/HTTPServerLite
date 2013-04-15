package genius.mohammad.httpserver.lite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import genius.mohammad.httpserver.lite.R;

public class HelpActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helplayout);
		Button closeButton = (Button) findViewById(R.id.helpClose);
		closeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});
		Button moreOnSocketsButton = (Button)findViewById(R.id.moreOnSockets);
		moreOnSocketsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String url = "https://play.google.com/store/apps/details?id=genius.mohammad.httpserver";
	        	Intent i = new Intent(Intent.ACTION_VIEW);
	        	i.setData(Uri.parse(url));  
	        	startActivity(i);
				finish();
			}
			
		});
	}
}
