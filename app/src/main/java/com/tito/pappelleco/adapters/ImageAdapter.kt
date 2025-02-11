package com.tito.pappelleco.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tito.pappelleco.R
import com.tito.pappelleco.models.ImageItem

class ImageAdapter(private val onCategoryClick: (String) -> Unit) : ListAdapter<ImageItem, ImageAdapter.ViewHolder>(DiffCallback()) {

    // DiffUtil Callback para otimizar a atualização de itens na lista
    class DiffCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            // Verifica se o item é o mesmo com base no ID
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            // Verifica se o conteúdo do item é o mesmo
            return oldItem == newItem
        }
    }

    // ViewHolder para gerenciar a exibição de cada item da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bindData(item: ImageItem, onCategoryClick: (String) -> Unit) {
            // Carrega a imagem do item na ImageView
            imageView.setImageResource(item.drawableResId)

            // Configura o clique no item para passar a categoria
            itemView.setOnClickListener {
                onCategoryClick(item.category) // Chama a função passando a categoria
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Cria uma nova view para o item da lista
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = getItem(position) // Obtém o item da posição
        holder.bindData(imageItem, onCategoryClick) // Preenche a view do ViewHolder com os dados
    }
}
