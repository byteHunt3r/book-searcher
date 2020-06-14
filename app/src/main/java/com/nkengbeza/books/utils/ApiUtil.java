package com.nkengbeza.books.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil {

    private ApiUtil() {}

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAM = "q";

    public static URL buildUrl(String title) throws MalformedURLException {
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, title)
                .build();

        return new URL(uri.toString());
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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
}
