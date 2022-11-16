package com.ciejaycoding.letsmeat.models

data class Clients(
    val id : String ? = null,
    val profile : String? = null,
    val phone : String ? = null,
    val fullname : String ? = null,
    val addresses : Addresses ? = null,
    val defaultAddress : Int ? = null,
)



data class Addresses(
    val contact: Contact? = null,
    val location: Location ? = null,
)
data class Location(
    val province : String? = null,
    val city : String ? = null,
    val barangay : String ? = null,
    val landmark : String ? = null,
)

data class Contact(
    val person : String ? = null,
    val number: String ? = null,
)