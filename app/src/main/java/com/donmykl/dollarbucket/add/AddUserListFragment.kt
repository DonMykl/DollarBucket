package com.donmykl.dollarbucket.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.donmykl.dollarbucket.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddUserListFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_user_list,container,false)
        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val textView: TextView = view.findViewById(R.id.tv_picked_date)
        textView.text = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(System.currentTimeMillis())
        val pickDateBtn : ImageButton = view.findViewById(R.id.button_pick_date)

        val dpd = DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            //val myFormat = "dd.MM.yyyy"
            //val sdf = SimpleDateFormat(myFormat, Locale.US)
            //textView.text = sdf.format(c.time)
            //button click to show DatePickerDialog
        }
        pickDateBtn.setOnClickListener{
            DatePickerDialog(this.requireContext(),dpd,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show()
        }
        val button: Button = view.findViewById(R.id.button_save)
        button.setOnClickListener {

            val nameText = view.findViewById<TextInputEditText>(R.id.textUserNameEt).text.toString()
            val amountText = view.findViewById<TextInputEditText>(R.id.textUserAmountEt).text.toString().toLong()
            val collectedText = view.findViewById<TextInputEditText>(R.id.textUserCollectedEt).text.toString().toLong()
            val balanceText = view.findViewById<TextInputEditText>(R.id.textUserBalanceEt).text.toString().toLong()
            val date = Timestamp(c.time)

            saveFireStore(nameText, amountText, collectedText, balanceText, date)

        }
        exitTransition = MaterialFadeThrough()
        return view
    }
    fun saveFireStore(
        nameText: String,
        amountText: Long,
        collectedText: Long,
        balanceText: Long,
        date: Timestamp,

        ) {
        val db = FirebaseFirestore.getInstance()

        //HashMap that stores the Key-Value pair of the Name and Amount the User inputs
        val user : HashMap <String, Any> = HashMap()
        user.put("name", nameText)
        user.put("amount", amountText)
        user.put("collected", collectedText)
        user.put("balance", balanceText)
        user.put("date", date)

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@AddUserListFragment.requireActivity(), "user added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener{
                Toast.makeText(this@AddUserListFragment.requireActivity(), "user failed to be added", Toast.LENGTH_SHORT)
                    .show()

            }

    }
}


