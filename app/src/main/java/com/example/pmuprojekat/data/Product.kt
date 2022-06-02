package com.example.pmuprojekat.data

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.FieldPosition

data class Product(var productId:Int, var productName: String, var supplierId:Int, var categoryId:Int, var unitPrice:Double, var discontinued:Boolean)
{

}
