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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class MainActivity : AppCompatActivity() {
    private lateinit var _openMenuBtn: FloatingActionButton
    private lateinit var _navigationView: NavigationView
    private lateinit var _mainLayout: DrawerLayout

    private lateinit var _nameInput: TextInputEditText
    private lateinit var _residenceInput: TextInputEditText
    private lateinit var _emailInput: TextInputEditText
    private lateinit var _phoneInput: TextInputEditText
    private lateinit var _saveBtn: MaterialButton


    private var user: FirebaseUser? = null
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

        if (user != null)
            lifecycleScope.launch {
                loadUserProfile()
            }
    }

    private fun initViews() {


        if (user != null) SignalManager.getInstance().toast(buildString {
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

        _saveBtn.setOnClickListener { _ ->
            if (user != null)
                lifecycleScope.launch {
                    DBClient.getInstance().updateUser(
                        user!!.uid,
                        _nameInput.text.toString(),
                        _emailInput.text.toString(),
                        _phoneInput.text.toString(),
                        _residenceInput.text.toString()
                    )
                }
        }
    }

    private fun findViews() {
        _openMenuBtn = findViewById(R.id.open_menu_btn)
        _navigationView = findViewById(R.id.nav_view)
        _mainLayout = findViewById(R.id.main)

        _nameInput = findViewById(R.id.username_text_input)
        _residenceInput = findViewById(R.id.residence_text_input)
        _emailInput = findViewById(R.id.email_text_input)
        _phoneInput = findViewById(R.id.phone_text_input)
        _saveBtn = findViewById(R.id.save_BTN)
    }

    private suspend fun loadUserProfile() {
        val profile = DBClient.getInstance().getUserByUID(user!!.uid)

        if (profile == null)
            return

        _nameInput.setText(profile.name)
        _residenceInput.setText(profile.residence)
        _emailInput.setText(profile.email)
        _phoneInput.setText(profile.phone)
    }
}