package com.project.renthvn

data class Items(
    val cartitemID:String, val itemImage:String ,val itemName: String, val itemPrice: Double, val deliveryDate: String,
    val period: Int, val size: String, val pickupDate: String, val productID:String, val gender:String
) {
}
