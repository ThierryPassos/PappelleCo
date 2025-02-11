package com.tito.pappelleco.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tito.pappelleco.models.Product
import com.tito.pappelleco.R

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produto, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productName)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.productDescription)
        private val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(product: Product) {
            nameTextView.text = product.name
            descriptionTextView.text = product.description
            priceTextView.text = "R$ %.2f".format(product.price)

            // Carregar a imagem do produto utilizando Glide ou diretamente com o ContextCompat
            Glide.with(itemView.context)
                .load(product.imageResId) // Usando o id do recurso de imagem
                .into(productImageView)

            // Configurar clique no item
            itemView.setOnClickListener {
                onItemClick(product)
            }
        }

    }
}
