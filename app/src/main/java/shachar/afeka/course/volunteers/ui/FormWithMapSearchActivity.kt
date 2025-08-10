package shachar.afeka.course.volunteers.ui

import android.os.Bundle
import android.view.View
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
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.interfaces.IViewReadyCallback
import shachar.afeka.course.volunteers.models.Coordinates
import shachar.afeka.course.volunteers.utilities.Constants
import shachar.afeka.course.volunteers.utilities.MetaData
import shachar.afeka.course.volunteers.utilities.SignalManager
import java.lang.Exception

abstract class FormWithMapSearchActivity(
    val layoutId: Int,
    val mapFrameId: Int,
    val placeSearchFragmentContainerId: Int,
    val placeSearchViewId: Int
) :
    AppCompatActivity() {
    protected lateinit var mainFrameMap: FrameLayout
    protected lateinit var mapFragment: MapFragment
    protected lateinit var placeSearchFragment: PlaceSearchFragment
    protected lateinit var placeSearch: SearchView
    protected var location: Coordinates = Coordinates.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutId)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    protected open fun findViews() {
        mainFrameMap = findViewById(mapFrameId)
        placeSearch = findViewById(placeSearchViewId)
    }

    protected open fun initViews() {
        initMapFragment()
        initSearchFragment()
        initPlaceSearch()

        supportFragmentManager
            .beginTransaction()
            .add(mapFrameId, mapFragment)
            .add(placeSearchFragmentContainerId, placeSearchFragment)
            .commit()
    }

    private fun initMapFragment() {
        mapFragment = MapFragment()

        mapFragment.viewReadyCallback = object : IViewReadyCallback {
            override fun viewReady(view1: View) {
                val mapFragmentLayoutParams = view1.layoutParams
                mapFragmentLayoutParams.height = (400 * resources.displayMetrics.density).toInt()
                view1.layoutParams = mapFragmentLayoutParams
                view1.requestLayout()
            }
        }
    }

    private fun initPlaceSearch() {
        placeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && !query.isEmpty()) {
                    try {
                        val search = searchByTextRequest(
                            query, listOf(Place.Field.FORMATTED_ADDRESS)
                        )

                        placeSearchFragment.configureFromSearchByTextRequest(search)
                    } catch (err: Throwable) {
                        SignalManager.getInstance().toast(err.toString())
                    }
                }

                return true
            }

        })
    }

    private fun initSearchFragment() {
        val apiKey = MetaData.getGoogleGeoAPIKey(this.applicationContext)

        if (apiKey == null)
            throw Error("Google API key is missing.")

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placeSearchFragment =
            PlaceSearchFragment.newInstance(PlaceSearchFragment.STANDARD_CONTENT)

        placeSearchFragment.preferTruncation = false
        placeSearchFragment.mediaSize = MediaSize.SMALL
        placeSearchFragment.selectable = true

        placeSearchFragment.registerListener(object : PlaceSearchFragmentListener {
            override fun onLoad(places: List<Place>) {

            }

            override fun onRequestError(e: Exception) {

            }

            override fun onPlaceSelected(place: Place) {
                super.onPlaceSelected(place)
                if (place.location != null) {
                    mapFragment.zoom(
                        place.location!!.latitude,
                        place.location!!.longitude,
                        Constants.LocationDefault.ZOOM,
                        place.displayName
                    )
                    location =
                        Coordinates(place.location!!.latitude, place.location!!.longitude)
                }
            }
        })
    }
}