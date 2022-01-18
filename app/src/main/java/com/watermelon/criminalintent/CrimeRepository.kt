package com.watermelon.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.watermelon.criminalintent.database.CrimeDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime=database"

class CrimeRepository private constructor(context: Context) {
    private val executor = Executors.newSingleThreadExecutor()

    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val crimeDao = database.crimeDao()

    fun getCrimes(): LiveData<List<Crimen>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crimen?> = crimeDao.getCrime(id)
    fun updateCrime(crime: Crimen) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }
    fun addCrime(crime: Crimen) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }






    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }
        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
