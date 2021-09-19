package com.fcemtopall.listpeople

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcemtopall.listpeople.entity.model.FetchError
import com.fcemtopall.listpeople.entity.model.FetchResponse
import com.fcemtopall.listpeople.entity.model.Person

class MainViewModel : ViewModel() {

    private var currentPage = 1
    private val _triggerToast = MutableLiveData<String>()
    private val _personList = MutableLiveData<List<Person>>()

    val triggerToast = _triggerToast
    val personList = _personList

    init {
        loadData()
    }

    fun loadData() {
        DataSource().fetch(currentPage.toString(), object : FetchCompletionHandler {
            override fun invoke(fetchResponse: FetchResponse?, fetchError: FetchError?) {
                fetchError?.let { error ->
                    _triggerToast.postValue(error.errorDescription)
                }

                fetchResponse?.let { response ->
                    val currentList = mutableListOf<Person>()
                    _personList.value?.let { currentList.addAll(it) }
                    currentList.addAll(response.people)

                    val sortedList = currentList.sortedWith(compareBy { it.id }).distinct()

                    _personList.postValue(sortedList)
                    response.next?.let { currentPage = it.toInt() }
                }
            }

        })
    }

    fun cleanData() {
        _personList.postValue(mutableListOf())
    }

}

