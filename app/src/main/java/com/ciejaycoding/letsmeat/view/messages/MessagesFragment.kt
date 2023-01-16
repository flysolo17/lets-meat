package com.ciejaycoding.letsmeat.view.messages

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.databinding.FragmentMessagesBinding
import com.ciejaycoding.letsmeat.models.Messages
import com.ciejaycoding.letsmeat.utils.MESSAGE_TABLE
import com.ciejaycoding.letsmeat.utils.PROJECT_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : Fragment() {
    private lateinit var binding : FragmentMessagesBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var user : FirebaseUser ? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser
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
                    Toast.makeText(binding.root.context,"FAiled to send message! Try Again",Toast.LENGTH_SHORT).show()
                }
            }
    }

}