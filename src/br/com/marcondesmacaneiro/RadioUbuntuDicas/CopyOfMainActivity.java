package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import java.util.zip.Inflater;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CopyOfMainActivity extends Activity implements OnClickListener,
		OnPreparedListener, OnErrorListener, OnCompletionListener {

	private Button mostrar;
	private String TAG = getClass().getSimpleName();
	private MediaPlayer mp;
	private ProgressDialog pd;
	private static final int HELLO_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mp = MyMediaPlayer.getInstance().getMediaPlayer();

		mostrar = (Button) findViewById(R.id.botaoVisualizar);
		mostrar.setOnClickListener(this);
		
		if (mp.isPlaying()) {
			//mp.stop();
			Log.e(TAG, "IFFFFF");
			mostrar.setText("Parar");
		} else {
			mostrar.setText("Tocar");
			Log.e(TAG, "ELSEEEEEEEEEEEEE");
		}
	}

	public void onCompletion(MediaPlayer arg0) {
		pd.dismiss();
		Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_LONG)
				.show();
		// mp.start();
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		pd.dismiss();
		return false;
	}

	public void onPrepared(MediaPlayer mp) {
		Log.i("StreamAudioDemo", "finished");
		pd.setMessage("Playing.....");
		mp.start();
		pd.dismiss();

		mostrar.setText("Parar");

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.icon_radio;
		CharSequence tickerText = "Bem Vindo! Rádio UBUNTU Dicas";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "Rádio UBUNTU Dicas";
		CharSequence contentText = "Seja Bem Vindo!";
		Intent notificationIntent = new Intent(this, CopyOfMainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(HELLO_ID, notification);
	}

	public void onClick(View arg0) {
		if (mostrar.getText().toString()=="Parar") {
			stop();
		} else {
			pd = new ProgressDialog(this);
			pd.setMessage("Buffering.....");
			pd.show();
			try {
				if (mp == null) {
					mp = MyMediaPlayer.getInstance().getMediaPlayer();
				}
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mp.setOnPreparedListener(this);
				mp.setOnErrorListener(this);
				mp.setDataSource("http://74.222.1.197:13588");

				mp.prepareAsync();
				mp.setOnCompletionListener(this);
			} catch (Exception e) {
				Log.e(TAG, "Erro ai iniciar o player:  " + e.getMessage());
			}
			pd.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void stop() {
		mp.stop();
		mp.release();
		
		mp = null;

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.icon_radio;
		CharSequence tickerText = "Pausa Rádio UBUNTU Dicas.";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "Rádio UBUNTU Dicas";
		CharSequence contentText = "Rádio em pausa!";
		Intent notificationIntent = new Intent(this, CopyOfMainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(HELLO_ID, notification);

		mostrar.setText("Tocar");
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.d(TAG, "PlayerService onBufferingUpdate : " + percent + "%");
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  
	  savedInstanceState.putString("btnPlay", mostrar.getText().toString());
	  
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  
	  mostrar.setText(savedInstanceState.getString("btnPlay"));
	}
}
