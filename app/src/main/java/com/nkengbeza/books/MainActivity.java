package com.nkengbeza.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nkengbeza.books.model.Book;
import com.nkengbeza.books.ui.BooksAdapter;
import com.nkengbeza.books.ui.SearchActivity;
import com.nkengbeza.books.utils.ApiUtil;
import com.nkengbeza.books.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView rvBooks;
    private TextView tvGenericMsg;
    private ProgressBar fetchingPgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBooks = findViewById(R.id.rv_books);
        tvGenericMsg = findViewById(R.id.tv_generic_msg);
        fetchingPgBar = findViewById(R.id.data_loading);

        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");


        try {
            URL bookUrl;
            if (query == null || query.isEmpty()) {
                bookUrl = ApiUtil.buildUrl("Rust Language");
            } else {
                bookUrl = new URL(query);
            }

            new BooksQueryTask().execute(bookUrl);

        } catch (IOException e) {
            Toast.makeText(this, "Could not connect to the googleapis service!", Toast.LENGTH_LONG)
                    .show();
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        List<String> recentQueries = SharedPreferencesUtil.getAll(getApplicationContext());

        for (int i = 0; i < recentQueries.size(); i++) {
            menu.add(Menu.NONE, i, Menu.NONE, recentQueries.get(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId() + 1;
                String prefName = SharedPreferencesUtil.QUERY + position;
                String query = SharedPreferencesUtil.getString(getApplicationContext(), prefName);

                String[] prefParams = query.split("\\n");
                String[] queryParams = new String[4];

                System.arraycopy(prefParams, 0, queryParams, 0, prefParams.length);

                try {
                    URL bookUrl = ApiUtil.buildUrl(
                            queryParams[0] != null ? queryParams[0] : "",
                            queryParams[1] != null ? queryParams[1] : "",
                            queryParams[2] != null ? queryParams[2] : "",
                            queryParams[3] != null ? queryParams[3] : ""
                    );
                    new BooksQueryTask().execute(bookUrl);
                } catch (MalformedURLException e) {
                    Log.e("ERROR", "onOptionsItemSelected: " + e.getLocalizedMessage(), e);
                    e.printStackTrace();
                }

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL url = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(url);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_occurred_msg), Toast.LENGTH_LONG)
                    .show();
            Log.e("ERROR", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("ERROR", "onQueryTextSubmit: " + e.getLocalizedMessage(), e);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
            rvBooks.setVisibility(View.INVISIBLE);
            tvGenericMsg.setVisibility(View.INVISIBLE);
            fetchingPgBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            if (s == null) {
                tvGenericMsg.setText(R.string.error_occurred_msg);
                tvGenericMsg.setVisibility(View.VISIBLE);
            } else {
                try {
                    List<Book> books = ApiUtil.getBookList(s);

                    if (books.size() > 0) {
                        BooksAdapter adapter = new BooksAdapter(books);
                        rvBooks.setAdapter(adapter);
                        rvBooks.setVisibility(View.VISIBLE);
                    } else {
                        tvGenericMsg.setText(R.string.no_book_found_msg);
                        tvGenericMsg.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    tvGenericMsg.setText(R.string.no_book_found_msg);
                    tvGenericMsg.setVisibility(View.VISIBLE);
                }
            }

            fetchingPgBar.setVisibility(View.INVISIBLE);
        }
    }

}
