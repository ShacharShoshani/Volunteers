package shachar.afeka.course.volunteers

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.kotlin.searchByTextRequest
import com.google.android.libraries.places.widget.PlaceSearchFragment
import com.google.android.libraries.places.widget.PlaceSearchFragmentListener
import com.google.android.libraries.places.widget.model.MediaSize
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.ui.MapFragment
import shachar.afeka.course.volunteers.ui.MetaData
import shachar.afeka.course.volunteers.utilities.Constants
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager
import java.lang.Exception

class EditOrganizationActivity : AppCompatActivity() {
    private lateinit var _mainFrameMap: FrameLayout
    private lateinit var _mapFragment: MapFragment
    private lateinit var _placeSearchFragment: PlaceSearchFragment
    private lateinit var _placeSearch: SearchView
    private lateinit var _saveBtn: MaterialButton
    private lateinit var _nameInput: TextInputEditText
    private lateinit var _aboutInput: TextInputEditText
    private val _firebaseAuth = FirebaseAuth.getInstance()
    private var _user: FirebaseUser? = null
    private var _location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_organization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        _user = _firebaseAuth.currentUser
        findViews()
        initViews()
    }

    private fun findViews() {
        _mainFrameMap = findViewById(R.id.main_FRAME_map)
        _placeSearch = findViewById(R.id.search_button)
        _saveBtn = findViewById(R.id.organization_save_BTN)
        _nameInput = findViewById(R.id.organization_name_text_input)
        _aboutInput = findViewById(R.id.organization_about_text_input)
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

        _saveBtn.setOnClickListener { _ ->
            if (_user != null) {
                updateControlsEnabledState()

                lifecycleScope.launch {
                    try {
                        DBClient.getInstance().addOrganization(
                            _nameInput.text.toString(),
                            _aboutInput.text.toString(),
                            _location!!.latitude,
                            _location!!.longitude,
                            _user!!.uid
                        )
                    } catch (err: Throwable) {
                        SignalManager.getInstance().toast(err.toString())
                    }
                }.invokeOnCompletion { finish() }
            }
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, _mapFragment)
            .add(R.id.place_search_fragment, _placeSearchFragment)
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
                val location: LatLng? = place.location

                if (location != null) {
                    _mapFragment.zoom(
                        location.latitude,
                        location.longitude,
                        Constants.LocationDefault.ZOOM,
                        place.displayName
                    )
                    _location = location
                }
            }
        })
    }

    private fun updateControlsEnabledState() {
        _placeSearch.isEnabled = !_placeSearch.isEnabled
        _placeSearchFragment.selectable = !_placeSearchFragment.selectable
        _saveBtn.isEnabled = !_saveBtn.isEnabled
    }
}