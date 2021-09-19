package com.fcemtopall.listpeople

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fcemtopall.listpeople.databinding.ActivityMainBinding
import com.fcemtopall.listpeople.entity.model.Person
import com.fcemtopall.listpeople.ui.adapter.PersonListAdapter

class MainActivity : AppCompatActivity() {

    var currentPage = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var personListAdapter: PersonListAdapter
    private var personList = mutableListOf<Person>()

    private val viewModel by lazy {
        ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // recycler view
        personListAdapter = PersonListAdapter(personList, viewModel)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = personListAdapter
        }

        // scroll bottom listener
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!binding.recyclerView.canScrollVertically(1)) {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.loadData()
                }
            }
        })

        binding.nestedScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    viewModel.loadData()
                }
            }

        })

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.cleanData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.triggerToast.observe(this, {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        })


        // first load
        viewModel.personList.observe(this,  {
            personListAdapter.setData(it.toMutableList())
            binding.progressBar.visibility = View.GONE
        })
    }

}