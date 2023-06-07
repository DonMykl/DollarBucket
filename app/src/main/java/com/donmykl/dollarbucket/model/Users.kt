package com.donmykl.dollarbucket.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Users {
    val name: String? = null
    val amount: Number? = null
    val collected: Number? = null
    val balance:Number? = null
    val date: Timestamp? = null


}