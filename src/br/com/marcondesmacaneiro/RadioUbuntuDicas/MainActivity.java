package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements
		MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,
		OnClickListener {

	private String TAG = getClass().getSimpleName();

	private Button playButton;
	private static final int HELLO_ID = 1;
	MediaPlayer mediaPlayer = new MediaPlayer();
	
	ProgressDialog dialog;

	Intent playbackServiceIntent;

	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// playButton = (Button) this.findViewById(R.id.btn_play);
		// stopButton = (Button) this.findViewById(R.id.btn_stop);
		//
		// playButton.setOnClickListener(this);
		// stopButton.setOnClickListener(this);
		//
		// playbackServiceIntent = new
		// Intent("br.com.marcondesmacaneiro.RadioUbuntuDicas.BackgroundRadioService.class");
		// this.startService(playbackServiceIntent);

		playButton = (Button) findViewById(R.id.btn_play);
		playButton.setOnClickListener(this);

		// Criar o adView
		adView = new AdView(this, AdSize.SMART_BANNER, "a150a16be9ab1be");
		
		// Pesquisar seu LinearLayout presumindo que ele foi dado
		// o atributo android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
		// Adicionar o adView a ele
		layout.addView(adView);

		adView.setGravity(Gravity.BOTTOM);
		
		// Iniciar uma solicitação genérica para carregá-lo com um anúncio
		adView.loadAd(new AdRequest());
	}

	public void onClick(View v) {
		Log.e("teste", v.toString());
		switch (v.getId()) {
		case R.id.btn_play:
			Log.e("Play", "Button Play");
			try {
				if (mediaPlayer.isPlaying()) {
					Log.e("Stop", "Parando o streaming");
					//mediaPlayer.stop();
					stop();
					//stopService(new Intent(this, MyService.class));
					playButton.setText("Play");
				} else {
					Log.e("Play", "Iniciando o streaming");
					//mediaPlayer.start();
					play();
					//startService(new Intent(this, MyService.class));
					playButton.setText("Stop");
				}
			} catch (Exception e) {
				Log.e("Play", "ERRO no Play");
			}
			break;
		default:
			break;
		}
	}

	@UiThreadTest
	private void play() {
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setMessage("teste");
		dialog.show();
		Uri myUri = Uri.parse("http://74.222.1.197:13588");
	
		try {
	    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.create(this, myUri);
			mediaPlayer.setDataSource(this, myUri);
			//mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

                @Override
                public void onCompletion(MediaPlayer media) {
                    dialog.dismiss();
                    if (media != null)
            			media.release();
                }

            });
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnErrorListener(this);
			//mediaPlayer.prepare(); // might take long! (for buffering, etc)
			mediaPlayer.create(this, myUri);
			Thread thread = new Thread(new Runnable() {
	            public void run() {
	    			mediaPlayer.prepareAsync();
	                //mediaPlayer.start();
	            }
	        });
	        thread.start();
			

			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			int icon = R.drawable.icon_radio;
			CharSequence tickerText = "Bem Vindo! Rádio UBUNTU Dicas";
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);

			Context context = getApplicationContext();
			CharSequence contentTitle = "Rádio UBUNTU Dicas";
			CharSequence contentText = "Seja Bem Vindo!";
			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);

			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);

			mNotificationManager.notify(HELLO_ID, notification);

			Log.d(TAG, "LoadClip Done");
		} catch (Throwable t) {
			Log.d(TAG, t.toString());
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "Stream is prepared");
		mp.start();
	}

	private void stop() {
		mediaPlayer.stop();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.icon_radio;
		CharSequence tickerText = "Pausa Rádio UBUNTU Dicas.";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "Rádio UBUNTU Dicas";
		CharSequence contentText = "Rádio em pausa!";
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(HELLO_ID, notification);
	}

//	private void stop() {
//		mediaPlayer.stop();
//		String ns = Context.NOTIFICATION_SERVICE;
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
//		mNotificationManager.cancel(HELLO_ID);
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (mp != null)
			mp.release();
		StringBuilder sb = new StringBuilder();
		sb.append("Media Player Error: ");
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			sb.append("Not Valid for Progressive Playback");
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			sb.append("Server Died");
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			sb.append("Unknown");
			break;
		default:
			sb.append(" Non standard (");
			sb.append(what);
			sb.append(")");
		}
		sb.append(" (" + what + ") ");
		sb.append(extra);
		Log.e(TAG, sb.toString());
		return true;
	}
	
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.d(TAG, "PlayerService onBufferingUpdate : " + percent + "%");
	}

}
