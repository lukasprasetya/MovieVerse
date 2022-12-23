package com.example.movieverse.ui.favorite.tvshow

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


class FavoriteTvShowAdapter :
    PagedListAdapter<MovieEntity, FavoriteTvShowAdapter.FavTvShowViewHolder>(DIFF_CALLBACK) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTvShowViewHolder {
        val itemCardListBinding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavTvShowViewHolder(itemCardListBinding)
    }

    override fun onBindViewHolder(
        holderTvShows: FavoriteTvShowAdapter.FavTvShowViewHolder,
        position: Int
    ) {
        val tvSHowEntity = getItem(position)
        if (tvSHowEntity != null) {
            holderTvShows.bind(tvSHowEntity)
        }
    }

    fun getSwipedData(swipedPosition: Int): MovieEntity? = getItem(swipedPosition)

    inner class FavTvShowViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShowEntity: MovieEntity) {
            with(binding) {
                tvTitleList.text = tvShowEntity.title
                tvRateList.text = tvShowEntity.voteAverage.toString()
                tvReleaseList.text = tvShowEntity.releaseDate

                Glide.with(itemView.context)
                    .load(
                        itemView.context.getString(
                            R.string.baseUrlImage,
                            tvShowEntity.posterPath
                        )
                    )
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
                        putExtra(DetailMovieActivity.EXTRA_TYPE, DetailType.TVSHOW.ordinal)
                        putExtra(DetailMovieActivity.EXTRA_ID, tvShowEntity.id)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}

