package nanodegree.rahall.popularmovies1.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import nanodegree.rahall.popularmovies1.MovieApplication;
import nanodegree.rahall.popularmovies1.R;
import nanodegree.rahall.popularmovies1.models.MovieDetail;
import nanodegree.rahall.popularmovies1.utilities.CustomIntents;
import nanodegree.rahall.popularmovies1.utilities.DelegateNetworkAccess;
import nanodegree.rahall.popularmovies1.utilities.HttpHelper;

public class MovieDetails extends AppCompatActivity {
    TextView mMovieTitle;
    ImageView mMovieImage;
    TextView mMovieLength;
    TextView mRevenue;
    TextView mHomePage;
    TextView mReleaseDate;
    TextView mVoteAverage;
    TextView mVoteCount;
    TextView mSynopsys;
    Context mContext;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        createViews();
        populateViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                CustomIntents.DOWNLOAD_DETAIL_COMPLETE));

    registerReceiver(receiver_error, new IntentFilter(
            CustomIntents.DOWNLOAD_ERROR));
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
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createViews() {
         mMovieTitle = (TextView) findViewById(R.id.movie_title);;
         mMovieImage = (ImageView) findViewById(R.id.movie_image);
         mMovieLength = (TextView) findViewById(R.id.movie_length);
         mRevenue = (TextView) findViewById(R.id.revenue);
         mHomePage = (TextView) findViewById(R.id.home_page);
         mReleaseDate = (TextView) findViewById(R.id.release_date);
         mVoteAverage = (TextView) findViewById(R.id.vote_average);
         mVoteCount = (TextView) findViewById(R.id.vote_count);
         mSynopsys = (TextView) findViewById(R.id.synopsys);
         mContext = this;


    }
    public void populateViews() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            mMovieTitle.setText(extras.getString(mContext.getString(R.string.title)));
            mReleaseDate.setText(getString(R.string.release_date_string) + extras.getString(mContext.getString(R.string.release_date),""));
            mVoteAverage.setText(getString(R.string.average_rating) + extras.getString(mContext.getString(R.string.vote_average),""));
            mVoteCount.setText(getString(R.string.reviews) + extras.getString(mContext.getString(R.string.vote_count), ""));
            mSynopsys.setText(extras.getString(mContext.getString(R.string.overview), ""));
            String id = extras.getString(mContext.getString(R.string.id), "");
            Picasso.with(mContext)
                    .load(HttpHelper.getImageSize185HttpRequest(extras.getString(mContext.getString(R.string.poster_image),"")))
                    .fit()
                    .into(mMovieImage);
            DelegateNetworkAccess.getMovies(getApplicationContext(), MovieApplication.getInstance().getSortPreference(), id);




        }
    }
    public void gotoLink(View view) {
        MovieDetail movieDetail = MovieApplication.getInstance().getMovieDetail();
        String url= movieDetail.getHomePage();
        Uri webPageUri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, webPageUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (CustomIntents.DOWNLOAD_DETAIL_COMPLETE.equals(action)) {

                MovieDetail movieDetail = MovieApplication.getInstance().getMovieDetail();
                mMovieLength.setText(movieDetail.getRuntime() + getString(R.string.minutes));
                long revenueLong = Long.parseLong(movieDetail.getRevenue());
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                String revenueNumberString = numberFormat.format(revenueLong);
                revenueNumberString = revenueNumberString.substring(0,revenueNumberString.lastIndexOf("."));
                String revenue = getString(R.string.movie_revenue) + revenueNumberString;

                mRevenue.setText(revenue);
                mHomePage.setText(movieDetail.getHomePage());



            }
        }
    };

    BroadcastReceiver receiver_error = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (CustomIntents.DOWNLOAD_ERROR.equals(action)) {


                mRevenue.setVisibility(View.GONE);

                mHomePage.setVisibility(View.GONE);



            }
        }
    };
}
