package com.tito.pappelleco

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tito.pappelleco.adapters.ItemAdapter
import com.tito.pappelleco.models.Product

class ActivityAdmin : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var itemAdapter: ItemAdapter
    private var allProducts: List<Product> = listOf()

    companion object {
        private const val REQUEST_CODE_ADD_ITEM = 1
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Inicializa banco de dados
        dbHelper = SQLiteHelper(this)

        // Busca todos os produtos do banco de dados
        allProducts = dbHelper.getAllProducts()

        // Configura RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.adminItemListRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(allProducts)
        recyclerView.adapter = itemAdapter

        // Botão flutuante para adicionar itens
        val fabAddItem = findViewById<FloatingActionButton>(R.id.fabAddItem)
        fabAddItem.setOnClickListener {
            val intent = Intent(this, ActivityAddItem::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_ITEM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == Activity.RESULT_OK) {
            // Atualiza lista de produtos
            allProducts = dbHelper.getAllProducts()
            itemAdapter.updateProducts(allProducts)
            Toast.makeText(this, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}
