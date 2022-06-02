package com.example.pmuprojekat.aktivnosti

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
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
import com.example.pmuprojekat.data.Product
import com.example.pmuprojekat.data.Supplier
import com.example.pmuprojekat.viewadapters.CategorySpinnerAdapter
import com.example.pmuprojekat.viewadapters.CategoryViewAdapter
import com.example.pmuprojekat.viewadapters.ProductViewAdapter
import com.example.pmuprojekat.viewadapters.SupplierSpinnerAdapter
import com.example.pmuprojekat.viewmodels.ProductViewModel
import com.example.pmuprojekat.viewmodels.SupplierViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProizvodiActivity: AppCompatActivity() {

    val viewModel: ProductViewModel by viewModels()
    val viewModel2: CategoryViewModel by viewModels()
    val viewModel3: SupplierViewModel by viewModels()
    var productViewAdapter: ProductViewAdapter? = null

    var adapter: CategorySpinnerAdapter? = null
    var adapter2: SupplierSpinnerAdapter? = null
    var catID: Int = 0
    var supID: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proizvodi)
        dohvatiProizvode(this, ApiRoutes.products)
    }


    private fun dohvatiProizvode(ctx: Context, sUrl: String): List<Product>? {
        var products: List<Product>? = null


        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            if (result != null) {
                try {

                    products = Klaxon().parseArray(result)

                    withContext(Dispatchers.Main) {


                        viewModel.lstProizvodi.value = products
                        val lstProizvodiView = findViewById<RecyclerView>(R.id.lstProizvodiView)
                        lstProizvodiView.layoutManager = LinearLayoutManager(ctx)
                        productViewAdapter = ProductViewAdapter(ctx, viewModel.lstProizvodi)
                        lstProizvodiView.adapter = productViewAdapter

                        var backBtn = findViewById<Button>(R.id.backButton2)
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

        return products
    }


     fun prikaziUnosProizvoda(view: View) {

        setContentView(R.layout.product_item)


        // DOHVATANJE KATEGORIJA
        var categories: List<Category>? = null

        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(ApiRoutes.categories)
            if (result != null) {
                try {

                    categories = Klaxon().parseArray(result)

                    withContext(Dispatchers.Main) {
                        viewModel2.lstKategorije.value = categories
                        var spinner: Spinner = findViewById(R.id.spinner)
                        adapter = CategorySpinnerAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_item,
                            categories
                        )
                        spinner.adapter = adapter
                        spinner?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {


                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    catID = adapter!!.getId(p2)
                                    println("catID: " + catID)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
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

        //DOHVATANJE DOBAVLJACA
        var suppliers: List<Supplier>? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(ApiRoutes.suppliers)
            if (result != null) {
                try {

                    suppliers = Klaxon().parseArray(result)

                    withContext(Dispatchers.Main) {
                        viewModel3.lstDobavljaci.value = suppliers
                        var spinner2: Spinner = findViewById(R.id.spinner2)
                        adapter2 = SupplierSpinnerAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_item,
                            suppliers
                        )
                        spinner2.adapter = adapter2

                        spinner2?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {


                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    supID = adapter2!!.getId(p2)
                                    println("supID: " + supID)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
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


        // NAZIV, CENA, POPUST
        val productButton = findViewById<Button>(R.id.proizvodAkcija)
        productButton.text = "Dodaj"
        val nazivProizvoda:EditText? = findViewById<EditText>(R.id.nazivProizvoda)
        val cenaProizvoda:EditText? = findViewById<EditText>(R.id.cenaProizvoda)
        val naPopustu = findViewById<CheckBox>(R.id.popust)

        productButton.setOnClickListener {
            val noviProizvod = Product(
                0,
                nazivProizvoda?.text.toString(),
                supID,
                catID,
                cenaProizvoda?.text.toString().toDouble(),
                naPopustu.isChecked
            )

            //UPIS PROIZVODA U BAZU
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val apiHandler = NorthwindApiHandler()
                    val result = apiHandler.postRequest(
                        ApiRoutes.products,
                        Klaxon().toJsonString(noviProizvod)
                    )
                    if (result != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(getApplicationContext(), "Usepsno dodavanje proizvoda", Toast.LENGTH_LONG)
                                .show()

                            //TODO NOVO DODATO
                            val intent= Intent(applicationContext, ProizvodiActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(intent)
                        }

                    } else {
                        print("Error: Get request returned no response")
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            }

        }




    }


}