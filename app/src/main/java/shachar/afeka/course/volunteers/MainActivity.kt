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
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var _openMenuBtn: FloatingActionButton
    private lateinit var _navigationView: NavigationView

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
        initViews()

    }

    private fun initViews() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        _openMenuBtn.setOnClickListener { _: View ->
            val drawerLayout: DrawerLayout = findViewById(R.id.main)
            if(!drawerLayout.isDrawerOpen(GravityCompat.END))
            {
                drawerLayout.openDrawer(GravityCompat.END)
            }
            else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

        }

        _navigationView.setNavigationItemSelectedListener { menuItem ->
           when(menuItem.itemId){
               R.id.menu_item_sign_out->{
                   if(user != null) {
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
    }
}