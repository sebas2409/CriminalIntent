package com.watermelon.criminalintent


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Crimen(
    @PrimaryKey val id:UUID= UUID.randomUUID(), // UUID proporciona una manera facil de generar una ID única aleatoria
    var title: String="",
    var date:Date=Date(),
    var isSolved:Boolean = false
)
