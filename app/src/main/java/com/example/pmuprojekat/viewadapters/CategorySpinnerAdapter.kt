package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.example.pmuprojekat.data.Category

class CategorySpinnerAdapter(val ctx: Context, val textViewResourceId:Int ,val data:List<Category>?): ArrayAdapter<Category?>(ctx,textViewResourceId,data!!)
{

    override fun getCount():Int
    {
        return data!!.size
    }

   override fun getItem(position:Int):Category
    {
        return data!![position]
    }

    override fun getItemId(position:Int): Long
    {
        return position.toLong()
    }

     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val label:TextView = super.getView(position,convertView,parent) as TextView
        label.setTextColor(Color.BLACK)
        label.setText(data!![position]?.categoryName)

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val label:TextView = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.setText(data!![position]?.categoryName)
        return label
    }

    fun getId(position: Int):Int
    {
        return data!![position].categoryId
    }






}