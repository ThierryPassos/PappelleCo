package com.tito.pappelleco.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tito.pappelleco.R
import com.tito.pappelleco.models.Product

class ItemAdapter(private var productList: List<Product>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // ViewHolder que gerencia a exibição de um item de produto
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.productName)
        private val descricaoTextView: TextView = itemView.findViewById(R.id.productDescription)
        private val precoTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)

        // Função de bind para associar os dados do produto aos elementos da view
        fun bind(product: Product) {
            nomeTextView.text = product.name
            descricaoTextView.text = product.description
            precoTextView.text = "R$ %.2f".format(product.price)

            // Usando Glide para carregar a imagem do produto (caso a imagem seja um URL ou URI)
            Glide.with(itemView.context)
                .load(product.imageResId) // Usando o id do recurso de imagem (ou URI)
                .into(productImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Infla o layout do item de produto
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produto, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Associa os dados do produto ao ViewHolder
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    // Função para atualizar a lista de produtos e notificar o RecyclerView sobre a alteração
    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts // Atualiza a lista interna
        notifyDataSetChanged()    // Notifica o RecyclerView para atualizar a interface
    }
}
