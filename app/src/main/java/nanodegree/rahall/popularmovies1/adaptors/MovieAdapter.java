package nanodegree.rahall.popularmovies1.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import nanodegree.rahall.popularmovies1.R;
import nanodegree.rahall.popularmovies1.activities.MovieDetails;
import nanodegree.rahall.popularmovies1.models.Movie;
import nanodegree.rahall.popularmovies1.models.Movies;
import nanodegree.rahall.popularmovies1.utilities.HttpHelper;

/**
 * Created by rahall on 8/29/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final LayoutInflater inflator;
    private Movies movies;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        inflator = LayoutInflater.from(context);
        movies = new Movies();

    }

    public void setMovies(Movies movies) {
        this.movies.getMovies().clear();
        this.movies.getMovies().addAll(movies.getMovies());
        this.notifyItemRangeInserted(0, movies.getMovies().size() - 1);


    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflator.from(context).inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movies.getMovies().get(position);

        if(((MovieViewHolder) holder).movieImage != null) {
            Picasso.with(context)
                    .load(HttpHelper.getImageSize185HttpRequest(movie.getPoster_path()))
                    .fit()

                    .into(((MovieViewHolder) holder).movieImage);
        }


    }

    @Override
    public int getItemCount() {
        return movies.getMovies().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieImage;
        LinearLayout movieImageLinearLayout;

        public MovieViewHolder(View view) {
            super(view);


            movieImageLinearLayout = (LinearLayout) view.findViewById(R.id.movie_image_linearlayout);
            movieImageLinearLayout.setOnClickListener(this);

            movieImage = (ImageView) view.findViewById(R.id.movie_image);


        }


        @Override
        public void onClick(View view) {
            Movie currentMovie = movies.getMovies().get(getAdapterPosition());
            if (view.getId() == R.id.movie_image_linearlayout) {
                Intent intentDetail = new Intent(view.getContext(), MovieDetails.class);
                intentDetail.putExtra(context.getString(R.string.release_date),currentMovie.getRelease_date());
                intentDetail.putExtra(context.getString(R.string.poster_image), currentMovie.getPoster_path());
                intentDetail.putExtra(context.getString(R.string.title), currentMovie.getTitle());
                intentDetail.putExtra(context.getString(R.string.vote_average),currentMovie.getVote_average());
                intentDetail.putExtra(context.getString(R.string.vote_count), currentMovie.getVote_count());
                intentDetail.putExtra(context.getString(R.string.id), currentMovie.getId());
                intentDetail.putExtra(context.getString(R.string.overview), currentMovie.getOverview());
                context.startActivity(intentDetail);



            }
        }
    }
}
