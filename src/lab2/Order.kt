package lab2

import java.time.LocalDate

class Order(val orderId: Int, val cart: ShoppingCart) {

    var orderDate: LocalDate? = null

    fun placeOrder() : Order{
        orderDate = LocalDate.now()
        return this
    }
}