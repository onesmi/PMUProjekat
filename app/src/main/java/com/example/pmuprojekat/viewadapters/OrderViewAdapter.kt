package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pmuprojekat.R
import com.example.pmuprojekat.aktivnosti.DetaljiKategorijeActivity
import com.example.pmuprojekat.aktivnosti.DetaljiPorudzbinaActivity
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Order

class OrderViewAdapter(val ctx: Context, val data: LiveData<List<Order>>): RecyclerView.Adapter<OrderViewAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.order_row,parent,false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bindItems(data.value!!.get(position))
    }

    override fun getItemCount(): Int {
        if(data.value!=null){
            return data.value!!.size
        }
        return 0;
    }
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(model: Order){
            val ordId=model.orderId
            val id=itemView.findViewById<TextView>(R.id.orderId)
            id.text = ordId.toString()

            val detaljnije=itemView.findViewById<Button>(R.id.detaljnijePorudzbina)
            detaljnije.setOnClickListener {
                val intent= Intent(ctx, DetaljiPorudzbinaActivity::class.java)
                intent.putExtra("ordId",ordId)
                ctx.startActivity(intent)
            }
        }
    }

}

