package shachar.afeka.course.volunteers

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.kotlin.searchByTextRequest
import com.google.android.libraries.places.widget.PlaceSearchFragment
import com.google.android.libraries.places.widget.PlaceSearchFragmentListener
import com.google.android.libraries.places.widget.model.MediaSize
import shachar.afeka.course.volunteers.ui.MapFragment
import shachar.afeka.course.volunteers.ui.MetaData
import shachar.afeka.course.volunteers.utilities.Constants
import shachar.afeka.course.volunteers.utilities.SignalManager
import java.lang.Exception

class EditOrganizationActivity : AppCompatActivity() {
    private lateinit var _mainFrameMap: FrameLayout
    private lateinit var _mapFragment: MapFragment
    private lateinit var _placeSearchFragment: PlaceSearchFragment
    private lateinit var _placeSearch: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_organization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
    }

    private fun findViews() {
        _mainFrameMap = findViewById(R.id.main_FRAME_map)
        _placeSearch = findViewById(R.id.search_button)
    }

    private fun initViews() {
        _mapFragment = MapFragment()
        initSearchFragment()

        _placeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && !query.isEmpty()) {
                    try {
                        val search = searchByTextRequest(
                            query, listOf(Place.Field.FORMATTED_ADDRESS)
                        )

                        _placeSearchFragment.configureFromSearchByTextRequest(search)
                    } catch (err: Throwable) {
                        SignalManager.getInstance().toast(err.toString())
                    }
                }

                return true
            }

        })

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, _mapFragment)
            .replace(R.id.place_search_fragment, _placeSearchFragment)
            .commit()
    }

    private fun initSearchFragment() {
        val apiKey = MetaData.getGoogleGeoAPIKey(this.applicationContext)

        if (apiKey == null)
            throw Error("Google API key is missing.")

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        _placeSearchFragment =
            PlaceSearchFragment.newInstance(PlaceSearchFragment.STANDARD_CONTENT)

        _placeSearchFragment.preferTruncation = false
        _placeSearchFragment.mediaSize = MediaSize.SMALL
        _placeSearchFragment.selectable = true

        _placeSearchFragment.registerListener(object : PlaceSearchFragmentListener {
            override fun onLoad(places: List<Place>) {

            }

            override fun onRequestError(e: Exception) {

            }

            override fun onPlaceSelected(place: Place) {
                super.onPlaceSelected(place)
                val location = place.location

                if (location != null) {
                    _mapFragment.zoom(
                        location.latitude,
                        location.longitude,
                        Constants.LocationDefault.ZOOM,
                        place.displayName
                    )
                }
            }
        })
    }
}