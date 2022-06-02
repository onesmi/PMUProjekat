package com.example.pmuprojekat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pmuprojekat.data.Order
import com.example.pmuprojekat.data.Product

class OrderViewModel:ViewModel() {
    val lstPorudzbine = MutableLiveData<List<Order>>()
}