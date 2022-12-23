package com.example.movieverse.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.databinding.FragmentMovieBinding
import com.example.movieverse.utils.SortUtils
import com.example.movieverse.viewmodel.ViewModelFactory
import com.example.movieverse.vo.Resource
import com.example.movieverse.vo.Status

class MovieFragment : Fragment() {

    private var _fragmentMoviesBinding: FragmentMovieBinding? = null
    private val fragmentMovieBinding get() = _fragmentMoviesBinding!!

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentMoviesBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return fragmentMovieBinding.root
    }

    private lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        movieAdapter = MovieAdapter()
        setList(SortUtils.NEWEST)

        with(fragmentMovieBinding.rvMovieFragm) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        fragmentMovieBinding.random.setOnClickListener { setList(SortUtils.RANDOM) }
        fragmentMovieBinding.vote.setOnClickListener { setList(SortUtils.BEST_VOTE) }
        fragmentMovieBinding.newest.setOnClickListener { setList(SortUtils.NEWEST) }
    }

    private fun setList(sort: String) {
        viewModel.getMovies(sort).observe(viewLifecycleOwner, moviesObserver)
    }

    private val moviesObserver = Observer<Resource<PagedList<MovieEntity>>> { movies ->
        if (movies != null) {
            when (movies.status) {
                Status.LOADING -> {
                    fragmentMovieBinding.progressBarFragmMovie.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    fragmentMovieBinding.progressBarFragmMovie.visibility = View.GONE
                    movieAdapter.submitList(movies.data)
                    movieAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    fragmentMovieBinding.progressBarFragmMovie.visibility = View.GONE
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMoviesBinding = null
    }
}
