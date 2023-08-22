package com.donmykl.dollarbucket.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.donmykl.dollarbucket.R
import com.donmykl.dollarbucket.adapter.UsersAdapter
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class UserFragment : Fragment() {

    private lateinit var adapter: UsersAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val query: Query = FirebaseFirestore.getInstance().collection("users")
            .orderBy("name",Query.Direction.ASCENDING)
        val recyclerView: RecyclerView = view.findViewById(R.id.listItems)
        recyclerView.setHasFixedSize(true)
        adapter = UsersAdapter(query)
        recyclerView.adapter = adapter
        enterTransition = MaterialFadeThrough()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false //Not yet supporting moving items up and down
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)
        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}