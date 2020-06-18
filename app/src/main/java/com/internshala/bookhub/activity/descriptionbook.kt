
package com.internshala.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Response
import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookhub.R
import com.internshala.bookhub.database.bookdao
import com.internshala.bookhub.database.bookdatabase
import com.internshala.bookhub.database.bookentities
import com.internshala.bookhub.models.Book
import com.internshala.bookhub.util.connectionmanager
import com.squareup.picasso.Picasso

import org.json.JSONObject

import kotlin.Exception

class descriptionbook : AppCompatActivity() {

    lateinit var txtbookname: TextView
    lateinit var txtbookauthor: TextView
    lateinit var txtbookprice: TextView
    lateinit var txtbookrating: TextView
    lateinit var txtbookimage: ImageView
    lateinit var txtbookdescription:TextView
    lateinit var addtofav:Button
    lateinit var progressbar:ProgressBar
    lateinit var progressbarlayout:RelativeLayout
    lateinit var toolbar:Toolbar
    var bookid:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descriptionbook)

        txtbookname=findViewById(R.id.booknamedescription)
        txtbookauthor=findViewById(R.id.bookauthordescription)
        txtbookprice=findViewById(R.id.bookpricedescription)
        txtbookrating=findViewById(R.id.bookratingdescription)
        txtbookimage=findViewById(R.id.imagedescription)
        txtbookdescription=findViewById(R.id.descriptionofbook)
        addtofav=findViewById(R.id.buttondescription)
        progressbarlayout=findViewById(R.id.progresbardescriptionlayout)
        progressbar=findViewById(R.id.progressbardescription)

        toolbar=findViewById(R.id.toolbardescription)

        progressbar.visibility=View.VISIBLE
        progressbarlayout.visibility=View.VISIBLE

        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if (connectionmanager().checkconnectivity(this@descriptionbook))
        {
            progressbarlayout.visibility=View.GONE
            if (intent!=null){
                bookid=intent.getStringExtra("book_id")
            }
            else{
                finish()
                Toast.makeText(this@descriptionbook,"error",Toast.LENGTH_SHORT).show()
            }
            if (bookid=="100")
            {
                Toast.makeText(this@descriptionbook,"error",Toast.LENGTH_SHORT).show()
            }

            val queue=Volley.newRequestQueue(this@descriptionbook)
            val url="http://13.235.250.119/v1/book/get_book/"

            val jsonParams=JSONObject()
            jsonParams.put("book_id",bookid)

            val jsonRequest = object :JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                try {

                    val success=it.getBoolean("success")

                    if (success){
                        val bookjsonobject= it.getJSONObject("book_data")


                        val bookimageurl=bookjsonobject.getString("image")
                        txtbookname.text=bookjsonobject.getString("name")
                        txtbookauthor.text=bookjsonobject.getString("author")
                        txtbookprice.text=bookjsonobject.getString("price")
                        txtbookrating.text=bookjsonobject.getString("rating")
                        txtbookdescription.text=bookjsonobject.getString("description")
                        Picasso.get().load(bookjsonobject.getString("image")).error(R.drawable.book).into(txtbookimage)

                        val bookentity=bookentities(
                            bookid?.toInt() as Int,
                            txtbookname.text.toString(),
                            txtbookauthor.text.toString(),
                            txtbookprice.text.toString(),
                            txtbookrating.text.toString(),
                            txtbookdescription.text.toString(),
                            bookimageurl


                        )

                        val isfav=(DBAsynchtask(applicationContext,bookentity,mode = 1).execute()).get()
                        if (isfav) {
                            addtofav.text = "Remove from Favourites"
                            val favColor = ContextCompat.getColor(
                                applicationContext,
                                R.color.colorfavourite
                            )
                            addtofav.setBackgroundColor(favColor)
                        } else {
                            addtofav.text = "Add to Favourites"
                            val noFavColor =
                                ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                            addtofav.setBackgroundColor(noFavColor)
                        }

                        addtofav.setOnClickListener {

                            if (!DBAsynchtask(
                                    applicationContext,
                                    bookentity,
                                    1
                                ).execute().get()
                            ) {

                                val async =
                                    DBAsynchtask(applicationContext, bookentity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(
                                        this@descriptionbook,
                                        "Book added to favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    addtofav.text = "Remove from favourites"
                                    val favColor = ContextCompat.getColor(applicationContext, R.color.colorfavourite)
                                    addtofav.setBackgroundColor(favColor)
                                } else {
                                    Toast.makeText(
                                        this@descriptionbook,
                                        "Some error occurred!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {

                                val async = DBAsynchtask(applicationContext, bookentity, 3).execute()
                                val result = async.get()

                                if (result){
                                    Toast.makeText(
                                        this@descriptionbook,
                                        "Book removed from favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    addtofav.text = "Add to favourites"
                                    val noFavColor =
                                        ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                    addtofav.setBackgroundColor(noFavColor)
                                } else {
                                    Toast.makeText(
                                        this@descriptionbook,
                                        "Some error occurred!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }


                    }





                    else{
                        Toast.makeText(this@descriptionbook,"error",Toast.LENGTH_SHORT).show()
                    }

                }

                catch (e: Exception){
                    Toast.makeText(this@descriptionbook,"error",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener{



            }

            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String, String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="e07c22a352bb4d"
                    return headers

                }
            }
            queue.add(jsonRequest)

        }

        else{

            progressbarlayout.visibility=View.VISIBLE


            val dialog= AlertDialog.Builder(this@descriptionbook)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("settings"){text,listener->
                val settingsintent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                this@descriptionbook?.finish()
            }
            dialog.setNegativeButton("cancel"){text, listener->

                ActivityCompat.finishAffinity(this@descriptionbook)
            }
            dialog.create()
            dialog.show()
        }





    }

    class DBAsynchtask(val context: Context,val bookentity: bookentities,val mode:Int):AsyncTask<Void, Void , Boolean>(){

        val db=Room.databaseBuilder(context,bookdatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1->{
                    val book:bookentities?=db.bookdao().getbookbyid(bookentity.book_id.toString())
                    db.close()
                    return book!=null

                }

                2->{
                    db.bookdao().insertbook(bookentity)
                    db.close()
                    return true
                }

                3->{
                    db.bookdao().deletebook(bookentity)
                    db.close()
                    return true

                }
            }

            return false
        }
    }
}
