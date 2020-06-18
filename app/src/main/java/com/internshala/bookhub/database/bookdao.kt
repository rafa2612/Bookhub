package com.internshala.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface bookdao {

    @Insert
    fun insertbook(bookentity:bookentities)

    @Delete
    fun deletebook(bookentity: bookentities)

    @Query("Select * from books")
    fun getallbooks():List<bookentities>

    @Query("Select * from books where book_id= :bookid")
    fun getbookbyid(bookid:String):bookentities

}