package com.project.renthvn

data class Items(
    val itemName: String, val itemPrice: Double, val itemQuantity: Int, val deliveryDate: String,
    val period: Int, val size: String, val pickupDate: String
) {
}