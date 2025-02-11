package com.tito.pappelleco

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tito.pappelleco.adapters.CartAdapter
import com.tito.pappelleco.models.CartItem
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ActivityCarrinho : AppCompatActivity() {

    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var finishPurchaseButton: Button
    private lateinit var totalPriceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        initializeViews()

        // Recuperar os itens do carrinho passados pela Intent
        cartItems = intent.getParcelableArrayListExtra<CartItem>("cartItems")?.toMutableList() ?: mutableListOf()

        // Atualizar a UI com os itens do carrinho
        updateCartUI()

        // Configuração do RecyclerView
        setupRecyclerView()

        // Configurar o botão de finalizar compra
        setupFinishPurchaseButton()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerView)
        emptyTextView = findViewById(R.id.emptyCartText)
        finishPurchaseButton = findViewById(R.id.finishPurchaseButton)
        totalPriceText = findViewById(R.id.totalPriceText)
    }

    private fun updateCartUI() {
        if (cartItems.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            totalPriceText.text = "Total: R$ 0.00"
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            totalPriceText.text = "Total: R$ %.2f".format(calculateTotalPrice())
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems) { itemToRemove ->
            removeItemFromCart(itemToRemove)
        }
    }

    private fun setupFinishPurchaseButton() {
        finishPurchaseButton.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                createPdf("TITO")
                finalizePurchase()
            } else {
                Toast.makeText(this, "O carrinho está vazio!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateTotalPrice(): Double {
        return cartItems.sumOf { it.product.price * it.quantity }
    }

    private fun removeItemFromCart(itemToRemove: CartItem) {
        cartItems.remove(itemToRemove)
        recyclerView.adapter?.notifyDataSetChanged()
        updateCartUI()
        Toast.makeText(this, "${itemToRemove.product.name} removido do carrinho.", Toast.LENGTH_SHORT).show()
    }

    private fun finalizePurchase() {
        cartItems.clear() // Zerar o carrinho
        recyclerView.adapter?.notifyDataSetChanged()
        updateCartUI()
        Toast.makeText(this, "Compra finalizada!", Toast.LENGTH_SHORT).show()

        // Retornar para a ActivityInicial
        val intent = Intent(this, ActivityInicial::class.java)
        startActivity(intent)
        finish() // Fecha a ActivityCarrinho
    }

    private fun createPdf(userName: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()

        // Adicionar título ao PDF
        paint.textSize = 18f
        canvas.drawText("Nota Fiscal - $userName", 10f, 25f, paint)

        // Listar os itens do carrinho
        paint.textSize = 14f
        var yPosition = 50f
        cartItems.forEachIndexed { index, item ->
            val itemText = "${index + 1}. ${item.product.name} - ${item.quantity}x R$ ${item.product.price}"
            canvas.drawText(itemText, 10f, yPosition, paint)
            yPosition += 20f
        }

        pdfDocument.finishPage(page)

        // Salvar o PDF no armazenamento
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!directory.exists()) directory.mkdirs()

        val file = File(directory, "NotaFiscal_${System.currentTimeMillis()}.pdf")
        try {
            FileOutputStream(file).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            Toast.makeText(this, "PDF salvo em: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Erro ao criar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}
