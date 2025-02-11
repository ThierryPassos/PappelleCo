package com.tito.pappelleco.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.tito.pappelleco.ActivityInicial
import com.tito.pappelleco.models.Product
import com.tito.pappelleco.R

class ProductDetailDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(product: Product): ProductDetailDialogFragment {
            val fragment = ProductDetailDialogFragment()
            val args = Bundle()
            args.putParcelable("product", product)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)

        // Obtendo o produto do Bundle com segurança
        val product: Product? = arguments?.getParcelable("product")
        if (product == null) {
            // Produto não encontrado, feche o diálogo ou exiba um erro
            Toast.makeText(requireContext(), "Erro ao carregar o produto", Toast.LENGTH_SHORT).show()
            dismiss()
            return null
        }

        // Exibindo os detalhes do produto
        view.findViewById<TextView>(R.id.productName).text = product.name
        view.findViewById<TextView>(R.id.productDescription).text = product.description
        view.findViewById<TextView>(R.id.productPrice).text = "R$ %.2f".format(product.price)

        // Exibindo a imagem do produto
        val productImageView: ImageView = view.findViewById(R.id.productImage)
        Glide.with(this)
            .load(product.imageResId) // Usando o ID da imagem do drawable
            .into(productImageView)

        // Botão para adicionar ao carrinho
        view.findViewById<Button>(R.id.addToCartButton).setOnClickListener {
            // Chama a função para adicionar o produto ao carrinho
            (activity as? ActivityInicial)?.addToCart(product)

            // Exibe uma mensagem de confirmação
            Toast.makeText(requireContext(), "${product.name} adicionado ao carrinho", Toast.LENGTH_SHORT).show()

            // Fecha o diálogo
            dismiss()
        }

        return view
    }
}
