package lab2

class Electronics(productId: Int, name: String, price: Double, description: String?, private val brand: String,
    private val warrantyPeriod: String) : Product(productId, name, price, description){

    override fun displayInfo() {
        println("$productId : $name\n" +
                "$price\n" +
                "Изготовитель: $brand\n" +
                "Срок годности $warrantyPeriod\n" +
                "Описание: ${description ?: "У товара нет описания"}")
    }
}