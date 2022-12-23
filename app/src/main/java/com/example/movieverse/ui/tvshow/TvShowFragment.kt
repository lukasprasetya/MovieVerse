package com.example.movieverse.ui.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.databinding.FragmentTvShowBinding
import com.example.movieverse.utils.SortUtils
import com.example.movieverse.viewmodel.ViewModelFactory
import com.example.movieverse.vo.Resource
import com.example.movieverse.vo.Status


class TvShowFragment : Fragment() {

    private var _fragmentTvShowsBinding: FragmentTvShowBinding? = null
    private val fragmentTvShowsBinding get() = _fragmentTvShowsBinding!!

    private lateinit var tvShowsAdapter: TvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentTvShowsBinding = FragmentTvShowBinding.inflate(layoutInflater, container, false)
        return fragmentTvShowsBinding.root
    }

    private lateinit var viewModel: TvShowViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[TvShowViewModel::class.java]

        tvShowsAdapter = TvShowAdapter()
        setList(SortUtils.NEWEST)

        with(fragmentTvShowsBinding.rvTvshowFragm) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }

        fragmentTvShowsBinding.random.setOnClickListener { setList(SortUtils.RANDOM) }
        fragmentTvShowsBinding.vote.setOnClickListener { setList(SortUtils.BEST_VOTE) }
        fragmentTvShowsBinding.newest.setOnClickListener { setList(SortUtils.NEWEST) }

    }

    private fun setList(sort: String) {
        viewModel.getTvShows(sort).observe(viewLifecycleOwner, tvShowsObserver)
    }

    private val tvShowsObserver = Observer<Resource<PagedList<MovieEntity>>> { tvSHows ->
        if (tvSHows != null) {
            when (tvSHows.status) {
                Status.LOADING -> {
                    fragmentTvShowsBinding.progressBarFragmTvshow.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    fragmentTvShowsBinding.progressBarFragmTvshow.visibility = View.GONE
                    tvShowsAdapter.submitList(tvSHows.data)
                    tvShowsAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    fragmentTvShowsBinding.progressBarFragmTvshow.visibility = View.GONE
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentTvShowsBinding = null
    }
}


