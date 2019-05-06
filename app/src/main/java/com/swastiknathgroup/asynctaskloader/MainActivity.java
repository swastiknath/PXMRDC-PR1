package com.swastiknathgroup.asynctaskloader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
private EditText mBookInput;
private TextView mTitleText;
private TextView mAuthorText;
private Button mButton;
private TextView mpublisehr;
private TextView mpublisheddate;
private TextView mdescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookInput = (EditText)findViewById(R.id.bookinput);
        mAuthorText = (TextView)findViewById(R.id.authortext);
        mTitleText = (TextView)findViewById(R.id.title_text);
        mdescription = (TextView)findViewById(R.id.description);
        mpublisehr = (TextView)findViewById(R.id.publisher);
        mpublisheddate = (TextView)findViewById(R.id.published_date);
        mButton = (Button)findViewById(R.id.search_button);
        if(getSupportLoaderManager().getLoader(0) !=null){
           getSupportLoaderManager().initLoader(0, null, this);
        }

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBooks(v);
            }
        });
    }

    private void searchBooks(View v){
        String queryString = mBookInput.getText().toString();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("queryString", queryString);
        getSupportLoaderManager().restartLoader(0, queryBundle, this);
//        new FetchBook(mTitleText, mAuthorText).execute(queryString);
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String queryString = "";
        if(bundle !=null){
            queryString = bundle.getString("queryString");
        }
        return new BookLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            //phrasing and looping variables
            int i = 0;
            String title = null;
            String authors = null;
            String publication = null;
            String date= null;
            String description = null;
            String  pagenum = null;
            String category = null;


            while(i<itemsArray.length() && authors == null && title == null){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject Volumneinfo = book.getJSONObject("volumeInfo");
                try {
                    title = Volumneinfo.getString("title");
                    authors = Volumneinfo.getString("authors");
                    publication = Volumneinfo.getString("publisher");
                    date = Volumneinfo.getString("publishedDate");
                    description = Volumneinfo.getString("description");
                    pagenum = Volumneinfo.getString("pageCount");
                    category = Volumneinfo.getString("categories");

                }catch (JSONException e){e.printStackTrace();}

                i++;
            }

            if(title != null && authors != null){
                mTitleText.setText("Title: "+title);
                mAuthorText.setText("Authors: "+authors);
                mdescription.setText("What to look for: \n"+ description);
                mpublisehr.setText("Publication:\n "+publication);
                mpublisheddate.setText("Category: \n"+category);
            }else{
                mAuthorText.setText("404 NOT FOUND.");
                mTitleText.setText("No Results Found");
            }
        }catch (Exception e){
            e.printStackTrace();
            mTitleText.setText("500 INTERNAL SERVER ERROR");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
     //do nothing bro.
    }

}
