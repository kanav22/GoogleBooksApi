/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.bookListing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bookListing.R.id.keyword;

public class BookActivity extends AppCompatActivity {
    public static final String LOG_TAG = BookActivity.class.getName();
    static final String SEARCH_RESULTS = "" + R.string.searchresults;
    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";
    String rKeyword = "";
    EditText keywordEditText;
    String queryUrl;
    private TextView mEmptyStateTextView;
    private bookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);
        ListView bookListView = (ListView) findViewById(R.id.list);
        Parcelable state = bookListView.onSaveInstanceState();

        // Create a new {@link ArrayAdapter} of books
        mAdapter = new bookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);
        bookListView.onRestoreInstanceState(state);
        keywordEditText = (EditText) findViewById(keyword);
        Button searchButton = (Button) findViewById(R.id.search);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected()) {
                    rKeyword = keywordEditText.getText().toString();
                    queryUrl = "https://www.googleapis.com/books/v1/volumes?q=" + rKeyword + "&maxResults=10";
                    new BookAsyncTask().execute(queryUrl);

                } else {
                    Toast.makeText(BookActivity.this, R.string.Nointernet_message,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (savedInstanceState != null) {
            Book[] books = (Book[]) savedInstanceState.getParcelableArray(SEARCH_RESULTS);
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Book[] books = new Book[mAdapter.getCount()];
        for (int i = 0; i < books.length; i++) {
            books[i] = mAdapter.getItem(i);
        }
        outState.putParcelableArray(SEARCH_RESULTS, (Parcelable[]) books);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.fetchDataFromUrl(queryUrl);
            return result;

        }

        @Override
        protected void onPostExecute(List<Book> books) {
            mEmptyStateTextView.setText(R.string.no_books_found);
            mAdapter.clear();
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }

        }
    }
}



