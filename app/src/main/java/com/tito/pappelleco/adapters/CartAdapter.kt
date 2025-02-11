package com.tito.pappelleco.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tito.pappelleco.R
import com.tito.pappelleco.models.CartItem

class CartAdapter(
    private var items: MutableList<CartItem>,
    private val onRemoveItem: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.productName)
        val itemQuantity: TextView = view.findViewById(R.id.productQuantity)
        val itemPrice: TextView = view.findViewById(R.id.productPrice)
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val removeButton: Button = view.findViewById(R.id.removeButton) // Referência ao botão
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        // Configura os dados no layout
        holder.itemName.text = item.product.name
        holder.itemQuantity.text = "Quantidade: ${item.quantity}"
        holder.itemPrice.text = "R$ %.2f".format(item.product.price)

        // Carrega a imagem usando Glide
        Glide.with(holder.itemView.context)
            .load(item.product.imageResId)
            .into(holder.imageView)

        // Configurar o clique no botão Remover
        holder.removeButton.setOnClickListener {
            onRemoveItem(item)
        }
    }

    override fun getItemCount(): Int = items.size

    // Método para atualizar a lista
    fun updateItems(newItems: MutableList<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
