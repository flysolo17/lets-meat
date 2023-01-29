package com.ciejaycoding.letsmeat.view.messages

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.models.Messages
import com.ciejaycoding.letsmeat.models.Staff
import com.ciejaycoding.letsmeat.utils.CLIENTS_TABLE
import com.ciejaycoding.letsmeat.utils.STAFF_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(private val context: Context, private val  messagesList: List<Messages>, private val myID : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SEND_MESSAGE = 0
    private val RECEIVED_MESSAGE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SEND_MESSAGE) {
            MessageSentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_sender, parent, false)
            )

        } else {
            MessageReceivedViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_receiver, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages: Messages = messagesList[position]
        if (holder.itemViewType == SEND_MESSAGE) {
            val sentViewHolder = holder as MessageSentViewHolder
            sentViewHolder.message.text = messages.message
            sentViewHolder.timestamp.text = timestampToDate(messages.dateSend!!)

        } else {
            val receivedViewHolder = holder as MessageReceivedViewHolder
            receivedViewHolder.message.text = messages.message
            receivedViewHolder.timestamp.text = messages.dateSend?.let { timestampToDate(it) }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val senderID: String? = messagesList[position].sender
        return if (myID == senderID) {
            SEND_MESSAGE
        } else {
            RECEIVED_MESSAGE
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    internal class MessageSentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var timestamp: TextView

        var firestore : FirebaseFirestore
        init {
            message = itemView.findViewById(R.id.textMessage)
            timestamp = itemView.findViewById(R.id.textMessageDate)
            firestore = FirebaseFirestore.getInstance()
        }

    }

    internal class MessageReceivedViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var timestamp: TextView
        var firestore : FirebaseFirestore
        init {
            message = itemView.findViewById(R.id.textMessage)
            timestamp = itemView.findViewById(R.id.textMessageDate)
            firestore = FirebaseFirestore.getInstance()
        }


    }

/*    fun displayMyProfile(myID: String) {
        firestore.collection(CLIENTS_TABLE)
            .document(myID)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data = it.toObject(Clients::class.java)
                    data?.let { client ->
                        if (!client.profile.isNullOrEmpty()) {
                            Glide.with(itemView.context).load(it).into(imageProfile)
                        }
                    }
                }
            }
    }*/
    @SuppressLint("SimpleDateFormat")
    private fun timestampToDate(timestamp: Long): String {
        val pattern = "EE MMM dd, HH:mm aa"
        val date = Date(timestamp)
        val format: Format = SimpleDateFormat(pattern)
        return format.format(date)
    }


}
