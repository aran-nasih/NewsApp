package newsapp.blowapp.com.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private ListView listView;
    private NewsAdapter adapter;
    private ProgressBar progressBar;
    private TextView badResponse;
    private static int NEWS_LOADER_ID = 1;
    private String GUARDIAN_API = "http://content.guardianapis.com/search?order-by=newest&q=youtube&api-key=test";
    android.app.LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar_view);
        this.progressBar.setVisibility(View.GONE);
        this.badResponse = (TextView) findViewById(R.id.bad_response_textview);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(this.badResponse);
        this.adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(this.adapter);

        loaderManager = getLoaderManager();

        loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);

        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                updateNews();
            }
        };

        // schedule the task to run starting now and then every minute...
        timer.schedule(hourlyTask, 0l, 1000 * 60);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News news = adapter.getItem(position);
                Uri urlIntent = Uri.parse(news.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, urlIntent);
                startActivity(intent);
            }
        });

    }

    public void updateNews() {
        if (isNetworkAvailable(this)) {
            loaderManager = getLoaderManager();
            loaderManager.restartLoader(0, null, MainActivity.this);
        } else {
            badResponse.setText("no internet connection");
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsAsyncTaskLoader(this, GUARDIAN_API);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> newses) {
        progressBar.setVisibility(View.GONE);

        adapter.clear();
        if (newses == null || newses.isEmpty()) {
            badResponse.setText("No News Found");
            return;
        }
        adapter.addAll(newses);

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        adapter.clear();
    }
}
