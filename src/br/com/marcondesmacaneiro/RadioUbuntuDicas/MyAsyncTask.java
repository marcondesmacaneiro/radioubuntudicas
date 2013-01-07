package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import java.net.URI;
import java.util.List;

import net.moraleboost.streamscraper.Scraper;
import net.moraleboost.streamscraper.Stream;
import net.moraleboost.streamscraper.scraper.ShoutCastScraper;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

	ProgressDialog mProgressDialog;
	//private TextView txtNomeMusica;

	@Override
	protected void onPostExecute(Void result) {
		Log.e("TESTE", "VAI onPostExecute");
		
		try {
			Scraper scraper = new ShoutCastScraper();
			List<Stream> streams = scraper.scrape(new URI(
					"http://74.222.1.197:13588"));
			for (Stream stream : streams) {
				//System.out.println("Song Title: " + stream.getCurrentSong());
				//System.out.println("URI: " + stream.getUri());
				//txtNomeMusica.setText(stream.getCurrentSong());
				Log.e("NOMEMUSICA", "Configurando o nome da música "+stream.getCurrentSong());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERRO", "Não capturou o nome da música: " + e.getMessage());
		}
	}

	@Override
	protected void onPreExecute() {
		Log.e("TESTE", "VAI onPreExecute");
	}

	@Override
	protected Void doInBackground(Void... params) {
		// your network operation
		return null;
	}
}
