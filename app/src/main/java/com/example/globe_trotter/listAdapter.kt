package com.example.globe_trotter
import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyListAdapter(val context: Activity, val title: ArrayList<String>, val description: ArrayList<String>, val imgid: ArrayList<Uri>):
    ArrayAdapter<String>(context, R.layout.single_list_item, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.single_list_item, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = title[position]
        imageView.setImageURI(imgid[position])
        subtitleText.text = description[position]

        return rowView
    }
}