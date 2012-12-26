package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

class BackgroundRadioService extends Service implements
		OnCompletionListener {

	private String TAG = getClass().getSimpleName();
	// private static final MediaPlayer mp = new MediaPlayer();
	MediaPlayer mediaPlayer;

	private static final int HELLO_ID = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Uri myUri = Uri.parse("http://74.222.1.197:13588");
		try {
			if (mediaPlayer == null) {
				// this.mpUD = new MediaPlayer();
			} else {
				mediaPlayer.stop();
				mediaPlayer.reset();
			}
			mediaPlayer.setDataSource(this, myUri); // Go to Initialized state
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// mediaPlayer.setOnPreparedListener(this);
			// mediaPlayer.setOnBufferingUpdateListener(this);
			// mediaPlayer.setOnErrorListener(this);
			mediaPlayer.prepareAsync();

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

		// mediaPlayer = MediaPlayer.create(this, R.raw.s);// raw/s.mp3
		// mediaPlayer.setOnCompletionListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.w("Command", "onStartCommand");
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
		return START_STICKY;
	}

	public void onDestroy() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
	}

	public void onCompletion(MediaPlayer _mediaPlayer) {
		stopSelf();
	}
}
