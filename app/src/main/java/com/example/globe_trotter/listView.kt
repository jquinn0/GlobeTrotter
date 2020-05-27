package com.example.globe_trotter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.list_view.*


class listView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_view)
        val names = ArrayList<String>()
        val dates = ArrayList<String>()
        val pics = ArrayList<Uri>()

        var io = 0
        for(i in MainActivity.entries){
            names.add(io, "Name: " + i.locationName)
            dates.add(io, "Date Visited: " + i.date + "\nLocation Score: " + i.calcScore())
            pics.add(io, Uri.parse(i.pic))
            io++
        }

        val myListAdapter = MyListAdapter(this,names,dates,pics)
        list_View.adapter = myListAdapter

        list_View.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            for(i in MainActivity.entries){
                if("Name: "+ i.locationName == itemAtPos){
                    detailedView.le = i
                    var i = Intent(applicationContext, detailedView::class.java)
                    startActivity(i)
                }
            }
        }
    }
}