package com.avelycure.movie_info.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.IInstantiator
import com.avelycure.core_navigation.NavigationConstants
import com.avelycure.core_navigation.NavigationConstants.GET_MORE_INFO
import com.avelycure.core_navigation.NavigationConstants.LOAD_IMAGES
import com.avelycure.core_navigation.NavigationConstants.NAVIGATOR
import com.avelycure.core_navigation.Navigator
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.constants.MovieConstants.DEFAULT_MOVIE_ID
import com.avelycure.domain.constants.MovieConstants.MOVIE_ID
import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.models.formatters.getNiceCast
import com.avelycure.domain.models.formatters.getNiceCompanies
import com.avelycure.domain.models.formatters.getNiceCountries
import com.avelycure.domain.models.formatters.getNiceGenres
import com.avelycure.movie_info.R
import com.avelycure.movie_info.presentation.adapters.MovieImagesAdapter
import com.avelycure.movie_info.presentation.adapters.SimilarMoviesAdapter
import com.avelycure.movie_info.utils.getMoney
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.Serializable

@AndroidEntryPoint
class MovieInfoFragment : Fragment() {

    companion object Instantiator : IInstantiator {
        private const val tag = "MOVIE_INFO"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val movieInfoFragment = MovieInfoFragment()
            movieInfoFragment.arguments = bundle
            return movieInfoFragment
        }
    }

    private var loadImage: (String, ImageView) -> Unit = { _, _ -> }
    private var openMovieInfo: (Int) -> Unit = { _ -> }

    private var movieId: Int = DEFAULT_MOVIE_ID

    private val movieInfoViewModel: MovieInfoViewModel by viewModels()

    private lateinit var movieImagesAdapter: MovieImagesAdapter
    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter

    private lateinit var ivPoster: AppCompatImageView
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvTagline: AppCompatTextView
    private lateinit var ratingBar: AppCompatRatingBar
    private lateinit var tvReviews: AppCompatTextView
    private lateinit var tvCastTitle: AppCompatTextView
    private lateinit var tvCast: AppCompatTextView
    private lateinit var tvGenresTitle: AppCompatTextView
    private lateinit var tvGenres: AppCompatTextView
    private lateinit var tvCountriesTitle: AppCompatTextView
    private lateinit var tvCountries: AppCompatTextView
    private lateinit var tvBudget: AppCompatTextView
    private lateinit var tvBudgetTitle: AppCompatTextView
    private lateinit var tvRevenue: AppCompatTextView
    private lateinit var tvRevenueTitle: AppCompatTextView
    private lateinit var tvCompaniesTitle: AppCompatTextView
    private lateinit var tvCompanies: AppCompatTextView
    private lateinit var tvOverview: AppCompatTextView
    private lateinit var rvMovieImages: RecyclerView
    private lateinit var rvSimilarMovies: RecyclerView

    private lateinit var compas: Navigator

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_info, container, false)
        movieId = arguments?.getInt(MOVIE_ID, DEFAULT_MOVIE_ID) ?: DEFAULT_MOVIE_ID

        loadImage =
            arguments?.getSerializable(LOAD_IMAGES) as? (String, ImageView) -> Unit ?: { _, _ -> }

        openMovieInfo =
            arguments?.getSerializable(GET_MORE_INFO) as? (Int) -> Unit ?: { _ -> }

        compas = (arguments?.getSerializable(NavigationConstants.NAVIGATOR) as? Navigator)!!

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements(view)
        initRecyclerView()
        lifecycleScope.launchWhenStarted {
            movieInfoViewModel.state.collect { state ->
                setUI(state.movieInfo, state.images, state.similar)

                if (state.videoIsAvailable && !state.videoIsUploaded) {
                    childFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.youtube_container,
                            YTFragment.getInstance(state.videoInfo.key)
                        )
                        .commit()
                    movieInfoViewModel.onTrigger(MovieInfoEvents.VideoIsUploaded)
                }
            }
        }
        movieInfoViewModel.onTrigger(MovieInfoEvents.OnOpenInfoFragment(movieId))
    }

    private fun setUI(movieInfo: MovieInfo, images: List<String>, similar: List<Movie>) {
        loadImage(RequestConstants.IMAGE + movieInfo.posterPath, ivPoster)
        tvTitle.text = movieInfo.title

        tvTagline.text = movieInfo.tagline

        ratingBar.rating = movieInfo.voteAverage

        tvReviews.text = movieInfo.voteCount.toString()

        tvGenresTitle.text = getString(R.string.tgenres)
        tvGenres.text = movieInfo.getNiceGenres()

        tvCountriesTitle.text = getString(R.string.tcountries)
        tvCountries.text = movieInfo.getNiceCountries()

        tvCompaniesTitle.text = getString(R.string.tcompanies)
        tvCompanies.text = movieInfo.getNiceCompanies()

        tvBudgetTitle.text = getString(R.string.tbudget)
        tvBudget.text = getMoney(movieInfo.budget)

        tvRevenueTitle.text = getString(R.string.trevenue)
        tvRevenue.text = getMoney(movieInfo.revenue)

        tvOverview.text = movieInfo.overview

        tvCastTitle.text = getString(R.string.tcast)
        tvCast.text = movieInfo.getNiceCast()

        movieImagesAdapter.imagesList = images
        similarMoviesAdapter.similarMovies = similar
    }

    private fun initViewElements(view: View) {
        ivPoster = view.findViewById(R.id.mi_poster)
        tvTitle = view.findViewById(R.id.mi_title)
        tvOverview = view.findViewById(R.id.mi_overview)
        tvTagline = view.findViewById(R.id.mi_tagline)
        tvReviews = view.findViewById(R.id.mi_reviews)
        tvGenresTitle = view.findViewById(R.id.mi_genres_title)
        tvGenres = view.findViewById(R.id.mi_genres)
        ratingBar = view.findViewById(R.id.mi_ratingbar)
        tvCountriesTitle = view.findViewById(R.id.mi_countries_title)
        tvCountries = view.findViewById(R.id.mi_countries)
        tvBudget = view.findViewById(R.id.mi_budget)
        tvBudgetTitle = view.findViewById(R.id.mi_budget_title)
        tvRevenueTitle = view.findViewById(R.id.mi_revenue_title)
        tvRevenue = view.findViewById(R.id.mi_revenue)
        tvCompaniesTitle = view.findViewById(R.id.mi_companies_title)
        tvCompanies = view.findViewById(R.id.mi_companies)
        tvCast = view.findViewById(R.id.mi_cast)
        tvCastTitle = view.findViewById(R.id.mi_cast_title)
        rvMovieImages = view.findViewById(R.id.mi_rv_images)
        rvSimilarMovies = view.findViewById(R.id.mi_rv_similar_movies)
    }

    private fun initRecyclerView() {
        movieImagesAdapter = MovieImagesAdapter(loadImage)
        rvMovieImages.adapter = movieImagesAdapter
        rvMovieImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        similarMoviesAdapter = SimilarMoviesAdapter(loadImage) { id ->
            compas.add(
                "MOVIES",
                MovieInfoFragment.Instantiator.tag,
                Bundle().apply {
                    putInt(MOVIE_ID, id)

                    putSerializable(
                        LOAD_IMAGES, loadImage as Serializable
                    )

                    putSerializable(NAVIGATOR, compas)
                }
            )
        }
        rvSimilarMovies.adapter = similarMoviesAdapter
        rvSimilarMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

}