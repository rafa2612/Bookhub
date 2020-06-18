package com.internshala.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [bookentities::class],version = 1)
abstract class bookdatabase:RoomDatabase() {

    abstract fun bookdao():bookdao
}