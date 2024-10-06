package lab2

class Book(productId: Int, name: String, price: Double, description: String?, private val author: String,
           private val publisher: String, private val genre: String) : Product(productId, name, price, description){

    override fun displayInfo() {
        println("$productId : $name\n" +
                "$price\n" +
                "Автор: $author\n" +
                "Издатель $publisher\n" +
                "Жанр: $genre\n" +
                "Описание: ${description ?: "У товара нет описания"}")
    }
}