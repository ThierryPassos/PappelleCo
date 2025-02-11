package com.tito.pappelleco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tito.pappelleco.adapters.CarouselAdapter
import com.tito.pappelleco.adapters.ProductAdapter
import com.tito.pappelleco.fragments.ProductDetailDialogFragment
import com.tito.pappelleco.models.CartItem
import com.tito.pappelleco.models.Product


class ActivityInicial : AppCompatActivity() {

    private lateinit var rvProductList: RecyclerView
    private lateinit var rvImageList: RecyclerView
    private lateinit var products: List<Product>
    private var cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        rvProductList = findViewById(R.id.itemListRV)
        rvImageList = findViewById(R.id.imageRV)

        // Lista de produtos
        products = listOf(

            Product("Lápis", "Lápis de cor", 4.00, "Lápis", R.drawable.lapis1),
            Product("Lápiseira", "Lápiseira top", 7.00, "Lápis", R.drawable.lapiseira),
            Product("Lápis", "Lápis 2b", 2.00, "Lápis", R.drawable.lapis2),
            Product("Caneta Vermelha", "Caneta vermelha", 2.00, "Lápis", R.drawable.canetavermelha),
            Product("Caneta Preta", "Caneta preta", 2.00, "Lápis", R.drawable.canetapreta),
            Product("Caneta deluxe", "Caneta preta", 10.00, "Lápis", R.drawable.canetatrident),
            Product("Caderno universitario azul", "Caderno 10 materias", 32.00, "Caderno", R.drawable.cadernoazul),
            Product("Caderno universitario rosa", "Caderno universitário", 30.00, "Caderno", R.drawable.cadernorosa),
            Product("Caderno universitario roxo", "Caderno universitário", 30.00, "Caderno", R.drawable.cadernoroxo),
            Product("Caderno caligrafia", "Caderno caligrafia", 15.00, "Caderno", R.drawable.caligrafia),
            Product("Caderno desenho", "Caderno de desenho", 15.00, "Caderno", R.drawable.cadernodesenho),
            Product("Caderno quadriculado", "Caderno de matematica", 15.00, "Caderno", R.drawable.matematica),
            Product("Livro Harry Potter", "Saga Harry Potter", 199.00, "Livros", R.drawable.l1),
            Product("Livro Crepusculo", "Saga Crepusculo", 150.00, "Livros", R.drawable.l2),
            Product("Livro bão", "Livro de Romance", 30.00, "Livros", R.drawable.l3),
            Product("Livro Otimo", "Livro de historia", 30.00, "Livros", R.drawable.l4),
            Product("Mochila M1", "Mochila escolar MASC", 99.00, "Mochilas", R.drawable.mochila1),
            Product("Mochila F1", "Mochila escolar", 99.00, "Mochilas", R.drawable.mochila4),
            Product("Mochila M2", "Mochila escolar MASC", 99.00, "Mochilas", R.drawable.mochila2),
            Product("Mochila F2", "Mochila escolar", 99.00, "Mochilas", R.drawable.mochila5),
            Product("Mochila M3", "Mochila escolar MASC", 99.00, "Mochilas", R.drawable.mochila3),
            Product("Mochila F3", "Mochila escolar", 99.00, "Mochilas", R.drawable.mochila6),
            Product("Grampeador", "grampeador do bão", 10.00, "Geral", R.drawable.g1),
            Product("Tesoura", "tesoura corta tudo", 7.00, "Geral", R.drawable.g2),
            Product("Régua", "30cm", 4.00, "Geral", R.drawable.g3),
            Product("Compasso", "compasso", 3.50, "Geral", R.drawable.g4)
        )

        // Lista de imagens do carrossel
        val carouselItems = listOf(
            R.drawable.lapis,
            R.drawable.cadernos,
            R.drawable.mochilas,
            R.drawable.livros,
            R.drawable.geral
        )

        val carouselCategories = listOf("Lápis", "Caderno", "Mochilas", "Livros", "Geral")

        // Configuração do RecyclerView para o carrossel
        rvImageList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImageList.adapter = CarouselAdapter(carouselItems, carouselCategories) { category ->
            updateProductList(category)
        }

        // Configuração do RecyclerView para a lista de produtos
        rvProductList.layoutManager = LinearLayoutManager(this)
        rvProductList.adapter = ProductAdapter(products) { product: Product ->
            showProductDetails(product)
        }


        // Configuração do botão para abrir o carrinho
        val cartButton = findViewById<FloatingActionButton>(R.id.cartButton)
        cartButton.setOnClickListener {
            openCart()
        }
    }

    private fun updateProductList(category: String) {
        val filteredProducts = filterProductsByCategory(category)
        rvProductList.adapter = ProductAdapter(filteredProducts) { product: Product ->
            showProductDetails(product)
        }
    }

    private fun showProductDetails(product: Product) {
        val dialog = ProductDetailDialogFragment.newInstance(product)
        dialog.show(supportFragmentManager, "productDetails")
    }

    private fun openCart() {
        val intent = Intent(this, ActivityCarrinho::class.java)
        intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
        startActivity(intent)
    }

    fun addToCart(product: Product) {
        val existingCartItem = cartItems.find { it.product == product }
        if (existingCartItem != null) {
            existingCartItem.quantity++
        } else {
            cartItems.add(CartItem(product))
        }
    }

    private fun filterProductsByCategory(category: String): List<Product> {
        return products.filter { it.category == category }
    }
}
