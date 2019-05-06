package com.swastiknathgroup.asynctaskloader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
private WeakReference<TextView> mTitleText;
private WeakReference<TextView> mAuthorText;

    FetchBook(TextView titletext, TextView authortext){
        this.mAuthorText= new WeakReference<>(authortext);
        this.mTitleText = new WeakReference<>(titletext);
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = NetworkUtils.getBookInfo(strings[0]);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            //phrasing and looping variables
            int i = 0;
            String title = null;
            String authors = null;
            while(i<itemsArray.length() && authors == null && title == null){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject Volumneinfo = book.getJSONObject("volumeInfo");
                try {
                    title = Volumneinfo.getString("title");
                    authors = Volumneinfo.getString("authors");
                }catch (JSONException e){e.printStackTrace();}

                i++;
            }

            if(title != null && authors != null){
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            }else{
                mAuthorText.get().setText("404 NOT FOUND.");
                mTitleText.get().setText("No Results Found");
            }
        }catch (Exception e){
            e.printStackTrace();
            mTitleText.get().setText("500 INTERNAL SERVER ERROR");
        }
    }
}
