package com.tito.pappelleco

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tito.pappelleco.models.Categoria
import java.io.File
import java.io.FileOutputStream

class ActivityAddItem : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper
    private var imageFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        dbHelper = SQLiteHelper(this)

        // Elementos da UI
        val nameEditText = findViewById<EditText>(R.id.edtName)
        val descriptionEditText = findViewById<EditText>(R.id.edtDescription)
        val priceEditText = findViewById<EditText>(R.id.edtPrice)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val imageView = findViewById<ImageView>(R.id.productImageView)
        val selectImageButton = findViewById<Button>(R.id.btnSelectImage)
        val saveButton = findViewById<Button>(R.id.btnAddProduct)

        // Configuração do Spinner de categorias
        setupCategorySpinner(spinnerCategory)

        // Seleção de imagem
        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val categoryName = spinnerCategory.selectedItem?.toString() ?: ""

            if (name.isEmpty() || description.isEmpty() || price == null || categoryName.isEmpty() || imageFilePath == null) {
                Toast.makeText(this, "Preencha todos os campos e selecione uma imagem.", Toast.LENGTH_SHORT).show()
            } else {
                addProduct(name, description, price, categoryName, imageFilePath!!)
            }
        }
    }

    private fun setupCategorySpinner(spinnerCategory: Spinner) {
        val categories = dbHelper.getAllCategories().map(Categoria::nome)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val imageView = findViewById<ImageView>(R.id.productImageView)
                imageView.setImageURI(imageUri)
                saveImageLocally(imageUri)
            }
        }
    }

    private fun saveImageLocally(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val directory = getExternalFilesDir("images")
        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        imageFilePath = file.absolutePath // Salva o caminho do arquivo
    }

    private fun addProduct(name: String, description: String, price: Double, categoryName: String, imagePath: String) {
        val categoryId = dbHelper.getCategoryIdByName(dbHelper.writableDatabase, categoryName)
        if (categoryId == -1) {
            Toast.makeText(this, "Categoria inválida.", Toast.LENGTH_SHORT).show()
            return
        }

        val success = dbHelper.insertProduct(name, description, price, 1, categoryId, imagePath)
        if (success) {
            Toast.makeText(this, "Item adicionado com sucesso.", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Erro ao adicionar o item.", Toast.LENGTH_SHORT).show()
        }
    }
}
