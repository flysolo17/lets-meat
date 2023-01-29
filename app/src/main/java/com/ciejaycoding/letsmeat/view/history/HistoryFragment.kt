package com.ciejaycoding.letsmeat.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentHistoryBinding
import com.ciejaycoding.letsmeat.models.Transaction
import com.ciejaycoding.letsmeat.utils.TRANSACTION_TABLE
import com.ciejaycoding.letsmeat.view.orders.adapters.TransactionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query



class HistoryFragment : Fragment() , TransactionAdapter.TransactionClickListener {
    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var binding : FragmentHistoryBinding
    private lateinit var firestore : FirebaseFirestore
    private lateinit var transactionList : MutableList<Transaction>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore =  FirebaseFirestore.getInstance()
        transactionList = mutableListOf()
        FirebaseAuth.getInstance().currentUser?.let {
            getAllTransactions(it.uid)
        }
        transactionAdapter = TransactionAdapter(view.context,transactionList,0,this)
        binding.recyclerviewTransaction.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = transactionAdapter
        }
    }
    private fun getAllTransactions(uid : String) {
        firestore.collection(TRANSACTION_TABLE)
            .whereEqualTo("clientID",uid)
            .orderBy("date",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                value?.let {
                    transactionList.clear()
                    it.documents.map { doc ->
                        val transactions : Transaction? = doc.toObject(Transaction::class.java)
                        transactions?.let { transaction ->
                            transactionList.add(transaction)
                            transactionAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    override fun viewTransaction(id: String) {
        val directions = HistoryFragmentDirections.actionHistoryFragmentToViewTransactionFragment(id)
        findNavController().navigate(directions)
    }
}