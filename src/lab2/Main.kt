package lab2

import kotlin.random.Random

val Orders: MutableList<Order> = mutableListOf()

val Catalog: Array<Product> = arrayOf(
    Product(1, "Test", 542.41, null),
    Electronics(2, "RTX 4090", 34005.43, "GPU",
        "Nvidia", "5 Years"),
    Book(2, "Война и мир", 500.0, "Роман-эпопея",
        "Толстой", "publisher", "Роман"),
    Clothing(3, "Майка", 1200.50, null,
        50, "Синтетика", "Мужская")
)

fun main(){
    menu()
}

fun menu(){
    while (true){
        println("Выберите действие: \n 1.Оформить новый заказ \n 2.Посмотреть заказы")
        when(readln().toIntOrNull()){
            1 -> {
                val cart = createShoppingCart()
                val newOrder = Order(Random.nextInt(), cart).placeOrder()
                Orders.add(newOrder)
            }
            2 -> {
                for (order in Orders){
                    println("Заказ ${order.orderId}\n" +
                            "Сумма ${order.cart.calculateTotal()}\n" +
                            "Товары в корзине:\n ${order.cart.getInfo()}")
                }
            }
            else -> return
        }
    }
}

fun createShoppingCart(): ShoppingCart{

    val cart = ShoppingCart()

    while (true){
        println("Выберите действие: \n 1.Добавить \n 2.Просмотреть корзину \n 3.Оформить заказ")
        when (readln().toIntOrNull()) {
            1 -> {
                println("Введите айди товара")
                val id: Int = readln().toInt()
                val product: Product = Catalog.first { product -> product.productId == id }
                cart.addProduct(product)
            }
            2 -> {
                cart.getInfo()
            }
            3 -> {
                return cart
            }
            else -> {
                println("Ошибка")
            }
        }
    }
}
