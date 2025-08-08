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
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import shachar.afeka.course.volunteers.ui.OrganizationsListFragment
import shachar.afeka.course.volunteers.ui.UserEditFragment
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class MainActivity : AppCompatActivity() {
    private lateinit var _openMenuBtn: FloatingActionButton
    private lateinit var _navigationView: NavigationView
    private lateinit var _mainLayout: DrawerLayout
    private lateinit var _tabLayout: TabLayout
    private lateinit var _viewPager: ViewPager2

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

        SignalManager.init(this)
        DBClient.init()
        user = firebaseAuth.currentUser

        initViews()
    }

    private fun initViews() {
        _viewPager.adapter = ViewPageAdapter(this)

        TabLayoutMediator(_tabLayout, _viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My Profile"
                1 -> "My Organizations"
                else -> null
            }
        }.attach()

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
        _tabLayout = findViewById(R.id.tab_layout)
        _viewPager = findViewById(R.id.view_pager)
    }

    inner class ViewPageAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserEditFragment()
                1 -> OrganizationsListFragment()
                else -> throw IllegalStateException("Invalid position.")
            }
        }
    }
}