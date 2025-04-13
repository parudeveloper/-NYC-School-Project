package com.mvvmnycschool.ui.schoollist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mvvmnycschool.databinding.FragmentSchoolBinding
import com.mvvmnycschool.domine.HighSchoolListItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolFragment : Fragment() {
    private val TAG = "SchoolFragment"
    lateinit var binding: FragmentSchoolBinding
    lateinit var schoolListViewModel: SchoolListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchoolBinding.inflate(layoutInflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        schoolListViewModel = ViewModelProvider(this).get(SchoolListViewModel::class)
        schoolListViewModel.highSchoolListLiveData.observe(
            viewLifecycleOwner,
            Observer { response ->
                /* response?.let {
                     makeAdapterCallToDisplayItems(response)
                     Log.d(TAG, "initViews: ${response.size}")
                 }*/
                binding.apply {
                    if (response != null) {
                        // recyclerview
                        val adapter = MySchoolRecyclerViewAdapter(response) {
                            showSchoolDetails(it)
                        }
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE

                        // toolbar buttons
                        refresh.visibility = View.VISIBLE
                        savedSchools.visibility = View.VISIBLE
                        // progress bar and error text
                        progressBar.visibility = View.GONE
                        errorText.visibility = View.GONE

                    } else {
                        // recyclerview
                        recyclerView.visibility = View.INVISIBLE
                        // toolbar buttons
                        refresh.visibility = View.VISIBLE
                        savedSchools.visibility = View.VISIBLE
                        // progress bar and error text
                        errorText.visibility = View.VISIBLE
                        errorText.text = "Error loading schools"
                        progressBar.visibility = View.GONE
                    }
                }
            })

        schoolListViewModel.getHighSchoolFromNetwork()
        binding.schoolListToolbar.title = "NYC High Schools"
    }

    private fun showSchoolDetails(highSchoolListItem: HighSchoolListItem) {
        SchoolFragmentDirections.actionSchoolFragmentToSchoolDetails(highSchoolListItem)
            .also { findNavController().navigate(it) }

    }
}