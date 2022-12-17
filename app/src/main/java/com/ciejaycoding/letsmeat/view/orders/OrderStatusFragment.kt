package com.ciejaycoding.letsmeat.view.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentOrderStatusBinding
import com.ciejaycoding.letsmeat.models.Order


class OrderStatusFragment : Fragment() {

    private lateinit var binding : FragmentOrderStatusBinding
    private var orders: List<Order> ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orders =it.getParcelableArrayList("orders")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderStatusBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewOrders.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = PurchasesAdapter(view.context,orders!!)
        }
    }
}