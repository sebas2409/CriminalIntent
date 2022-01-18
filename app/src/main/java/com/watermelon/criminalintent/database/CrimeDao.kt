package com.watermelon.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.watermelon.criminalintent.Crimen
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM Crimen")
    fun getCrimes(): LiveData<List<Crimen>>
    @Query("SELECT * FROM Crimen WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crimen?>
    @Update
    fun updateCrime(crime: Crimen)
    @Insert
    fun addCrime(crime: Crimen)


}