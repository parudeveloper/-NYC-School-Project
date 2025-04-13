package com.mvvmnycschool.ui.schoollist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvmnycschool.domine.HighSchoolListItem
import com.mvvmnycschool.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolListViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private var isLoading = MutableLiveData<Boolean>()
    private val _hsMutableList = MutableLiveData<ArrayList<HighSchoolListItem>?>()
    val highSchoolListLiveData: LiveData<ArrayList<HighSchoolListItem>?> get() = _hsMutableList

    private var fetchedData: ArrayList<HighSchoolListItem>? = null


    fun getHighSchoolFromNetwork() {
        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val response =
                    apiService.getAllHighSchools() // avoid fetching data again if already fetched
                fetchedData = if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
                _hsMutableList.postValue(fetchedData)
                isLoading.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading.postValue(false)
                _hsMutableList.postValue(null)
            }
        }
    }


}