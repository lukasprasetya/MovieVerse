package com.example.movieverse.ui.favorite.movie

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieverse.R
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.databinding.ItemRowBinding
import com.example.movieverse.ui.detail.DetailMovieActivity
import com.example.movieverse.ui.detail.DetailType

class FavoriteMovieAdapter :
    PagedListAdapter<MovieEntity, FavoriteMovieAdapter.FavMovieViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMovieViewHolder {
        val itemRowBinding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavMovieViewHolder(itemRowBinding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieAdapter.FavMovieViewHolder, position: Int) {
        val movieEntity = getItem(position)
        if (movieEntity != null) {
            holder.bind(movieEntity)
        }
    }

    fun getSwipedData(swipedPosition: Int): MovieEntity? = getItem(swipedPosition)

    inner class FavMovieViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieEntity: MovieEntity) {
            with(binding) {
                tvTitleList.text = movieEntity.title
                tvRateList.text = movieEntity.voteAverage.toString()
                tvReleaseList.text = movieEntity.releaseDate


                Glide.with(itemView.context)
                    .load(itemView.context.getString(R.string.baseUrlImage, movieEntity.posterPath))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarList.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarList.visibility = View.GONE
                            return false
                        }
                    })
                    .into(posterList)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_TYPE, DetailType.MOVIE.ordinal)
                        putExtra(DetailMovieActivity.EXTRA_ID, movieEntity.id)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}