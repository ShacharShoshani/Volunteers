package shachar.afeka.course.volunteers.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.utilities.Constants
import shachar.afeka.course.volunteers.utilities.SignalManager


class MapFragment(
    lat: Double = Constants.LocationDefault.LATITUDE,
    lon: Double = Constants.LocationDefault.LONGITUDE,
    zoom: Float = Constants.LocationDefault.ZOOM,
    title: String? = null
) : Fragment() {

    private var mapClient: GoogleMap? = null

    private val callback =
        { googleMap: GoogleMap ->
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            mapClient = googleMap
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                tryEnableMyLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constants.PermissionCodes.LOCATION
                )

                tryEnableMyLocation()
            }

            zoom(lat, lon, zoom, title)
        }

    fun tryEnableMyLocation() {
        try {
            mapClient?.isMyLocationEnabled = true
        } catch (_: SecurityException) {
            SignalManager.getInstance()
                .toast("Failed to get current location.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun zoom(
        lat: Double = Constants.LocationDefault.LATITUDE,
        lon: Double = Constants.LocationDefault.LONGITUDE,
        zoom: Float = Constants.LocationDefault.ZOOM,
        title: String? = null
    ) {
        val location = LatLng(lat, lon)

        val markerConfig = MarkerOptions().position(location).title(title)

        mapClient?.addMarker(markerConfig)

        mapClient?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }
}