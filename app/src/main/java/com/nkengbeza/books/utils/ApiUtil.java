package com.nkengbeza.books.utils;

import android.net.Uri;
import android.util.Log;

import com.nkengbeza.books.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApiUtil {

    private ApiUtil() {
    }

    private static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String QUERY_PARAM = "q";
    private static final String TITLE_PARAM = "intitle:";
    private static final String AUTHOR_PARAM = "inauthor:";
    private static final String PUBLISHER_PARAM = "inpublisher:";
    private static final String ISBN_PARAM = "isbn:";

    public static URL buildUrl(String title) throws MalformedURLException {
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, title)
                .build();

        return new URL(uri.toString());
    }

    public static URL buildUrl(String title, String author, String publisher, String isbn) throws MalformedURLException {
        StringBuilder builder = new StringBuilder();
        if (!title.isEmpty()) builder.append(TITLE_PARAM).append(title).append("+");
        if (!author.isEmpty()) builder.append(AUTHOR_PARAM).append(author).append("+");
        if (!publisher.isEmpty()) builder.append(PUBLISHER_PARAM).append(publisher).append("+");
        if (!isbn.isEmpty()) builder.append(ISBN_PARAM).append(isbn);

        if (builder.length() > 0) {
            if (builder.charAt(builder.length() - 1) == '+') {
                builder.setLength(builder.length() - 1);
            }
        }


        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, builder.toString())
                .build();

        return new URL(uri.toString());
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);

        InputStream stream = null;
        Scanner scanner = null;

        try {
            stream = connection.getInputStream();
            scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("ERROR", String.format("Error reading data!!!\n%s", e.toString()));
            return null;

        } finally {
            if (stream != null) stream.close();
            if (scanner != null) scanner.close();
            connection.disconnect();
        }

    }

    public static List<Book> getBookList(String json) throws JSONException {
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "title";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBMAIL = "thumbnail";

        List<Book> books = null;

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);

            for (int i = 0; i < arrayBooks.length(); i++) {
                if (books == null) books = new ArrayList<>();

                JSONObject obj = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoObj = obj.getJSONObject(VOLUME_INFO);
                JSONArray authorsArrayObj = volumeInfoObj.isNull(AUTHORS) ? null : volumeInfoObj.getJSONArray(AUTHORS);
                JSONObject imgLinksObj = volumeInfoObj.isNull(IMAGE_LINKS) ? null : volumeInfoObj.getJSONObject(IMAGE_LINKS);

                List<String> authors = new ArrayList<>();

                if (authorsArrayObj != null) {
                    for (int j = 0; j < authorsArrayObj.length(); j++) {
                        authors.add(authorsArrayObj.get(j).toString());
                    }
                }

                books.add(new Book(
                        obj.getString(ID),
                        volumeInfoObj.getString(TITLE),
                        volumeInfoObj.isNull(SUBTITLE) ? null :volumeInfoObj.getString(SUBTITLE),
                        authors,
                        volumeInfoObj.isNull(PUBLISHER) ? null : volumeInfoObj.getString(PUBLISHER),
                        volumeInfoObj.isNull(PUBLISHED_DATE) ? null : volumeInfoObj.getString(PUBLISHED_DATE),
                        volumeInfoObj.isNull(DESCRIPTION) ? null : volumeInfoObj.getString(DESCRIPTION),
                        imgLinksObj == null || imgLinksObj.isNull(THUMBMAIL) ? null : imgLinksObj.getString(THUMBMAIL)
                ));
            }
        } catch (JSONException e) {
            Log.e("ERROR", "getBookList: " + e.getMessage(), e);
            throw e;
        }

        return books;
    }
}
