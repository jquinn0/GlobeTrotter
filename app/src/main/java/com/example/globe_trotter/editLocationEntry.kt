package com.example.globe_trotter

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.globe_trotte.LocationEntry
import kotlinx.android.synthetic.main.edit_location_entry.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class editLocationEntry: AppCompatActivity(){
    var myFile: Uri? = null

    companion object latlong{
        var nums = ArrayList<Int>()
        var le = LocationEntry(0, "", 0.0, 0.0, "","", "")
        var toEdit: LocationEntry? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_location_entry)
        val arrayAd = ArrayAdapter.createFromResource(this, R.array.locationTypes, R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = arrayAd
        spinner.onItemSelectedListener = itemSelected()
        calendarView.setOnDateChangeListener(){ _, y, m, d ->
            var months = m + 1
            var date = months.toString() +"/" +  d.toString() +"/" + y.toString()
            date_view.text = date
        }
        if(toEdit != null){
            date_view.text = toEdit!!.date
            myFile = Uri.parse(toEdit!!.pic)
            imageView.setImageURI(myFile)
            Toast.makeText(editLocationEntry@this, "Editing '${toEdit!!.locationName}'", Toast.LENGTH_LONG).show()
        }
    }

    fun openGall(view: View) {
        val gall = Intent(Intent.ACTION_PICK)
        gall.type = "image/*"
        startActivityForResult(gall, 3)
    }

    fun takePic(view: View) {
        val myIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val photoFile= File.createTempFile("JPEG_$timeStamp",".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        val photoUri= FileProvider.getUriForFile(this,"com.example.android.fileprov",photoFile)
        myFile=photoUri
        myIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(myIntent,2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2) // pic
        {
            imageView.setImageURI(myFile)
        }
        if(requestCode==3){ // gallery
            val imageUri: Uri? = data?.data
            myFile = imageUri
            imageView.setImageURI(myFile)
        }

    }

    inner class itemSelected: AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent!!.getItemAtPosition(position).toString().isNotEmpty()) {
                if(nums.contains(position) && position != 0){
                    nums.remove(position)
                    Toast.makeText(applicationContext, "" + parent.getItemAtPosition(position).toString() + " trait removed.", Toast.LENGTH_LONG).show()
                }
                else{
                    if(position == 0 && nums.contains(0)){
                        // already added
                    }
                    else {
                        nums.add(position)
                        if (position != 0) {
                            Toast.makeText(
                                applicationContext,
                                "" + parent.getItemAtPosition(position).toString() + " trait added.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
                }
                var s = ""
                for(i in nums){
                    s += traitToString(i) + "\n"
                }
                textView4.text = s
                spinner.setSelection(0)
            }
    }

     fun traitToString(i: Int): String{
        when(i){
            0 -> return "New Location"
            1 -> return "Home"
            2-> return "Different State"
            3-> return "Different Country"
            4-> return "Different Region"
            5-> return "Entertainment Venue"
            6-> return "Famous Landmark"
            7-> return "Restaurant/Bar"
            8-> return "Tropical/Mediterranean Climate"
            9-> return "Average City"
            10-> return "Big City"
            11-> return "Park/Zoo"
            12-> return "Beach/Lake/River"
            13-> return "Desert Climate"
        }
        return ""
    }

    inner class myAsync: AsyncTask<LocationEntry, Unit, Unit>() {
        override fun doInBackground(vararg p0: LocationEntry) {
            val myDb= DataBaseManager(applicationContext)
            myDb.writableDatabase
            myDb.insert(p0[0])
            return
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            finish()
        }
    }

    inner class myAsync2: AsyncTask<LocationEntry, Unit, Unit>() {
        override fun doInBackground(vararg p0: LocationEntry) {
            val myDb= DataBaseManager(applicationContext)
            myDb.writableDatabase
            myDb.modify(p0[0])
            return
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            finish()
        }
    }

    fun confirmEntry(view: View) {
        if(locationName.text.toString().isEmpty()){
            Toast.makeText(this, "NAME FIELD CANT BE EMPTY", Toast.LENGTH_LONG).show()
            return
        }
        if(date_view.text.toString().isEmpty() || date_view.text.toString() == "DATE FIELD CANT BE EMPTY"){
            Toast.makeText(this, "DATE FIELD CANT BE EMPTY", Toast.LENGTH_LONG).show()
            date_view.text = "DATE FIELD CANT BE EMPTY"
            return
        }
        if(toEdit != null){
            toEdit!!.locationName = locationName.text.toString()
            toEdit!!.date = date_view.text.toString()

            if (myFile != null){
                toEdit!!.pic = myFile.toString()
            }
            var s = ""
            for(i in nums){
                s += (i.toString() + ",")
            }
            nums.clear()
            toEdit!!.traits = s
            myAsync2().execute(toEdit)
            toEdit = null
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            return
        }
        le.lat = findOnMap.lat
        le.long = findOnMap.long
        le.locationName = locationName.text.toString()
        le.date = date_view.text.toString()

        if (myFile != null){
            le.pic = myFile.toString()
        }
        var s = ""
        for(i in nums){
            s += (i.toString() + ",")
        }
        nums.clear()
        le.traits = s
        myAsync().execute(le)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}