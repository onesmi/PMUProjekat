package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat.getColor
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pmuprojekat.R
import com.example.pmuprojekat.aktivnosti.DetaljiKategorijeActivity
import com.example.pmuprojekat.aktivnosti.DetaljiProizvodiActivity
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Product
import com.google.android.material.internal.ContextUtils.getActivity

class ProductViewAdapter(val ctx: Context, val data: LiveData<List<Product>>): RecyclerView.Adapter<ProductViewAdapter.ProductViewHodler>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHodler {
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.product_row,parent,false)
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
            val proId=model.productId
            val nam=itemView.findViewById<TextView>(R.id.productName)
            nam.text=model.productName
            val popust:Boolean = model.discontinued

            println("DISCOUNTED:${model.productName} $popust")
            if(popust)
            {
                nam.setTextColor(Color.WHITE)
                nam.setBackgroundColor(Color.rgb(98, 0, 238))
            }
            else
            {
                nam.setTextColor(Color.BLACK)
                nam.setBackgroundColor(Color.WHITE)
            }

            val detaljnije=itemView.findViewById<Button>(R.id.detaljnijeProizvod)
            detaljnije.setOnClickListener {
                val intent= Intent(ctx, DetaljiProizvodiActivity::class.java)
                intent.putExtra("pId",proId)
                ctx.startActivity(intent)
            }
        }
    }

}


