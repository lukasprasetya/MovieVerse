package com.example.movieverse.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.movieverse.R
import com.example.movieverse.databinding.ActivityHomeBinding
import com.example.movieverse.ui.favorite.FavoriteActivity
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        carousel_home.pageCount = carouselPoster.size
        carousel_home.setImageListener(posterListener)

        val sectionPagerAdaptor = SectionPagerAdaptor(this, supportFragmentManager)
        activityHomeBinding.viewPagerHome.adapter = sectionPagerAdaptor
        activityHomeBinding.tabLayoutHome.setupWithViewPager(activityHomeBinding.viewPagerHome)
    }

    val carouselPoster = intArrayOf(
        R.drawable.header_crimes,
        R.drawable.header_mary_queen,
        R.drawable.header_star_is_born,
        R.drawable.header_aquaman,
        R.drawable.header_family,
        R.drawable.header_gotham
    )

    val posterListener = ImageListener { position, imageView ->
        imageView.setImageResource(carouselPoster[position])
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_fav) {
            startActivity(Intent(this, FavoriteActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }
}