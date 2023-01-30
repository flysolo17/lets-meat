package com.ciejaycoding.letsmeat.view.home.indicators

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ciejaycoding.letsmeat.models.Products

class IndicatorAdapter(fragment: Fragment, private val categories: List<String>, private val productList : ArrayList<Products>) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
       return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = MenuProductsFragment()
        val array = arrayListOf<Products>()
        if (position == 0) {
            array.addAll(productList)
        } else {
            array.addAll(productList.filter { it.category == categories[position]})
        }

        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION,position)
            putParcelableArrayList(ARG_PRODUCTS,array)
            putParcelableArrayList(ARG_TRANSACTIONS,array)
        }
        return fragment
    }
    companion object {
        const val ARG_POSITION = "POSITION"
        const val ARG_PRODUCTS ="PRODUCTS"
        const val ARG_TRANSACTIONS = "TRANSACTIONS"
    }

}