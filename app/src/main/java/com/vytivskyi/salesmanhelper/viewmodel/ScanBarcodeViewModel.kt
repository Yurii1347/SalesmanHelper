package com.vytivskyi.salesmanhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.vytivskyi.salesmanhelper.ScanBarcodeFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScanBarcodeViewModel : ViewModel() {

    private val _progressState = MutableLiveData<Boolean>()
    val progressState: LiveData<Boolean> = _progressState

    private val _navigation = MutableLiveData<NavDirections?>()
    val navigation: LiveData<NavDirections?> = _navigation

    init {
        _progressState.value = false
    }

    fun searchBarcode(barcode: String) {
        _progressState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _navigation.value = ScanBarcodeFragmentDirections.actionBarcodeSacannerToSuccessFragment(barcode)
            _progressState.value = false
        }
    }

    fun doneNavigating() {
        _navigation.value = null
    }
}