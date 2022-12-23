package com.example.movieverse.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieverse.R
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.databinding.ActivityDetailMovieBinding
import com.example.movieverse.viewmodel.ViewModelFactory
import com.example.movieverse.vo.Resource
import com.example.movieverse.vo.Status
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "exytra_type"
        const val EXTRA_ID = "extra_id"
        const val TITLE = "title"
    }

    private lateinit var activityDetailMovieBinding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        activityDetailMovieBinding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(activityDetailMovieBinding.root)

        val type = intent.getIntExtra(EXTRA_TYPE, -1)
        val enumType: DetailType = DetailType.values()[type]

        val id = intent.getIntExtra(EXTRA_ID, -1)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailMovieViewModel::class.java]

        activityDetailMovieBinding.progressBarDetail.visibility = View.VISIBLE
        activityDetailMovieBinding.nestedScrollView.visibility = View.GONE

        when (enumType) {
            DetailType.MOVIE -> {
                viewModel.selectedMovieId(id)
                viewModel.getMovieDetail.observe(this, { movie ->
                    if (movie != null) {
                        showDetail(movie)
                    }
                })
            }
            DetailType.TVSHOW -> {
                viewModel.selectedTvShowId(id)
                viewModel.getTvShowDetail.observe(this, { tvShow ->
                    if (tvShow != null) {
                        showDetail(tvShow)
                    }
                })
            }
        }

        activityDetailMovieBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        // activityDetailMovieBinding.appBar.addOnOffsetChangedListener(this)
        activityDetailMovieBinding.shareButton.setOnClickListener { share() }
        activityDetailMovieBinding.favoriteButton.setOnClickListener {
            when (enumType) {
                DetailType.MOVIE -> {
                    viewModel.setFavoriteMovie()
                }
                DetailType.TVSHOW -> {
                    viewModel.setFavoriteTvShow()
                }
            }
        }

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapseLayout.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapseLayout)) {
                toolbar.apply {
                    setBackgroundColor(Color.WHITE)
                    title = intent.getStringExtra(TITLE)
                    visibility = View.VISIBLE
                    setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                }
                collapseLayout.setCollapsedTitleTextColor(Color.BLACK)
            } else {
                toolbar.apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    toolbar.visibility = View.VISIBLE
                    setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                }
                collapseLayout.setExpandedTitleColor(Color.TRANSPARENT)
            }
        })
    }

    private fun share() {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder.from(this).apply {
            setType(mimeType)
            setChooserTitle(getString(R.string.share_title))
            setText(getString(R.string.share_text))
            startChooser()
        }
    }

    private fun showDetail(movie: Resource<MovieEntity>) {
        when (movie.status) {
            Status.LOADING ->
                activityDetailMovieBinding.progressBarDetail.visibility = View.VISIBLE
            Status.SUCCESS ->
                if (movie.data != null) {
                    activityDetailMovieBinding.progressBarDetail.visibility = View.GONE
                    activityDetailMovieBinding.nestedScrollView.visibility = View.VISIBLE

                    val state = movie.data.favorite

                    setFavoriteState(state)
                    populateMovieDetail(movie.data)
                }
            Status.ERROR -> {
                activityDetailMovieBinding.progressBarDetail.visibility = View.GONE
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFavoriteState(state: Boolean) {
        if (state) {
            activityDetailMovieBinding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_favorite_24
                )
            )
        } else {
            activityDetailMovieBinding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_favorite_border_24
                )
            )
        }
    }

    private fun populateMovieDetail(movieEntity: MovieEntity) {
        with(activityDetailMovieBinding) {
            tvTitleDetail.text = movieEntity.title
            tvRateDetail.text = movieEntity.voteAverage.toString()
            tvReleaseDetail.text = movieEntity.releaseDate
            tvDescDetail.text = movieEntity.overview


            Glide.with(this@DetailMovieActivity)
                .load(getString(R.string.baseUrlImage, movieEntity.backdropPath))
                .into(imgHeaderDetail)
            imgHeaderDetail.tag = movieEntity.backdropPath

            Glide.with(this@DetailMovieActivity)
                .load(getString(R.string.baseUrlImage, movieEntity.posterPath))
                .into(imgPosterDetail)
            imgPosterDetail.tag = movieEntity.posterPath

            activityDetailMovieBinding.collapseLayout.title = movieEntity.title
        }
    }
}
