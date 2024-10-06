package lab2

class Clothing(productId: Int, name: String, price: Double, description: String?, private val size : Int, private val material: String,
               private val gender: String) : Product(productId, name, price, description) {
    override fun displayInfo() {
        println("$productId : $name\t" +
                "$price\n" +
                "Размер: $size\n" +
                "Материал $material\n" +
                "Пол: $gender\n" +
                "Описание: ${description ?: "У товара нет описания"} \n")
    }
}