package com.example.pmuprojekat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Supplier

class SupplierViewModel: ViewModel() {
    val lstDobavljaci= MutableLiveData<List<Supplier>>()
}