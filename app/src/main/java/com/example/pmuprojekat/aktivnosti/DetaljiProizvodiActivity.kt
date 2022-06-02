package com.example.pmuprojekat.aktivnosti

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import com.example.apitest.viewmodels.CategoryViewModel
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Product
import com.example.pmuprojekat.data.Supplier
import com.example.pmuprojekat.generici.GenericClass2
import com.example.pmuprojekat.viewadapters.CategorySpinnerAdapter
import com.example.pmuprojekat.viewadapters.SupplierSpinnerAdapter
import com.example.pmuprojekat.viewmodels.ProductViewModel
import com.example.pmuprojekat.viewmodels.SupplierViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetaljiProizvodiActivity : AppCompatActivity() {


    var catID: Int? = null
    var newCatID: Int? = null
    var catIndex: Int? = null
    val viewModel: CategoryViewModel by viewModels()
    var adapter: CategorySpinnerAdapter? = null
    var spinner: Spinner? = null
    var categories: List<Category>? = null

    var supID: Int? = null
    var newSupId: Int? = null
    var supIndex: Int? = null
    val viewModel2: SupplierViewModel by viewModels()
    var adapter2: SupplierSpinnerAdapter? = null
    var spinner2: Spinner? = null
    var suppliers: List<Supplier>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_item)
        val pId = intent.extras!!["pId"]

        spinner = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)
        dohvatiProizvod(ApiRoutes.products + "/${pId}")


        // PRETHODNO RESENJE
        /*dohvatiKategorije(ApiRoutes.categories)
        dohvatiDobavljace(ApiRoutes.suppliers)
        dohvatiProizvode(ApiRoutes.products + "/${pId}")*/
    }


    fun dohvatiProizvod(sUrl: String):GenericClass2 {
        var product: Product? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            if (result != null) {
                try {

                    product = Klaxon().parse(result)
                    catID = product!!.categoryId
                    supID = product!!.supplierId

                    if (catID != null) {
                        val result2 = apiHandler.getRequest(ApiRoutes.categories)

                        if (result2 != null) {
                            try {
                                categories = Klaxon().parseArray(result2)


                                if (categories != null) {
                                    for (i in 0..(categories?.size?.minus(1!!)!!)) {
                                        if (categories!![i].categoryId == catID) catIndex = i
                                    }

                                    if (supID != null) {
                                        val result3 = apiHandler.getRequest(ApiRoutes.suppliers)

                                        if (result3 != null) {
                                            try {
                                                suppliers = Klaxon().parseArray(result3)

                                                if (suppliers != null) {
                                                    for (i in 0..(suppliers?.size?.minus(1!!)!!)) {
                                                        if (suppliers!![i].supplierId == supID) supIndex =
                                                            i
                                                    }

                                                    withContext(Dispatchers.Main) {

                                                        viewModel.lstKategorije.value = categories
                                                        adapter = CategorySpinnerAdapter(
                                                            applicationContext,
                                                            android.R.layout.simple_spinner_item,
                                                            categories
                                                        )
                                                        spinner?.adapter = adapter
                                                        spinner?.setSelection(catIndex!!)

                                                        spinner?.onItemSelectedListener =
                                                            object : AdapterView.OnItemSelectedListener {


                                                                override fun onItemSelected(
                                                                    p0: AdapterView<*>?,
                                                                    p1: View?,
                                                                    p2: Int,
                                                                    p3: Long
                                                                ) {
                                                                    newCatID = adapter!!.getId(p2)
                                                                }

                                                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                                                    TODO("Not yet implemented")
                                                                }
                                                            }



                                                        viewModel2.lstDobavljaci.value = suppliers
                                                        adapter2 = SupplierSpinnerAdapter(
                                                            applicationContext,
                                                            android.R.layout.simple_spinner_item,
                                                            suppliers
                                                        )

                                                        spinner2?.adapter = adapter2
                                                        spinner2?.setSelection(supIndex!!)

                                                        spinner2?.onItemSelectedListener =
                                                            object :
                                                                AdapterView.OnItemSelectedListener {


                                                                override fun onItemSelected(
                                                                    p0: AdapterView<*>?,
                                                                    p1: View?,
                                                                    p2: Int,
                                                                    p3: Long
                                                                ) {
                                                                    newSupId = adapter2!!.getId(p2)
                                                                }

                                                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                                                    TODO("Not yet implemented")
                                                                }
                                                            }

                                                        val nazivProizvoda = findViewById<EditText>(R.id.nazivProizvoda)
                                                        nazivProizvoda.setText(product!!.productName)
                                                        val cenaProizoda = findViewById<EditText>(R.id.cenaProizvoda)
                                                        cenaProizoda.setText(product!!.unitPrice.toString())
                                                        val popust = findViewById<CheckBox>(R.id.popust)
                                                        popust.isChecked = product!!.discontinued


                                                        val brisanjeProizvoda = findViewById<Button>(R.id.proizvodAkcija2)
                                                        brisanjeProizvoda.text = "Obrisi"
                                                        brisanjeProizvoda.isVisible = true
                                                        brisanjeProizvoda.setOnClickListener { obrisiProizvod(product!!.productId) }
                                                        val snimiIzmene = findViewById<Button>(R.id.proizvodAkcija)
                                                        snimiIzmene.text = "Snimi"
                                                        snimiIzmene.setOnClickListener {
                                                            snimanjeIzmena(
                                                                product!!.productId,
                                                                nazivProizvoda.text.toString(),
                                                                newSupId!!.toString().toInt(),
                                                                newCatID!!.toString().toInt(),
                                                                cenaProizoda.text.toString().toDouble(),
                                                                popust.isChecked
                                                            )
                                                        }
                                                    }
                                                }
                                            } catch (err: Error) {
                                                print("Error when parsing JSON: " + err.localizedMessage)
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

        return GenericClass2(categories,suppliers,product)
    }

    fun obrisiProizvod(pId: Int) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val apiHandler = NorthwindApiHandler()
                    val result = apiHandler.deleteRequest(ApiRoutes.products + "/${pId}")
                    if (result != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                getApplicationContext(),
                                "Usepsno obrisan proizvod",
                                Toast.LENGTH_LONG
                            )
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

        fun snimanjeIzmena(
            pId: Int,
            pName: String,
            supId: Int,
            catId: Int,
            uP: Double,
            disc: Boolean
        ) {
            val izmenjenProizvod = Product(
                pId,
                pName,
                supId,
                catId,
                uP,
                disc
            )
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val apiHandler = NorthwindApiHandler()
                    val result = apiHandler.putRequest(
                        ApiRoutes.products + "/${pId}",
                        Klaxon().toJsonString(izmenjenProizvod)
                    )
                    if (result != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                getApplicationContext(),
                                "Usepsno izmenjeni podaci",
                                Toast.LENGTH_LONG
                            )
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


// PRETHODNO RESENJE
/*fun dohvatiProizvode(sUrl: String): Product? {
            var product: Product? = null
            lifecycleScope.launch(Dispatchers.IO) {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.getRequest(sUrl)
                if (result != null) {
                    try {
                        // Parse result string JSON to data class
                        product = Klaxon().parse(result)

                        withContext(Dispatchers.Main) {
                            catID = product!!.categoryId
                            supID = product!!.supplierId

                            val nazivProizvoda = findViewById<EditText>(R.id.nazivProizvoda)
                            nazivProizvoda.setText(product!!.productName)
                            val cenaProizoda = findViewById<EditText>(R.id.cenaProizvoda)
                            cenaProizoda.setText(product!!.unitPrice.toString())
                            val popust = findViewById<CheckBox>(R.id.popust)
                            popust.isChecked = product!!.discontinued


                            val brisanjeProizvoda = findViewById<Button>(R.id.proizvodAkcija2)
                            brisanjeProizvoda.text = "Obrisi"
                            brisanjeProizvoda.isVisible = true
                            brisanjeProizvoda.setOnClickListener { obrisiProizvod(product!!.productId) }
                            val snimiIzmene = findViewById<Button>(R.id.proizvodAkcija)
                            snimiIzmene.text = "Snimi"
                            snimiIzmene.setOnClickListener {
                                snimanjeIzmena(
                                    product!!.productId,
                                    nazivProizvoda.text.toString(),
                                    newSupId!!.toString().toInt(),
                                    newCatID!!.toString().toInt(),
                                    cenaProizoda.text.toString().toDouble(),
                                    popust.isChecked
                                )
                            }

                        }
                    } catch (err: Error) {
                        print("Error when parsing JSON: " + err.localizedMessage)
                    }
                } else {
                    print("Error: Get request returned no response")
                }
                println("------------------------------PROIZVODI------------------------------")
            }
            return product
        }


        fun dohvatiKategorije(sUrl: String) {

            lifecycleScope.launch(Dispatchers.IO) {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.getRequest(sUrl)
                if (result != null) {
                    try {
                        // Parse result string JSON to data class
                        categories = Klaxon().parseArray(result)

                        withContext(Dispatchers.Main) {
                            viewModel.lstKategorije.value = categories
                            spinner = findViewById(R.id.spinner)
                            adapter = CategorySpinnerAdapter(
                                applicationContext,
                                android.R.layout.simple_spinner_item,
                                categories
                            )

                            spinner?.adapter = adapter
                        }

                    } catch (err: Error) {
                        print("Error when parsing JSON: " + err.localizedMessage)
                    }
                } else {
                    print("Error: Get request returned no response")
                }

                for (i in 0..(categories?.size?.minus(1!!)!!)) {
                    if (categories!![i].categoryId == catID) catIndex = i
                }
                if (catIndex != null) {
                    spinner?.setSelection(catIndex!!)
                    println("catIndex: $catIndex ------------------------KATEGORIJE------------------------------")
                }

                spinner?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {


                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            newCatID = adapter!!.getId(p2)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }

            }

        }

        fun dohvatiDobavljace(sUrl: String) {

            lifecycleScope.launch(Dispatchers.IO) {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.getRequest(sUrl)
                if (result != null) {
                    try {
                        // Parse result string JSON to data class
                        suppliers = Klaxon().parseArray(result)

                        withContext(Dispatchers.Main) {
                            viewModel2.lstDobavljaci.value = suppliers
                            spinner2 = findViewById(R.id.spinner2)
                            adapter2 = SupplierSpinnerAdapter(
                                applicationContext,
                                android.R.layout.simple_spinner_item,
                                suppliers
                            )

                            spinner2?.adapter = adapter2
                        }

                    } catch (err: Error) {
                        print("Error when parsing JSON: " + err.localizedMessage)
                    }
                } else {
                    print("Error: Get request returned no response")
                }

                for (i in 0..(suppliers?.size?.minus(1!!)!!)) {
                    if (suppliers!![i].supplierId == supID) supIndex = i
                }
                if (supIndex != null) {
                    spinner2?.setSelection(supIndex!!)
                    println("supIndex: $supIndex ------------------------DOBAVLJACI------------------------------")
                }

                spinner2?.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {


                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            newSupId = adapter2!!.getId(p2)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }

            }

        }*/

}

