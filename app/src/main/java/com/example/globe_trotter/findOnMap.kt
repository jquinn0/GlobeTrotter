package com.example.globe_trotter

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_select_act.*

class findOnMap: AppCompatActivity(), OnMapReadyCallback, OnMapClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var map: GoogleMap
    var number = 1

    companion object latlong{
        var lat=0.00
        var long=0.00
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_select_act)
        Toast.makeText(this, "Select anywhere on map to drop a custom location pin.", Toast.LENGTH_LONG).show()
        val mapFrag = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFrag.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(findOnMap@this)

        fusedLocationClient.lastLocation.addOnSuccessListener {
                location : Location? -> location
            if (location != null) {
                lat = location.latitude
                long = location.longitude
            }
            // Got last known location. In some rare situations this can be null.
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        map.setOnMapClickListener(findOnMap@this)
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(40.00, -90.00)))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onMapClick(p0: LatLng?) {
        map.clear()
        lat = p0?.latitude ?: 0.0
        long = p0?.longitude ?: 0.0
        map.addMarker(MarkerOptions().position(p0!!).title("Selected Point ${number}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
        number++
    }

    fun clickedForLocation(view: View){

        fusedLocationClient.lastLocation.addOnSuccessListener {
                location : Location? -> location
            if (location != null) {
                lat = location.latitude
                long = location.longitude
            }
            // Got last known location. In some rare situations this can be null.
        }

        getLastLocation()
    }


    fun getLastLocation()
    {
        map.clear()
        if(!isLocationEnabled()){
            Toast.makeText(this,"LOCATION NOT ENABLED. COULD NOT GET CURRENT LOCATION", Toast.LENGTH_LONG).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
                location : Location? -> location
            if (location != null) {
                lat = location.latitude
                long = location.longitude
            }
            // Got last known location. In some rare situations this can be null.
        }


        map.addMarker(MarkerOptions().position(LatLng(lat, long)).title("Selected Point ${number}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun makeNewLocation(view: View) {
        if(lat == 0.0 && long == 0.0 || lat == null){
            Toast.makeText(this,"Please Select A Location.", Toast.LENGTH_LONG).show()
            return
        }
        val i = Intent(this, editLocationEntry::class.java)
        startActivity(i)
    }
}