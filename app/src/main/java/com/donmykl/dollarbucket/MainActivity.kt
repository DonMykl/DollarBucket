package com.donmykl.dollarbucket

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.donmykl.dollarbucket.user.UserFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar        : MaterialToolbar
    private lateinit var navController  : NavController
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout   : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val query: Query = FirebaseFirestore.getInstance().collection("users")

        // Initialize Views
        toolbar         = findViewById(R.id.activity_main_toolbar)
        navigationView  = findViewById(R.id.nav_view)
        drawerLayout    = findViewById(R.id.drawer_layout)

        // Get NavHostFragment and NavController
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController   = navHostFrag.navController

        //Define AppBarConfiguration : Connect to Drawer layout with Navigation Component
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        //Connect Toolbar with NavController
        toolbar.setupWithNavController(navController, appBarConfiguration)

        //Connect NavigationView with NavController
        navigationView.setupWithNavController(navController)

        //Floating action button that launches the add user Fragment(MainActivity)
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            /*val activityIntent = Intent(this, UserFragment::class.java)
            startActivity(activityIntent)*/
        }
        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val textView: TextView = findViewById(R.id.tv_picked_date)
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        val pickDateBtn : ImageButton = findViewById(R.id.button_pick_date)

        val dpd = DatePickerDialog.OnDateSetListener{view, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(c.time)
            //button click to show DatePickerDialog
        }
        pickDateBtn.setOnClickListener{
            DatePickerDialog(this,dpd,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)).show()
        }

        val button: Button = findViewById(R.id.button_save)
        button.setOnClickListener {

            val nameText = findViewById<EditText>(R.id.textUserName).text.toString()
            val amountText = findViewById<EditText>(R.id.textUserAmount).text.toString().toInt()
            val collectedText = findViewById<EditText>(R.id.textUserCollected).text.toString().toInt()
            val balanceText = findViewById<EditText>(R.id.textUserBalance).text.toString().toInt()
            // Code here executes on main thread after user presses button
            //val nameText = userNameText.text.toString()
            //val amountText = userAmountText.text.toString()
            //val collectedText = userCollectedText.text.toString()
            //val balanceText = userBalanceText.text.toString()
            val date = Timestamp(c.time)

            saveFireStore(nameText, amountText, collectedText, balanceText, date)

        }
    }

    fun saveFireStore(
        nameText: String,
        amountText: Number,
        collectedText: Number,
        balanceText: Number,
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
                Toast.makeText(this@MainActivity, "user added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener{Toast.makeText(this@MainActivity, "user failed to be added", Toast.LENGTH_SHORT)
                .show()

            }

    }
    override fun onBackPressed() {
        if (drawerLayout.isOpen) {
            drawerLayout.close()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}