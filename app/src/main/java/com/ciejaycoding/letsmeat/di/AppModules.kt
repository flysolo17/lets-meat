package com.ciejaycoding.letsmeat.di

import android.content.Context
import com.ciejaycoding.letsmeat.repository.ProductRepository
import com.ciejaycoding.letsmeat.repository.ProductRepositoryImpl
import com.ciejaycoding.letsmeat.repository.auth.AuthRepository
import com.ciejaycoding.letsmeat.repository.auth.AuthRepositoryImpl
import com.ciejaycoding.letsmeat.repository.cart.CartRepository
import com.ciejaycoding.letsmeat.repository.cart.CartRepositoryImpl
import com.ciejaycoding.letsmeat.repository.purchases.PurchasesRepository
import com.ciejaycoding.letsmeat.repository.purchases.PurchasesRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Singleton
    @Provides
    fun provideProductRepository() : ProductRepository {
        return ProductRepositoryImpl(FirebaseFirestore.getInstance())
    }
    @Singleton
    @Provides
    fun provideAuthRepository(@ApplicationContext context: Context) : AuthRepository {
        return AuthRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance(),
            FirebaseStorage.getInstance(),context)
    }

    @Singleton
    @Provides
    fun provideCartRepository() : CartRepository {
        return CartRepositoryImpl(FirebaseFirestore.getInstance())
    }

    @Singleton
    @Provides
    fun providePurchasesRepository() : PurchasesRepository {
        return PurchasesRepositoryImpl(FirebaseFirestore.getInstance())
    }
}