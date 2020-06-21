package com.nkengbeza.books.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.nkengbeza.books.R;
import com.nkengbeza.books.databinding.ActivityBookDetailBinding;
import com.nkengbeza.books.model.Book;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Book book = getIntent().getParcelableExtra("book");
        ActivityBookDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail);
        binding.setBook(book);
    }
}
