package com.ciejaycoding.letsmeat.repository.cart

import android.net.Uri
import com.ciejaycoding.letsmeat.models.Cart
import com.ciejaycoding.letsmeat.models.CartAndProduct
import com.ciejaycoding.letsmeat.models.Order
import com.ciejaycoding.letsmeat.models.Payment
import com.ciejaycoding.letsmeat.models.Products
import com.ciejaycoding.letsmeat.utils.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.common.io.Files
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.checker.guieffect.qual.UI

class CartRepositoryImpl(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage) : CartRepository {
    private val cartAndProductList  = mutableListOf<CartAndProduct>()
    private val cartList  = mutableListOf<Cart>()
    override suspend fun addToCart(uid: String,cart: Cart,result : (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(CLIENTS_TABLE)
            .document(uid)
            .collection(CLIENTS_CART)
            .document(cart.productID!!)
            .set(cart)
            .addOnCompleteListener { task->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Added!"))
                } else{
                    result.invoke(UiState.Success("Failed to add!"))
                }
            }
    }

    override suspend fun removeToCart(
        uid: String,
        cartID: String,
    ) {
        firestore.collection(CLIENTS_TABLE).document(uid)
            .collection(CLIENTS_CART)
            .document(cartID)
            .delete()
    }

    override  fun getAllCart(uid: String ,result: (UiState<List<Cart>>) -> Unit){
      /*  val query: Query = firestore.collection(CLIENTS_TABLE).document(uid)
            .collection(CLIENTS_CART)
            .orderBy("addedAt",Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<Cart>()
            .setQuery(query, Cart::class.java)
            .build()*/
        result.invoke(UiState.Loading)
        val query: Query = firestore.collection(CLIENTS_TABLE).document(uid)
            .collection(CLIENTS_CART)
            .orderBy("addedAt",Query.Direction.DESCENDING)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cartList.clear()
                for (data in task.result) {
                    val cart = data.toObject(Cart::class.java)
                    cartList.add(cart)
                }
                result.invoke(UiState.Success(cartList))
            } else {
                result.invoke(UiState.Failed("Failed to fetch cart"))
            }
        }

    }

    override suspend fun incrementQuantity(uid: String,cartAndProduct: CartAndProduct) {

            firestore.collection(CLIENTS_TABLE).document(uid)
                .collection(CLIENTS_CART)
                .document(cartAndProduct.cart?.productID!!)
                .update("quantity",FieldValue.increment(1))




    }

    override suspend fun decrementQuantity(uid: String,cartAndProduct: CartAndProduct) {
            firestore.collection(CLIENTS_TABLE).document(uid)
                .collection(CLIENTS_CART)
                .document(cartAndProduct.cart?.productID!!)
                .update("quantity",FieldValue.increment(-1))

    }

    override fun addToCheckout(cartAndProduct: CartAndProduct)  {
        cartAndProductList.add(cartAndProduct)

    }

    override fun removeToCheckOut(cartAndProduct: CartAndProduct){
        cartAndProductList.filter { it.cart?.productID == cartAndProduct.cart?.productID }
            .forEach {
                cartAndProductList.remove(it)
            }
    }

    override fun editToCheckOut(isCheck : Boolean ,cart: Cart) {
      /*  firestore.collection(CLIENTS_TABLE)
            .document(cart.clientID!!)
            .collection(CLIENTS_CART)
            .document(cart.productID!!)
            .update("isChecked" ,isCheck)*/
    }

    override fun getCheckoutList() : List<CartAndProduct> {
        return  cartAndProductList
    }

    override fun checkOut(order: Order, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        order.id = firestore.collection("Orders").document().id
        firestore.collection(ORDER_TABLE)
            .document(order.id!!)
            .set(order).addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Transaction Success!"))
                } else {
                    result.invoke(UiState.Failed("Transaction Failed"))
                }
            }
    }

    override suspend fun uploadGcashReceipt(order: Order, imageUri: Uri, uid: String, result: (UiState<Order>) -> Unit) {
        val storage  = storage.getReference(uid).child(GCASH).child(System.currentTimeMillis().toString() + "." + Files.getFileExtension(
            imageUri.toString())
        )
        result.invoke(UiState.Loading)
        try {
            val uri: Uri = withContext(Dispatchers.IO) {
                storage
                    .putFile(imageUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            order.payment?.details?.image = uri.toString()
            result.invoke(UiState.Success(order))
        } catch (e: FirebaseException){
            result.invoke(UiState.Failed(e.message!!))
        }catch (e: Exception){
            result.invoke(UiState.Failed(e.message!!))
        }
    }

}