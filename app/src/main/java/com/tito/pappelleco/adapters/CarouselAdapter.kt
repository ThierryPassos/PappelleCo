package com.tito.pappelleco.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tito.pappelleco.R

class CarouselAdapter(
    private val items: List<Int>, // Imagens do carrossel
    private val categories: List<String>, // Categorias associadas às imagens
    private val onCategoryClick: (String) -> Unit // Função de callback para filtrar os produtos pela categoria
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position], categories[position], onCategoryClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.carouselImageView)

        fun bind(imageResId: Int, category: String, onCategoryClick: (String) -> Unit) {
            // Carregar a imagem
            Glide.with(itemView.context)
                .load(imageResId)
                .into(imageView)

            // Quando a imagem for clicada, filtrar os produtos pela categoria
            itemView.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }
}
