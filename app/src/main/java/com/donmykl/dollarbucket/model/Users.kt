package com.donmykl.dollarbucket.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Users (
    var name: String? = null,
    var amount: Long? = null,
    var collected: Long? = null,
    var balance:Long? = null,
    var date: Timestamp? = null,
) : Parcelable