package com.example.pmuprojekat.aktivnosti

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.apitest.viewmodels.CategoryViewModel
import com.example.pmuprojekat.MainActivity
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Order
import com.example.pmuprojekat.viewadapters.CategoryViewAdapter
import com.example.pmuprojekat.viewadapters.OrderViewAdapter
import com.example.pmuprojekat.viewmodels.OrderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PorudzbineActivity : AppCompatActivity() {
    val viewModel: OrderViewModel by viewModels() // the value is computed only on first access
    var orderViewAdapter: OrderViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porudzbine)
        dohvatiPorudzbine(this, ApiRoutes.orders)
    }



    private fun dohvatiPorudzbine(ctx: Context, sUrl: String): List<Order>? {
        var orders: List<Order>? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            if (result != null) {
                try {

                    orders = Klaxon().parseArray(result)

                    withContext(Dispatchers.Main) {
                        viewModel.lstPorudzbine.value = orders
                        val lstPorudzbineView = findViewById<RecyclerView>(R.id.lstPorudzbineView)
                        lstPorudzbineView.layoutManager = LinearLayoutManager(ctx)
                        orderViewAdapter = OrderViewAdapter(ctx, viewModel.lstPorudzbine)
                        lstPorudzbineView.adapter = orderViewAdapter

                        var backBtn = findViewById<Button>(R.id.backButton3)
                        backBtn.setOnClickListener {
                            val intent= Intent(ctx, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(intent)
                        }
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }
        return orders
    }






}