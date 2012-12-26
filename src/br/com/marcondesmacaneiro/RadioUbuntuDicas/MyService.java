package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	private static final String TAG = "MyService";
	MediaPlayer player = new MediaPlayer();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		
		Uri myUri = Uri.parse("http://74.222.1.197:13588");
		try {
			//player = MediaPlayer.create(this, R.raw.braincandy);
			//player.setLooping(false); // Set looping
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.create(this, myUri);
			player.setDataSource(this, myUri);
			//mediaPlayer.start();
			//player.setOnCompletionListener(this);
			//player.setOnPreparedListener(this);
			//player.setOnBufferingUpdateListener(this);
			//player.setOnErrorListener(this);
			//player.prepare(); // might take long! (for buffering, etc)
		} catch (Throwable t) {
			Log.d(TAG, t.toString());
		}
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		player.stop();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		player.start();
	}
}
