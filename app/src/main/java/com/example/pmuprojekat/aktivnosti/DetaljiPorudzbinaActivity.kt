package com.example.pmuprojekat.aktivnosti

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.*
import com.example.pmuprojekat.generici.GenericClass
import com.example.pmuprojekat.viewadapters.*
import com.example.pmuprojekat.viewmodels.OrderViewModel
import com.example.pmuprojekat.viewmodels.ProductViewModel
import com.example.pmuprojekat.viewmodels.SupplierViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class DetaljiPorudzbinaActivity: AppCompatActivity(){


    var ordId:Int? = null
    val viewModel: ProductViewModel by viewModels()
    val viewModel2: SupplierViewModel by viewModels()
    var supplierViewAdapter:SupplierViewAdapter? = null
    var productViewAdapter2: ProductViewAdapter2? = null
    var newProducts:List<Product>? = null
    var newSuppliers: List<Supplier>? = null
    //var suppliersIds = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_items)
        ordId = intent.extras!!["ordId"] as Int?


        dohvatiProizvode(this, ApiRoutes.orderDetails)
    }


     private fun dohvatiProizvode(ctx: Context, sUrl: String):GenericClass  {
        var orders: List<OrderDetails>? = null
        var productsIds = mutableListOf<Int>()
        var products: List<Product>? = null
        var searchedProducts = mutableListOf<Product>()

        var suppliers: List<Supplier>? = null
        var searchedSuppliers = mutableListOf<Supplier>()
         var searchedSuppliersMap = mutableMapOf<Int,Supplier>()

        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)

            if (result != null) {
                try {

                    orders = Klaxon().parseArray(result)

                    if(orders != null)
                    {
                            for(i in 0 until orders!!.size)
                            {
                                if(orders!![i].orderId == ordId)
                                {
                                    productsIds.add(orders!![i].productId)
                                }
                            }

                            println("-----------------------PRODUCTIDS: $productsIds ------------------------------------")

                            productsIds = productsIds.sorted() as MutableList<Int>
                            val result2 = apiHandler.getRequest(ApiRoutes.products)

                            if(result2 != null)
                            {
                                try {

                                    products = Klaxon().parseArray(result2)
                                    if (products != null) {


                                        for (i in 0 until products!!.size)
                                        {
                                            for (j in 0 until productsIds.size)
                                            {
                                                if(products!![i].productId == productsIds[j])
                                                {
                                                    searchedProducts.add(products!![i])
                                                    //suppliersIds.add(products!![i].supplierId)
                                                    searchedSuppliersMap.put(products!![i].supplierId,
                                                        Supplier(i,"")
                                                    )
                                                }
                                            }
                                        }
                                        println("-----------------------SUPPLIERSIDSMAP: $searchedSuppliersMap ------------------------------------")
                                        newProducts = searchedProducts

                                        val result3 = apiHandler.getRequest(ApiRoutes.suppliers)

                                        if(result3 != null)
                                        {
                                            suppliers = Klaxon().parseArray(result3)

                                            if(suppliers != null)
                                            {
                                                for (i in 0 until suppliers!!.size) {
                                                    for (j in 0 until searchedSuppliersMap.size) {
                                                        if (searchedSuppliersMap.containsKey(suppliers!![i].supplierId)) {
                                                            //searchedSuppliers.add(suppliers!![i])
                                                            searchedSuppliersMap.put(suppliers!![i].supplierId, Supplier(suppliers!![i].supplierId,suppliers!![i].companyName))
                                                        }
                                                    }
                                                }

                                                searchedSuppliers = ArrayList(searchedSuppliersMap.values)
                                                newSuppliers = searchedSuppliers


                                                withContext(Dispatchers.Main) {

                                                    val ukupnoProizvoda =
                                                        findViewById<TextView>(R.id.ukupnoProizvoda)
                                                    ukupnoProizvoda.text =
                                                        productsIds.count().toString()
                                                    viewModel.lstProizvodi.value = newProducts
                                                    val lstProizvodiView2 =
                                                        findViewById<RecyclerView>(R.id.lstProizvodiView2)
                                                    lstProizvodiView2.layoutManager =
                                                        LinearLayoutManager(ctx)
                                                    productViewAdapter2 =
                                                        ProductViewAdapter2(ctx, viewModel.lstProizvodi)
                                                    lstProizvodiView2.adapter = productViewAdapter2

                                                    viewModel2.lstDobavljaci.value = newSuppliers
                                                    val lstDobavljaciView =
                                                        findViewById<RecyclerView>(R.id.lstDobavljaciView2)
                                                    lstDobavljaciView.layoutManager =
                                                        LinearLayoutManager(ctx)
                                                    supplierViewAdapter =
                                                        SupplierViewAdapter(
                                                            ctx,
                                                            viewModel2.lstDobavljaci
                                                        )
                                                    lstDobavljaciView.adapter = supplierViewAdapter

                                                }


                                            }



                                        }

                                    }

                                } catch (err: Error) {
                                    print("Error when parsing JSON: " + err.localizedMessage)
                                }

                            }
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }


         return GenericClass(newProducts,newSuppliers)

    }

}