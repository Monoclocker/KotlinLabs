package lab2

open class Product(open val productId: Int,
              open val name: String,
              val price: Double,
              open val description: String?) {

    open fun displayInfo(){
        println("$productId : $name \t $price \n ${description ?: "У товара нет описания"}\n")
    }
}