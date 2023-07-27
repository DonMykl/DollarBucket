package com.donmykl.dollarbucket.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

abstract class FirestoreAdapter<VH: RecyclerView.ViewHolder>(
    private val query: Query
) : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {
    //Function to listen to document changes, we call this function in our activity whenever we need to get data
    private var registration: ListenerRegistration? = null
    //Snapshot variable for ArrayList of type DocumentSnapshot that has all the doc snapshots we will work with
    private val snapshots = ArrayList<DocumentSnapshot>()

    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }
    open fun stopListening() {
        if (registration == null) {
            registration?.remove()
            registration = null
        }
        snapshots.clear()
    }

    override fun onEvent(
        documentSnapshots: QuerySnapshot?,
        exception: FirebaseFirestoreException?) {

        //To handle errors
        if (exception != null) {
            Log.e("onEvent:error", exception.toString())
            return
        }
        //Dispatch the event
        if (documentSnapshots != null) {

            for (change in documentSnapshots.documentChanges) {
                //Snapshot of the changed document
                when (change.type) {
                    //New document was added to set of documents matching query
                    DocumentChange.Type.ADDED -> {
                        onDocumentAdded(change)
                    }
                    //New Document within the query was modified
                    DocumentChange.Type.MODIFIED -> {
                        onDocumentModified(change)
                    }
                    //Document removed, deleted, no longer matches the query
                    DocumentChange.Type.REMOVED -> {
                        onDocumentRemoved(change)
                    }
                }
            }
        }
    }
    //Methods for document changes, made to 'snapshots' variable
    protected open fun onDocumentAdded(change: DocumentChange) {
        //Each time changes are made we use their index to do it correctly
        snapshots.add(change.newIndex, change.document)
        //We notify those changes with the index
        notifyItemInserted(change.newIndex)
    }
    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            //Item changed but remained in the same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            //Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }
    protected open fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }
    protected open fun getSnapshot(index: Int): DocumentSnapshot? {
        return snapshots[index]
    }

}