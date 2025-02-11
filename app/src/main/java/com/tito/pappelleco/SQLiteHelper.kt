package com.tito.pappelleco

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tito.pappelleco.models.Categoria
import com.tito.pappelleco.models.Product

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, "PappellcoDatabase", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Criação das tabelas
        db.execSQL("""
            CREATE TABLE Categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT UNIQUE
            )
        """)
        db.execSQL("""
            CREATE TABLE Produto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                descricao TEXT,
                preco REAL,
                categoria_id INTEGER,
                quantidade INTEGER,
                image_path TEXT, -- Caminho local da imagem
                FOREIGN KEY (categoria_id) REFERENCES Categoria(id)
);

        """)
        db.execSQL("""
            CREATE TABLE User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT UNIQUE,
                senha TEXT,
                cpf TEXT UNIQUE
            )
        """)

        // Inserir categorias padrão
        insertDefaultCategories(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Produto")
        db.execSQL("DROP TABLE IF EXISTS Categoria")
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    // Inserir categorias padrão
    fun insertDefaultCategories(db: SQLiteDatabase) {
        val categories = listOf("Livros", "Lápis", "Cadernos", "Mochilas", "Geral")
        categories.forEach { categoryName ->
            val values = ContentValues().apply {
                put("nome", categoryName)
            }
            db.insert("Categoria", null, values)
        }
    }

    // Buscar todas as categorias
    @SuppressLint("Range")
    fun getAllCategories(): List<Categoria> {
        val categories = mutableListOf<Categoria>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Categoria", null)

        if (cursor.moveToFirst()) {
            do {
                val nome = cursor.getString(cursor.getColumnIndex("nome"))
                categories.add(Categoria( nome))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categories
    }

    // Buscar o ID da categoria pelo nome
    @SuppressLint("Range")
    fun getCategoryIdByName(db: SQLiteDatabase, categoryName: String): Int {
        val cursor = db.query(
            "Categoria",
            arrayOf("id"),
            "nome = ?",
            arrayOf(categoryName),
            null, null, null
        )

        val categoryId = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndex("id")) else -1
        cursor.close()
        return categoryId
    }

    // Inserir um novo produto
    fun insertProduct(nome: String, descricao: String, preco: Double, quantidade: Int, categoryId: Int, imagePath: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nome", nome)
            put("descricao", descricao)
            put("preco", preco)
            put("quantidade", quantidade)
            put("categoria_id", categoryId)
            put("image_path", imagePath) // Armazena o caminho local
        }
        val result = db.insert("Produto", null, values)
        return result != -1L
    }


    // Buscar todos os produtos
    @SuppressLint("Range")
    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Produto", null)

        if (cursor.moveToFirst()) {
            do {
                val nome = cursor.getString(cursor.getColumnIndex("nome"))
                val descricao = cursor.getString(cursor.getColumnIndex("descricao"))
                val preco = cursor.getDouble(cursor.getColumnIndex("preco"))
                val quantidade = cursor.getInt(cursor.getColumnIndex("quantidade"))
                val categoriaId = cursor.getInt(cursor.getColumnIndex("categoria_id"))
                val categoriaNome = getCategoryNameById(db, categoriaId)

                products.add(Product(nome, descricao, preco, categoriaNome, quantidade))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    // Buscar o nome da categoria pelo ID
    @SuppressLint("Range")
    private fun getCategoryNameById(db: SQLiteDatabase, categoryId: Int): String {
        val cursor = db.query(
            "Categoria",
            arrayOf("nome"),
            "id = ?",
            arrayOf(categoryId.toString()),
            null, null, null
        )

        val categoryName = if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndex("nome")) else ""
        cursor.close()
        return categoryName
    }

    // Inserir um novo usuário
    fun insertUser(nome: String, email: String, senha: String, cpf: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nome", nome)
            put("email", email)
            put("senha", senha)
            put("cpf", cpf)
        }
        val result = db.insert("User", null, values)
        return result != -1L
    }

    // Verificar usuário
    fun checkUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "User",
            arrayOf("id"),
            "email = ? AND senha = ?",
            arrayOf(email, password),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    // Verificar se CPF já existe
    fun cpfExists(cpf: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "User",
            arrayOf("id"),
            "cpf = ?",
            arrayOf(cpf),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
    fun updatePassword(cpf: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("senha", newPassword)

        val result = db.update("user", contentValues, "cpf = ?", arrayOf(cpf))
        db.close()
        return result > 0
    }

}
