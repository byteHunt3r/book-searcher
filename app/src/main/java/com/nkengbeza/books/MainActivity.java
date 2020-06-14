package com.nkengbeza.books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nkengbeza.books.utils.ApiUtil;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView displayTextView;
    private ProgressBar fetchingPgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchingPgBar = findViewById(R.id.dataLoading);
        displayTextView = findViewById(R.id.tvResponse);


        try {
            URL bookUrl = ApiUtil.buildUrl("Rust programming");

            BooksQueryTask task = new BooksQueryTask();
            task.execute(bookUrl);

        } catch (IOException e) {
            Toast.makeText(this, "Could not connect to the googleapis service!", Toast.LENGTH_LONG)
                    .show();
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    class BooksQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... urls) {
            try {
                return ApiUtil.getJson(urls[0]);
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayTextView.setText("");
            fetchingPgBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            displayTextView.setText(s);
            fetchingPgBar.setVisibility(View.INVISIBLE);
        }
    }

}
