package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pmuprojekat.R
import com.example.pmuprojekat.data.Product

class ProductViewAdapter2(val ctx: Context, val data: LiveData<List<Product>>): RecyclerView.Adapter<ProductViewAdapter2.ProductViewHodler>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHodler {
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.order_items_product_row,parent,false)
        return ProductViewHodler(view)
    }

    override fun onBindViewHolder(holder: ProductViewHodler, position: Int) {
        holder.bindItems(data.value!!.get(position))
    }

    override fun getItemCount(): Int {
        if(data.value!=null){
            return data.value!!.size
        }
        return 0;
    }


    inner class ProductViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(model: Product){
            val nam=itemView.findViewById<TextView>(R.id.productName2)
            nam.text=model.productName
        }
    }

}