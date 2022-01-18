package com.watermelon.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.watermelon.criminalintent.Crimen


@Database(entities = [Crimen::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase:RoomDatabase() {

    abstract fun crimeDao():CrimeDao


}