package com.themasterspirit.easyflickr.ui.photo

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.activity_photo.*
import java.text.DateFormat


class PhotoActivity : BaseActivity() {

    private val flickrPhoto: FlickrPhoto by lazy {
        intent.getParcelableExtra<FlickrPhoto>(FlickrPhoto.TAG)
    }

//    private val placeholder: Bitmap by lazy {
//        intent.getParcelableExtra<Bitmap>("bitmap")
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_photo)
            toolbar.setNavigationIcon(R.drawable.ic_back_photo)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        Picasso.get()
                .load(flickrPhoto.link(FlickrPhoto.Companion.Size.MEDIUM))
//                .placeholder(BitmapDrawable(resources, placeholder))
                .into(ivPhoto)

        tvAuthor.text = flickrPhoto.ownerName
        tvDate.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(flickrPhoto.dateUpload)

        toolbar.title = flickrPhoto.title

        ivPhoto.setOnClickListener {
            if (tvAuthor.visibility == View.VISIBLE) hideUi() else showUi()
        }
    }

    private fun hideUi() {
        hideSystemUi()
        tvAuthor.visibility = View.GONE
        tvDate.visibility = View.GONE
        toolbar.visibility = View.GONE
    }

    private fun showUi() {
        showSystemUi()
        tvAuthor.visibility = View.VISIBLE
        tvDate.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE

    }

    /**
     * Enables regular immersive mode.
     * For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
     * Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
     */
    private fun hideSystemUi() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    /**
     * Shows the system bars by removing all the flags
     * except for the ones that make the content appear under the system bars.
     */
    private fun showSystemUi() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}