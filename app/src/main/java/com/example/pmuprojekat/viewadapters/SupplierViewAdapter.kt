package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pmuprojekat.R
import com.example.pmuprojekat.data.Supplier

class SupplierViewAdapter(val ctx: Context, val data: LiveData<List<Supplier>>): RecyclerView.Adapter<SupplierViewAdapter.SupplierViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.order_items_supplier_row,parent,false)
        return SupplierViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        holder.bindItems(data.value!!.get(position))
    }

    override fun getItemCount(): Int {
        if(data.value!=null){
            return data.value!!.size
        }
        return 0;
    }


    inner class SupplierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(model: Supplier){
            val nam=itemView.findViewById<TextView>(R.id.supplierName2)
            nam.text=model.companyName
        }
    }

}
