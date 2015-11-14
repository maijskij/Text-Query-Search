package search.android.aboutall.eu.search;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Searcher sSearcher;

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;

    private SearchArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mEmptyView = (TextView) findViewById(R.id.emptyView);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new SearchArrayAdapter(new ArrayList<String>());
        mRecyclerView.setAdapter(mAdapter);

        if (sSearcher == null) {
            sSearcher = new Searcher();
            new DictionaryLoader().execute();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            updateResults(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        // handle querry on each change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Instructions: user only being able to enter numbers
                if (!Utils.isNumber(Utils.getLastChar(newText))){
                    searchView.setQuery(newText.substring(0, newText.length() - 1),false);
                }else {
                    updateResults(newText);
                }
                return true;
            }
        });

        // set "only numbers" keyboard
        searchView.setInputType(InputType.TYPE_CLASS_PHONE);

        // get my MenuItem with placeholder submenu
        MenuItem searchMenuItem = menu.findItem( R.id.search );
        // Expand the search menu item in order to show by default the query
        searchMenuItem.expandActionView();


        return true;
    }

    private void updateResults(String text){
        mAdapter.clear();
        List<String> data  = sSearcher.lookup(text);
        if (data != null) {
            mAdapter.addAll(data, text);
        }
        int visibility = data!=null && data.size()>0 ? View.GONE:View.VISIBLE;
        mEmptyView.setVisibility ( visibility );

    }

    private class DictionaryLoader extends AsyncTask<Void, Void, Void>{

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(MainActivity.this, "Loading dictionary...",
                    "Please wait", true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                sSearcher.loadDictionary(Utils.loadWords(getApplicationContext()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mProgressDialog != null){
                mProgressDialog.dismiss();
            }
        }
    }
}
