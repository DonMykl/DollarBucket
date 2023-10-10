package com.donmykl.dollarbucket.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.donmykl.dollarbucket.databinding.FragmentAddUserListBinding
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddUserListFragment : Fragment() {
    private var _binding: FragmentAddUserListBinding? = null
    private val binding get()  = _binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUserListBinding.inflate(inflater, container, false)
        val view = _binding?.root
        //Calendar
        val c = Calendar.getInstance()
        c.get(Calendar.YEAR)
        c.get(Calendar.MONTH)
        c.get(Calendar.DAY_OF_MONTH)

        val textView: MaterialTextView? = binding?.tvPickedDate
        textView?.text = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(System.currentTimeMillis())
        //val pickDateBtn: ImageButton? = binding?.buttonPickDate

//        val dpd = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            c.set(Calendar.YEAR, year)
//            c.set(Calendar.MONTH, monthOfYear)
//            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            //val myFormat = "dd.MM.yyyy"
            //val sdf = SimpleDateFormat(myFormat, Locale.US)
            //textView.text = sdf.format(c.time)
            //button click to show DatePickerDialog
      //  }
//        pickDateBtn?.setOnClickListener {
//            DatePickerDialog(
//                this.requireContext(), dpd,
//                c.get(Calendar.YEAR),
//                c.get(Calendar.MONTH),
//                c.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
         binding?.buttonSave?.setOnClickListener {

            val nameText = binding?.textUserNameEt?.text.toString()
            val amountText =
                binding?.textUserAmountEt?.text.toString().toLong()
            val collectedText =
                binding?.textUserCollectedEt?.text.toString()
                    .toLong()
            val balanceText =
                binding?.textUserBalanceEt?.text.toString()
                    .toLong()
            val date = Timestamp(c.time)

            saveFireStore(nameText, amountText, collectedText, balanceText, date)

        }
        exitTransition = MaterialFadeThrough()
        return view
    }

    private fun saveFireStore(
        nameText: String,
        amountText: Long,
        collectedText: Long,
        balanceText: Long,
        date: Timestamp,

        ) {
        val db = FirebaseFirestore.getInstance()

        //HashMap that stores the Key-Value pair of the Name and Amount the User inputs
        val user: HashMap<String, Any> = HashMap()
        user["name"] = nameText
        user["amount"] = amountText
        user["collected"] = collectedText
        user["balance"] = balanceText
        user["date"] = date

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(
                    this@AddUserListFragment.requireActivity(),
                    "user added successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@AddUserListFragment.requireActivity(),
                    "user failed to be added",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


