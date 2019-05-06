package com.swastiknathgroup.asynctaskloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class BookLoader extends AsyncTaskLoader<String> {
private String mQueryString;
    public BookLoader(@NonNull Context context, String QueryString) {
        super(context);
        mQueryString = QueryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String response = NetworkUtils.getBookInfo(mQueryString);
        return response;
    }


}
