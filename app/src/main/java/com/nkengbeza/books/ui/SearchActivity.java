package com.nkengbeza.books.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nkengbeza.books.MainActivity;
import com.nkengbeza.books.R;
import com.nkengbeza.books.utils.ApiUtil;
import com.nkengbeza.books.utils.SharedPreferencesUtil;

import java.net.MalformedURLException;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText etTitle = findViewById(R.id.etTitle);
        final EditText etAuthor = findViewById(R.id.etAuthor);
        final EditText etPublisher = findViewById(R.id.etPublisher);
        final EditText etIsbn = findViewById(R.id.etIsbn);

        final Button btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_search_data), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    // SharedPreferences
                    Context context = getApplicationContext();
                    int position = SharedPreferencesUtil.getInt(context, SharedPreferencesUtil.POSITION);
                    position = position == 0 || position == 5 ? 1 : position + 1;
                    String key = SharedPreferencesUtil.QUERY + position;
                    String value = title + "\n" + author + "\n" + publisher + "\n" + isbn;

                    SharedPreferencesUtil.setString(context, key, value);
                    SharedPreferencesUtil.setInt(context, SharedPreferencesUtil.POSITION, position);

                    try {
                        intent.putExtra("query", ApiUtil.buildUrl(title, author, publisher, isbn).toString());
                        startActivity(intent);
                    } catch (MalformedURLException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_occurred_msg), Toast.LENGTH_LONG)
                                .show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
