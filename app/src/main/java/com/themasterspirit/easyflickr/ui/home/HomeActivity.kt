package com.themasterspirit.easyflickr.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.FlickrAndroidViewModelFactory
import com.themasterspirit.easyflickr.utils.Loading
import com.themasterspirit.easyflickr.utils.Success
import kotlinx.android.synthetic.main.activity_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinContext
import org.kodein.di.android.ActivityRetainedScope
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.*

class HomeActivity : BaseActivity() {

    override val kodeinContext: KodeinContext<*> = kcontext(this@HomeActivity)
    override val kodein: Kodein by retainedKodein {
        extend(parentKodein)

        bind<HomeViewModel>() with scoped(ActivityRetainedScope<HomeActivity>()).singleton {
            FlickrAndroidViewModelFactory(application) {
                HomeViewModel(application, instance())
            }.create(HomeViewModel::class.java)
        }

        bind<HomeViewModel>() with provider {
            FlickrAndroidViewModelFactory(application) {
                HomeViewModel(application, instance())
            }.create(HomeViewModel::class.java)
        }
    }

    private val viewModel: HomeViewModel by instance()

    private val adapter by lazy { PhotoAdapter() }
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(application, 2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        initObservers()

        if (adapter.items.isEmpty()) viewModel.refreshPhotos()
    }

    private fun initViews() {
        swipeRefreshLayout.setOnRefreshListener { viewModel.refreshPhotos() }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.recentPhotos.observe(this, Observer { data ->
            when (data) {
                is Loading -> {
                    if (data.loading) {
                        if (adapter.items.isEmpty()) {
                        } else {
                            swipeRefreshLayout.isRefreshing = true
                        }
                    } else {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
                is Success -> {
                    adapter.items.clear()
                    adapter.items.addAll(data.result)
                    adapter.notifyDataSetChanged()
                }
                is Failure -> {
                    val error = data.error ?: getString(R.string.message_default_error)
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}
