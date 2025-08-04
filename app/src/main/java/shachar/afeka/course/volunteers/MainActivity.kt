package shachar.afeka.course.volunteers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import shachar.afeka.course.volunteers.utilities.SignalManager

class MainActivity : AppCompatActivity() {
    private lateinit var _openMenuBtn: FloatingActionButton
    private lateinit var _navigationView: NavigationView
    private lateinit var _mainLayout: DrawerLayout
    private var user: FirebaseUser? = null
    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()

        user = firebaseAuth.currentUser

        initViews()
        testDB()
    }

    private fun testDB() {
        if (user == null)
            return

        db.collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener { result ->
                if (!result.exists())
                    SignalManager.getInstance().toast("More detail are required.")
                else
                    SignalManager.getInstance().toast(result!!.getString("residence")!!)
            }
            .addOnFailureListener { exception ->
                if (exception.message != null)
                    SignalManager.getInstance().toast(exception.message!!)
            }
    }

    private fun initViews() {


        if (user != null)
            SignalManager.getInstance().toast(buildString {
                append("Hello ")
                append(user?.displayName)
                append("!")
            })

        _openMenuBtn.setOnClickListener { _: View ->
            if (!_mainLayout.isDrawerOpen(GravityCompat.END)) {
                _mainLayout.openDrawer(GravityCompat.END)
            } else {
                _mainLayout.closeDrawer(GravityCompat.START)
            }

        }

        _navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_sign_out -> {
                    if (user != null) {
                        firebaseAuth.signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
            true
        }
    }

    private fun findViews() {
        _openMenuBtn = findViewById(R.id.open_menu_btn)
        _navigationView = findViewById(R.id.nav_view)
        _mainLayout = findViewById(R.id.main)
    }
}