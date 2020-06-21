package com.nkengbeza.books.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nkengbeza.books.R;
import com.nkengbeza.books.model.Book;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {


    private List<Book> books;

    public BooksAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new BooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvPublishedDate;
        TextView tvPublisher;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvAuthors = itemView.findViewById(R.id.tvAuthors);
            this.tvPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
            this.tvPublisher = itemView.findViewById(R.id.tvPublisher);

            itemView.setOnClickListener(this);
        }

        public void bind(Book book) {
            tvTitle.setText(book.getTitle());

            tvAuthors.setText(book.getAuthors());
            tvPublishedDate.setText(book.getPublishedDate() != null ? book.getPublishedDate() : "");
            tvPublisher.setText(book.getPublisher() != null ? book.getPublisher() : "");
        }


        @Override
        public void onClick(View v) {
            Book book = books.get(this.getAdapterPosition());
            Intent intent = new Intent(v.getContext(), BookDetail.class);
            intent.putExtra("book", book);
            v.getContext().startActivity(intent);

        }

    }

}

