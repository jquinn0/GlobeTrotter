package com.example.globe_trotter

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.globe_trotte.LocationEntry
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main_map.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap

    companion object{
        var entries = ArrayList<LocationEntry>() // for the entries to put to the map
        var czLat: Double? = null
        var czLong: Double? = null // for the initial camera zoom
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)
        val arrayAd = ArrayAdapter.createFromResource(this, R.array.menuOptions, R.layout.support_simple_spinner_dropdown_item)
        spinner2.adapter = arrayAd
        spinner2.onItemSelectedListener = itemSelected()
        val mapFrag = supportFragmentManager.findFragmentById(R.id.mapView1) as SupportMapFragment
        mapFrag.getMapAsync(this)
    }

    inner class itemSelected: AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if(parent!!.getItemAtPosition(position).toString().isNotEmpty()){
                if (position == 1){
                    check_In()
                }
                if (position == 2){
                    listView()
                }
                if(position == 3){
                    randomLocation()
                }
            }
        }
    }

    private fun randomLocation(){
        spinner2.setSelection(0)
        if(entries.isEmpty()){
            Toast.makeText(this, "Create a location entry first!",Toast.LENGTH_LONG).show()
            return
        }
        val i = entries.size
        val e = entries[Random.nextInt(i)]
        map.moveCamera((CameraUpdateFactory.newLatLngZoom(LatLng(e.lat, e.long), 13.0f)))
        Toast.makeText(this, "Displaying: '" +e.locationName +  "'",Toast.LENGTH_LONG).show()
    }

    fun listView(){
        val i = Intent(this, listView::class.java)
        spinner2.setSelection(0)
        startActivity(i)
    }

    fun check_In() {
        val int = Intent(this, findOnMap::class.java)
        spinner2.setSelection(0)
        startActivity(int)
    }

    override fun onStart() {
        super.onStart()
        mapView1.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView1.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView1.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView1.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mapView1.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        entries.clear()
        val myDB = DataBaseManager(applicationContext)
        val listOfEntries = myDB.selectAll()

        for(i in listOfEntries){
            entries.add(i)
        }

        var score = 0
        for(i in entries){
            map.addMarker( MarkerOptions().position(LatLng(i.lat, i.long)).title(i.locationName).icon(
                    BitmapDescriptorFactory.defaultMarker(i.getColor()))
            )

            score += i.calcScore()
        }
        textView5.text = score.toString()
        if(czLat != null){
            map.moveCamera((CameraUpdateFactory.newLatLngZoom(LatLng(czLat!!, czLong!!), 13.0f)))
            czLat = null
            czLong = null
        }
        else{

            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(40.00, -90.00)))
        }

        map.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            var validator = false
            override fun onMarkerClick(p0: Marker?): Boolean {
                for(i in entries)
                    if(p0?.position?.latitude == i.lat && p0?.position?.longitude == i.long){
                        detailedView.le = i
                        validator = true
                    }
                if(validator == true){
                    var i = Intent(applicationContext, detailedView::class.java)
                    startActivity(i)
                    return true
                }
                return false
            }
        })


    }

    fun help(view: View) {
        val ad = AlertDialog.Builder(MainActivity@this).create()
        ad.setTitle("Help")
        ad.setMessage("- This is the main map where all your location entries are shown.\n\n- Select the drop down bar in the upper right hand corner to view menu options. Here you can add a new entry, view a list of your entries, or go to a random marker on the map.\n\n- Click on a marker to view a location in more detail." +
                "\n\n- The number at the top of the screen represents the total combined score of all of your entries.\n\n- Marker Color Key: \nSuper Location: GOLD\nDifferent Country: ORANGE\nBig City: RED\n" +
                "Different State: PURPLE\nDifferent Region: MAGENTA\nWater Location: LIGHT BLUE\nHome: DARK PINK\nPark/Zoo: GREEN\nDefault Location: DARK BLUE")
        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", DialogInterface.OnClickListener { dialog, which ->  dialog.dismiss()})
        ad.show()
    }
}