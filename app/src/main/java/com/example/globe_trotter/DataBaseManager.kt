package com.example.globe_trotter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.globe_trotte.LocationEntry
import kotlin.random.Random

// columns for database that are needed for a single location entry
val TABLE_ENTRIES = "Entries"
val ID = "ID"
val LONG = "Longitude"
val LAT = "Latitude"
val NAME = "Location_Name"
val DATE = "Date_Visited"
val TRAITS = "Location_Traits"
val PIC = "Picture"


class DataBaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // We want the rest of the project to see these attributes
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Entries.db"
        var listOfIds = ArrayList<Int>()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${TABLE_ENTRIES} (" +
                    "${ID} INTEGER PRIMARY KEY, " +
                    "${NAME} TEXT, ${LAT} REAL, ${LONG} REAL, ${DATE} TEXT, ${TRAITS} TEXT, ${PIC} TEXT)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists $TABLE_ENTRIES")
        onCreate(p0)
    }

    fun insert(entryToInsert: LocationEntry) {
        var id = 0
        do{
            id = Random.nextInt(99999999)  + entryToInsert.lat.toInt() + entryToInsert.long.toInt()
        } while (listOfIds.contains(id)) // id made by huge random number plus long and lat.
        listOfIds.add(id) // just to make sure it is a unique id make list of them to verify.

        val db = this.writableDatabase
        val insertString = "INSERT INTO $TABLE_ENTRIES " +
                "VALUES ('${id}', '${entryToInsert.locationName}', '${entryToInsert.lat}'," +
                " '${entryToInsert.long}', '${entryToInsert.date}', '${entryToInsert.traits}', '${entryToInsert.pic}')"
        db.execSQL(insertString)
    }

    // used for iterating through DB entries easily.
    fun selectAll(): ArrayList<LocationEntry> {
        val sqlQuery = "select * from $TABLE_ENTRIES"

        val db = this.writableDatabase
        var toReturn = ArrayList<LocationEntry>()
        var cursor = db.rawQuery(sqlQuery, null)
        while (cursor.moveToNext()) {
            toReturn.add(LocationEntry(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2),
                cursor.getDouble(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
        }
        return toReturn
    }

    fun delete(toInsert: Int) {
        val db = this.writableDatabase
        val deleteString = "DELETE FROM $TABLE_ENTRIES " +
                "WHERE ${ID} = ${toInsert}"
        db.execSQL(deleteString)
    }

    fun modify(toInsert: LocationEntry) {
        val db = this.writableDatabase
        val modifyString = "UPDATE $TABLE_ENTRIES " +
                "SET ${NAME} = '${toInsert.locationName}', " +
                "${DATE} = '${toInsert.date}', " +
                "${TRAITS} = '${toInsert.traits}', " +
                "${PIC} = '${toInsert.pic}' " +
                "WHERE ${ID} = ${toInsert.id}"
        db.execSQL(modifyString)
    }
}