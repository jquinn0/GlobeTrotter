package com.example.globe_trotter

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.LocaleList
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.globe_trotte.LocationEntry
import kotlinx.android.synthetic.main.detail_view.*

class detailedView : AppCompatActivity() {
    companion object{
        var le: LocationEntry? = null

        fun convert(s: List<String>): ArrayList<String>{
            var toR = ArrayList<String>()
            for(i in s){
                if(i.isNotEmpty()){
                    var ia = i.toInt()
                    when(ia){
                        0 -> toR.add("New Location")
                        1 -> toR.add("Home")
                        2-> toR.add("Different State")
                        3-> toR.add("Different Country")
                        4->toR.add("Different Region")
                        5->toR.add("Entertainment Venue")
                        6-> toR.add("Famous Landmark")
                        7-> toR.add("Restaurant/Bar")
                        8-> toR.add("Tropical/Mediterranean Climate")
                        9-> toR.add("Average City")
                        10-> toR.add("Big City")
                        11-> toR.add("Park/Zoo")
                        12-> toR.add("Beach/Lake/River")
                        13->toR.add("Desert Climate")
                    }
                }

            }
            return toR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_view)
        if(le != null){
            nameBox.text = "Location Name: " + le!!.locationName
            imageView2.setImageURI(Uri.parse(le!!.pic))
            dateview.text = "Date Visited: " + le!!.date
            var s = le!!.traits.split(",")
            var s1 = convert(s)
            var traitString = "Location Traits: " + "\n"
            var st = ""
            for(i in s1){
                st += i + "\n"
            }
            traitString += st
            traitString += "\n" + "Location Score: " + le!!.calcScore().toString()
            traitView.text = traitString
        }
    }

    fun deleteEntry(view: View) {
        val ad = AlertDialog.Builder(detailedView@this).create()
        ad.setMessage("Are You Sure You Want To Delete This Entry?")
        ad.setCancelable(false)
        ad.setButton(Dialog.BUTTON_POSITIVE,"Yes", DialogInterface.OnClickListener { dialog, which ->
            if(le != null){
                myAsync().execute(le!!.id)
            }
        })
        ad.setButton(Dialog.BUTTON_NEGATIVE, "No", DialogInterface.OnClickListener { dialog, which -> return@OnClickListener })
        ad.show()
    }

    inner class myAsync: AsyncTask<Int, Unit, Unit>() {


        override fun doInBackground(vararg params: Int?) {
            val myDb= DataBaseManager(applicationContext)
            myDb.writableDatabase
            myDb.delete(params[0]!!)
            return
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            var i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    fun editEntry(view: View) {
        editLocationEntry.toEdit = le
        val i = Intent(this, editLocationEntry::class.java)
        startActivity(i)
    }
    fun showOnMap(view: View) {
        MainActivity.czLong = le?.long
        MainActivity.czLat = le?.lat
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}