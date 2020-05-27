package com.example.globe_trotte

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.example.globe_trotter.R
import com.example.globe_trotter.detailedView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

// This is the class that makes the location entry object
data class LocationEntry(var id: Int, var locationName: String, var lat: Double, var long: Double,  var date: String, var traits: String, var pic: String) {

    // convert the numeric traits to a String representation
    fun calcScore(): Int {
        var s = traits.split(",")
        var toR = 0
        for(i in s){
            if(i.isNotEmpty()){
                var ia = i.toInt()
                when(ia){
                    0 -> toR+=1
                    1 -> toR+=2
                    2-> toR+=6
                    3-> toR+=10
                    4->toR+=7
                    5->toR+=3
                    6-> toR+=8
                    7-> toR+=2
                    8-> toR+=10
                    9-> toR+=3
                    10-> toR+=6
                    11-> toR+=3
                    12-> toR+=3
                    13-> toR+=4
                }
            }

        }
        return toR
    }

    // calculate which color the marker should be
    fun getColor(): Float{
        var s = traits.split(",")
        if(calcScore() >= 25){
            return BitmapDescriptorFactory.HUE_YELLOW
        }

        if(s.contains("3")){
            return BitmapDescriptorFactory.HUE_ORANGE
        }

        if(s.contains("2")){
            return BitmapDescriptorFactory.HUE_VIOLET
        }

        if(s.contains("10")){
            return BitmapDescriptorFactory.HUE_RED
        }

        if(s.contains("4")){
            return BitmapDescriptorFactory.HUE_MAGENTA
        }

        if(s.contains("12")){
            return BitmapDescriptorFactory.HUE_CYAN
        }

        if(s.contains("1")){
            return BitmapDescriptorFactory.HUE_ROSE
        }

        if(s.contains("11")){
            return BitmapDescriptorFactory.HUE_GREEN
        }

        return BitmapDescriptorFactory.HUE_BLUE
    }
}