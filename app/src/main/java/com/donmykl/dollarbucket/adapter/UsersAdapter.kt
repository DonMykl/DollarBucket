package com.donmykl.dollarbucket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donmykl.dollarbucket.R
import com.donmykl.dollarbucket.model.Users
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class UsersAdapter(query: Query) : FirestoreAdapter<UsersAdapter.UsersViewHolder>(query) {
    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.textName)
        private val amount: TextView = itemView.findViewById(R.id.textAmount)
        private val collected: TextView = itemView.findViewById(R.id.textCollected)
        private val balance: TextView = itemView.findViewById(R.id.textBalance)
        private val date: TextView = itemView.findViewById(R.id.textDate)

        fun bind(snapshot: DocumentSnapshot) {
            val users: Users? = snapshot.toObject(Users::class.java)
            name.text = users?.name
            amount.text = users?.amount.toString()
            collected.text = users?.collected.toString()
            balance.text = users?.balance.toString()
            date.text = users?.date?.toDate().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot -> holder.bind(snapshot) }
    }

    fun deleteItem(position: Int) {
        getSnapshot(position)?.reference?.delete()
    }

}