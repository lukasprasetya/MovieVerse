package com.example.movieverse.ui.favorite.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieverse.R
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.databinding.FragmentFavoriteTvShowBinding
import com.example.movieverse.ui.favorite.FavoriteMovieViewModel
import com.example.movieverse.utils.SortUtils
import com.example.movieverse.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteTvShowFragment : Fragment() {

    private var _fragmentFavTvShowBinding: FragmentFavoriteTvShowBinding? = null
    private val fragmentFavTvShowBinding get() = _fragmentFavTvShowBinding!!

    private lateinit var tvShowAdapter: FavoriteTvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFavTvShowBinding =
            FragmentFavoriteTvShowBinding.inflate(inflater, container, false)
        return fragmentFavTvShowBinding.root
    }

    private lateinit var viewModel: FavoriteMovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(fragmentFavTvShowBinding.rvFavTvshowFragm)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[FavoriteMovieViewModel::class.java]

        tvShowAdapter = FavoriteTvShowAdapter()

        fragmentFavTvShowBinding.progressBarFragmFavTvshow.visibility = View.VISIBLE
        setList(SortUtils.RANDOM)

        with(fragmentFavTvShowBinding.rvFavTvshowFragm) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowAdapter
        }

        fragmentFavTvShowBinding.random.setOnClickListener { setList(SortUtils.RANDOM) }
        fragmentFavTvShowBinding.vote.setOnClickListener { setList(SortUtils.BEST_VOTE) }
        fragmentFavTvShowBinding.newest.setOnClickListener { setList(SortUtils.NEWEST) }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val tvShowEntity = tvShowAdapter.getSwipedData(swipedPosition)
                tvShowEntity?.let { viewModel.setFavorite(it) }
                val snackbar = Snackbar.make(view as View, R.string.undo_msg, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.yes_msg) { _ ->
                    tvShowEntity?.let { viewModel.setFavorite(it) }
                }
                snackbar.show()
            }
        }
    })

    private fun setList(sort: String) {
        viewModel.getFavoriteTvShows(sort).observe(viewLifecycleOwner, tvShowObserver)
    }

    private val tvShowObserver = Observer<PagedList<MovieEntity>> { tvShow ->
        if (tvShow.isNotEmpty()) {
            fragmentFavTvShowBinding.progressBarFragmFavTvshow.visibility = View.GONE
            tvShowAdapter.submitList(tvShow)
            tvShowAdapter.notifyDataSetChanged()
        } else {
            fragmentFavTvShowBinding.progressBarFragmFavTvshow.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentFavTvShowBinding = null
    }
}
