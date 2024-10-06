package lab2

class ShoppingCart {

    private var products: Array<Product> = emptyArray()

    fun addProduct(newProduct: Product){
        products = arrayOf(*products, newProduct)
    }

    fun removeProduct(productId: Int){
        products = products.filter { product -> product.productId == productId }
            .toTypedArray()
    }

    fun getInfo(){
        for (product in products){
            product.displayInfo()
        }
    }

    fun calculateTotal(): Double {
        var result = .0
        for (product in products) {
            result += product.price
        }
        return result
    }
}