package shachar.afeka.course.volunteers

import android.os.Bundle
import android.view.MenuItem
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
    private lateinit var open_menu_btn: FloatingActionButton
    private lateinit var navigation_view: NavigationView

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
        val firebaseAuth = FirebaseAuth.getInstance();
        val user = firebaseAuth.currentUser

        open_menu_btn.setOnClickListener { _: View ->
            val drawerLayout: DrawerLayout = findViewById(R.id.main)
            if(!drawerLayout.isDrawerOpen(GravityCompat.END))
            {
                drawerLayout.openDrawer(GravityCompat.END)
            }
            else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

        }

        navigation_view.setNavigationItemSelectedListener {menuItem ->
           when(menuItem.itemId){
               R.id.menu_item_sign_out->{
                   if(user != null) {
                       firebaseAuth.signOut()
                       finish()
                   }
               }
           }
            true
        }
    }

    private fun findViews() {
        open_menu_btn = findViewById(R.id.open_menu_btn)
        navigation_view = findViewById(R.id.nav_view)
    }
}