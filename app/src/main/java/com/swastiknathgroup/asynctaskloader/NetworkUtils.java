package com.swastiknathgroup.asynctaskloader;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkUtils {
private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
private static final String QUERY_PARAM = "q";
private static final String limit = "maxResults";
private static final String PRINT_TYPE = "printType";

static String getBookInfo(String querystring) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String bookJSONString = null;

try{
    Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAM, querystring)
            .appendQueryParameter(limit, "10")
            .appendQueryParameter(PRINT_TYPE, "books")
            .build();

    URL requestURL = new URL((builtURI.toString()));
    urlConnection = (HttpURLConnection)requestURL.openConnection();
    urlConnection.setRequestMethod("GET");
    urlConnection.connect();

    InputStream inputStream = urlConnection.getInputStream();
    reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

    StringBuilder builder = new StringBuilder();
    String line;
    while((line = reader.readLine()) != null){
        builder.append(line);
        builder.append("\n");
    }
    if(builder.length() == 0){
        return null;
    }
    bookJSONString = builder.toString();

}catch (IOException e){
    e.printStackTrace();
}finally {
  if(urlConnection!=null){
      urlConnection.disconnect();
  }
  if(reader !=null){
      try{
          reader.close();
      }catch (IOException e){e.printStackTrace();}
  }
}
    Log.d(LOG_TAG, bookJSONString);
    return bookJSONString;
}

}
