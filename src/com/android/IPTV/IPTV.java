package com.android.IPTV;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class IPTV extends Activity implements View.OnClickListener,
		View.OnKeyListener, AdapterView.OnItemSelectedListener {

	private String URL = "";
	private EditText editUrl;
	private Spinner spin;
	private int positionSpinner;
	private ProgressDialog dialog;
	private Button submitButton;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage("Connecting Wifi");
		dialog.show();
		
		ConnectionManagement cm = new ConnectionManagement();
		cm.execute((Void)null);
		

		// Bouton lecture vid�o
		
		submitButton = (Button) findViewById(R.id.buttonSubmit);
		submitButton.setOnClickListener(this);
		
		// Zone de saisie + �coute �v�nement clavier
		editUrl = (EditText) findViewById(R.id.urlEdit);
		editUrl.setSingleLine(true);
		editUrl.setOnKeyListener(this);

		// La liste des modes (sc�narios) du spinner
		String[] scenarios = { "Web TV (HTTP)", "Live RTSP Streaming" };
		
		spin = (Spinner) findViewById(R.id.spinner_scenario);
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, scenarios);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);

	}

	public void onClick(View view) {

		URL = editUrl.getText().toString();
		if (URL.equals(""))
			Toast.makeText(this, "Please enter a correct video link !",
					Toast.LENGTH_SHORT).show();
		else {
			// cr�ation de l'activit� lecture vid�o
			Intent i = new Intent(this, PlayingVideo.class);

			Bundle objetbunble = new Bundle();
			objetbunble.putString("URL", URL);
			objetbunble.putInt("position", positionSpinner);
			i.putExtras(objetbunble);

			startActivity(i);
		}

	}

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		if (position == 0) {
			positionSpinner = 0;

		} else {
			positionSpinner = 1;

		}

	}

	public void onNothingSelected(AdapterView<?> parent) {
		spin.setSelection(0);
		positionSpinner = 0;
	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			onClick(view);
			return true;
		} else
			return false;
	}

	private class ConnectionManagement extends
			AsyncTask<Void, Void, Void> {

		

		public void startWifi() {
			WifiManager wM = (WifiManager) getSystemService(WIFI_SERVICE);
			wM.setWifiEnabled(true);
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
			if (wm.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
				startWifi(); // D�marrer le wifi dans le cas o� il est d�sactiv�
					
				for (int i = 0; i < 99; i++) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
			
			return null;
		}

		

		public void onPostExecute(Void param) {
				dialog.cancel();

		}
	}
}
