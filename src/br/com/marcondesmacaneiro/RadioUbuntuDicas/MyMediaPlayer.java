package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class MyMediaPlayer extends MediaPlayer {
	
	private static MediaPlayer mp;
	
	private static MyMediaPlayer instance = null;

	private MyMediaPlayer() {
		
	}
	
	public static MyMediaPlayer getInstance () {
		if (instance == null) {
			instance = new MyMediaPlayer();
		}
		if (mp == null) {
			mp = new MediaPlayer();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//mp.setOnPreparedListener(this);
			//mp.setOnErrorListener(this);
			try {
				mp.setDataSource("http://74.222.1.197:13588");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return instance;
	}
	
	public MediaPlayer getMediaPlayer() {
		return mp;
	}
	
}
