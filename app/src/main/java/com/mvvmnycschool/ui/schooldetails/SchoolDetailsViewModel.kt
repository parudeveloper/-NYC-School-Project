package com.mvvmnycschool.ui.schooldetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvmnycschool.data.SchoolDetailsEntity
import com.mvvmnycschool.domine.HighSchoolListItem
import com.mvvmnycschool.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolDetailsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()
    private var highSchoolListItem: HighSchoolListItem? = null

    private var schoolDetails: MutableLiveData<SchoolDetailsEntity> = MutableLiveData()

    val loading: MutableLiveData<Boolean> get() = isLoading
    val data: MutableLiveData<SchoolDetailsEntity> get() = schoolDetails


    fun setSchool(school: HighSchoolListItem) {
        highSchoolListItem = school
        schoolDetails.value = SchoolDetailsEntity.fromSchoolListItem(school)

    }

    fun getSchoolDetails() {
        if (highSchoolListItem == null) return

        viewModelScope.launch {
            isLoading.postValue(true)
            try {
                val response = apiService.getAllSATScores(highSchoolListItem!!.dbn)
                if (response.isSuccessful) {
                    val scores = response.body()!!
                    if (scores.isEmpty()) {
                        isLoading.postValue(false)
                        return@launch
                    }
                    schoolDetails.postValue(
                        schoolDetails.value!!.copy(
                            satTestTakers = scores[0].numOfSatTestTakers.toIntOrNull(),
                            satCriticalReadingAvgScore = scores[0].satCriticalReadingAvgScore.toDoubleOrNull(),
                            satMathAvgScore = scores[0].satMathAvgScore.toDoubleOrNull(),
                            satWritingAvgScore = scores[0].satWritingAvgScore.toDoubleOrNull()
                        )
                    )
                }
                isLoading.postValue(false)

            } catch (e: Exception) {
                isLoading.postValue(false)
                return@launch
            }
        }


    }


}