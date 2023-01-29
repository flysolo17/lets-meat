package com.ciejaycoding.letsmeat.view.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.databinding.FragmentMessagesBinding
import com.ciejaycoding.letsmeat.models.Messages
import com.ciejaycoding.letsmeat.models.Staff
import com.ciejaycoding.letsmeat.utils.MESSAGE_TABLE
import com.ciejaycoding.letsmeat.utils.PROJECT_ID
import com.ciejaycoding.letsmeat.utils.STAFF_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : Fragment() {
    private lateinit var binding : FragmentMessagesBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var user : FirebaseUser ? = null;
    private lateinit var messageList : MutableList<Messages>
    private lateinit var messagesAdapter: MessagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageList = mutableListOf()

        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            messagesAdapter = MessagesAdapter(view.context,messageList,it.uid)
            getAllMessage(it.uid)
        }

        val linearLayoutManager = LinearLayoutManager(view.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerviewMessages.apply {
            layoutManager = linearLayoutManager
            adapter = messagesAdapter
        }

        binding.buttonSend.setOnClickListener {
            val message = binding.edtMessage.text.toString()
            if (message.isNotEmpty()) {
                user?.let { user ->
                    val messages = Messages(user.uid, PROJECT_ID, message, System.currentTimeMillis())
                    sendMessage(messages)
                }
              return@setOnClickListener
            }
            Toast.makeText(view.context,"Invalid Message",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(message: Messages) {
        firestore.collection(MESSAGE_TABLE)
            .add(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.edtMessage.setText("")
                    Toast.makeText(binding.root.context,"Message Sent!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context,"Failed to send message! Try Again",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun getAllMessage(uid : String) {
        firestore.collection(MESSAGE_TABLE)
            .orderBy("dateSend",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                value?.let {
                    messageList.clear()
                    for (snapshot in it.documents) {
                        val message : Messages ? = snapshot.toObject(Messages::class.java)
                        if (message != null) {
                            if (message.sender == uid && message.receiver == PROJECT_ID || message.sender == PROJECT_ID && message.receiver == uid) {
                                messageList.add(message)
                                messagesAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
    }


}