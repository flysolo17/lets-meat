package com.ciejaycoding.letsmeat.view.product.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.ImageHeaderParser
import com.ciejaycoding.letsmeat.R
import com.ciejaycoding.letsmeat.models.Clients
import com.ciejaycoding.letsmeat.models.Comments
import com.ciejaycoding.letsmeat.utils.CLIENTS_TABLE
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(val context : Context,val commentList : List<Comments>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_comments,parent,false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment : Comments = commentList[position]
        holder.rating.rating = comment.rating!!
        holder.comment.text = comment.comment
        comment.clientID?.let { holder.getClientInfo(it) }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val profile : CircleImageView = itemView.findViewById(R.id.imageProfile)
        val rating : RatingBar = itemView.findViewById(R.id.ratingBar)
        val name : TextView = itemView.findViewById(R.id.textClientName)
        val comment : TextView = itemView.findViewById(R.id.textComment)

        val firestore = FirebaseFirestore.getInstance()

        fun getClientInfo(id : String ) {
            firestore.collection(CLIENTS_TABLE)
                .document(id)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val client : Clients? = it.toObject(Clients::class.java)
                        client?.let { it ->
                            name.text = it.fullname
                            it.profile?.let {
                                Glide.with(itemView.context).load(it).into(profile)
                            }
                        }
                    } else {
                        name.text = "No User"
                    }
                }
        }
    }

}