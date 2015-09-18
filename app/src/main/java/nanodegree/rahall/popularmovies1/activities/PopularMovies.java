package nanodegree.rahall.popularmovies1.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import nanodegree.rahall.popularmovies1.MovieApplication;
import nanodegree.rahall.popularmovies1.R;
import nanodegree.rahall.popularmovies1.adaptors.MovieAdapter;
import nanodegree.rahall.popularmovies1.utilities.CustomIntents;
import nanodegree.rahall.popularmovies1.utilities.DelegateNetworkAccess;


public class PopularMovies extends AppCompatActivity {
   RecyclerView mMovieRecylcerView;
    MovieAdapter mMovieAdapter;
    Context mContext;
    public static final int NUM_COLS_PORTRAIT =2;
    public static final int NUM_COLS_LANDSCAPE=4;
    public static final int PORTRAIT = 1;
    public static final int LANDSCAPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        mContext = this;

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                CustomIntents.DOWNLOAD_COMPLETE));
        registerReceiver(receiver_error, new IntentFilter(
                CustomIntents.DOWNLOAD_ERROR));


       getMovies();

        mMovieRecylcerView= (RecyclerView) findViewById(R.id.movie_image_recyclerview);
        GridLayoutManager gridLayoutManager;
        if(mContext.getResources().getConfiguration().orientation == PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(mContext, NUM_COLS_PORTRAIT);
        } else {
            gridLayoutManager = new GridLayoutManager(mContext, NUM_COLS_LANDSCAPE);
        }
        mMovieRecylcerView.setLayoutManager(gridLayoutManager);
        mMovieRecylcerView.setHasFixedSize(true);
        mMovieAdapter= new MovieAdapter(mContext);
        mMovieRecylcerView.setAdapter(mMovieAdapter);


    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(receiver_error);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (MovieApplication.getInstance().getSortPreference().equals(getString(R.string.popularity))) {
            menu.findItem(R.id.action_sort_by_popularity).setChecked(true);
        } else {
            menu.findItem(R.id.action_sort_by_rating).setChecked(true);
        }
        if(MovieApplication.getInstance().getRequestType().equals(R.string.method_asynctask)) {
            menu.findItem(R.id.use_asyntask).setChecked(true);
        } else {
            menu.findItem(R.id.use_volley).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                if(item.isChecked()) {
                    item.setChecked(false);
                }else {
                    item.setChecked(true);
                }
                if (!MovieApplication.getInstance().getSortPreference().equals(getString(R.string.popularity))) {
                    MovieApplication.getInstance().setSortPreference(getString(R.string.popularity));
                    getMovies();

                }


                return true;
            case R.id.action_sort_by_rating:
                if(item.isChecked()) {
                    item.setChecked(false);
                }else {
                    item.setChecked(true);
                    if (!MovieApplication.getInstance().getSortPreference().equals(getString(R.string.ratings))) {
                        MovieApplication.getInstance().setSortPreference(getString(R.string.ratings));
                        getMovies();

                    }
                }
                return true;

            case R.id.use_volley:
                if(item.isChecked()) {
                    item.setChecked(false);
                }else {
                    item.setChecked(true);
                }
                if (!MovieApplication.getInstance().getRequestType().equals(getString(R.string.method_volley))) {
                    MovieApplication.getInstance().setRequestType(getString(R.string.method_volley));


                }


                return true;
            case R.id.use_asyntask:
                if(item.isChecked()) {
                    item.setChecked(false);
                }else {
                    item.setChecked(true);
                    if (!MovieApplication.getInstance().getRequestType().equals(getString(R.string.method_asynctask))) {
                        MovieApplication.getInstance().setRequestType(getString(R.string.method_asynctask));


                    }
                }
                return true;





            default:
                return super.onOptionsItemSelected(item);

        }


    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (CustomIntents.DOWNLOAD_COMPLETE.equals(action)) {



                mMovieAdapter.setMovies(MovieApplication.getInstance().getMovies());
                mMovieAdapter.notifyDataSetChanged();



                //reload all data
               // displayData();


            }
        }
    };

    BroadcastReceiver receiver_error = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (CustomIntents.DOWNLOAD_ERROR.equals(action)) {

                // start error intent
                gotoErrorActivity();




            }
        }
    };
    public void gotoErrorActivity() {
        Intent intent = new Intent(this,ErrorHandlerActivity.class);
        this.startActivity(intent);
    }

    public void getMovies() {
        DelegateNetworkAccess.getMovies(getApplicationContext(),MovieApplication.getInstance().getSortPreference());
    }

}
