package com.ciejaycoding.letsmeat.repository
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.CLIENTS_TABLE
import com.ciejaycoding.letsmeat.utils.PRODUCTS_TABLE
import com.ciejaycoding.letsmeat.utils.UiState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class ProductRepositoryImpl(private val firestore: FirebaseFirestore) : ProductRepository {

    private var productsList : MutableList<Products> ? = null

    override suspend fun getAllProducts(result: (UiState<List<Products>>) -> Unit) {
        productsList?.let {
            result.invoke(UiState.Success(it))
            return
        }
        productsList = mutableListOf()
        result.invoke(UiState.Loading)
        firestore.collection(PRODUCTS_TABLE).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                productsList?.clear()
                for (document in task.result) {
                    val product = document.toObject(Products::class.java)
                    productsList?.add(product)
                }
                productsList?.let {
                    result.invoke(UiState.Success(it))
                }
                } else {
                    result.invoke(UiState.Failed("Failed Fetching Products..."))
                }
            }
    }

//    override suspend fun getProductComments(userList : List<String>, result: (UiState<List<Clients>>) -> Unit) {
//        result.invoke(UiState.Loading)
//        val refList : MutableList<DocumentReference> = mutableListOf()
//        userList.map {
//            refList.add(firestore.collection(CLIENTS_TABLE).document(it))
//        }
//        val writeBatch = firestore.batch()
//        refList.map {
//            writeBatch.
//        }
//        writeBatch.commit().addOnCompleteListener {
//            if (it.isSuccessful) {
//                result.invoke(UiState.Success("Successfully Rated!"))
//            } else {
//                result.invoke(UiState.Failed("Failed to rate"))
//            }
//        }.addOnFailureListener {
//            result.invoke(UiState.Failed(it.message!!))
//        }
//    }


}