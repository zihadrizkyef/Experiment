package com.zref.experiment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnuViewModel : ViewModel() {

    val itemSelected = MutableLiveData<Int>()

}