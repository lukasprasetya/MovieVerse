package com.example.movieverse.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieverse.R
import com.example.movieverse.databinding.ActivityFavoriteBinding
import com.example.movieverse.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val activityFavoriteBinding get() = _activityFavoriteBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(activityFavoriteBinding.root)
        viewPagerConfig()

        var actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
       finish()
        return true
    }

    private fun viewPagerConfig() {
        val sectionPagerAdaptor = FavSectionPagerAdaptor(this, supportFragmentManager)
        view_pager_fav.adapter = sectionPagerAdaptor
        tab_layout_fav.setupWithViewPager(view_pager_fav)

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}