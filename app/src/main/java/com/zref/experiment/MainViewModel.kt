package com.zref.experiment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isExpand = MutableLiveData<Boolean>()
}