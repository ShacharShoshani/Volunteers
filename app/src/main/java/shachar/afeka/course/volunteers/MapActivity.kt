package shachar.afeka.course.volunteers

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import shachar.afeka.course.volunteers.ui.MapFragment
import shachar.afeka.course.volunteers.utilities.Constants

class MapActivity : AppCompatActivity() {
    private lateinit var _mainFrameMap: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun initViews() {
        val lat =
            intent.getDoubleExtra(R.string.param_lat.toString(), Constants.LocationDefault.LATITUDE)

        val lon =
            intent.getDoubleExtra(
                R.string.param_lon.toString(),
                Constants.LocationDefault.LONGITUDE
            )

        val title = intent.getStringExtra(R.string.param_title.toString())

        val mapFragment = MapFragment(lat, lon, Constants.LocationDefault.ZOOM, title)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()
    }

    private fun findViews() {
        _mainFrameMap = findViewById(R.id.main_FRAME_map)
    }
}