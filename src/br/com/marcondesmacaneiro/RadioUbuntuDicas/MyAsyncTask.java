package br.com.marcondesmacaneiro.RadioUbuntuDicas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

	ProgressDialog mProgressDialog;

	@Override
	protected void onPostExecute(Void result) {
		Log.e("TESTE", "VAI onPostExecute");
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
