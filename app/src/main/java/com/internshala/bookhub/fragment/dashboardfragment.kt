package com.internshala.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.dashboardrecycler
import com.internshala.bookhub.models.Book
import com.internshala.bookhub.util.connectionmanager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class dashboardfragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    lateinit var recyclerdashboard:RecyclerView
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var logout1:Button
    lateinit var bookInfoList:ArrayList<Book>
    lateinit var progresslayout:RelativeLayout
    lateinit var progressbar:ProgressBar


    var ratingcomparator= Comparator<Book>{book1, book2 ->
        if (book1.bookrating.compareTo(book2.bookrating,true) == 0){
            book1.bookname.compareTo(book2.bookname,true)
        }
        else{
            book1.bookrating.compareTo(book2.bookrating,true)
        }
    }





    lateinit var recycleradapter:dashboardrecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


       val view=inflater.inflate(R.layout.fragment_dashboardfragment,container, false)

        recyclerdashboard=view.findViewById(R.id.recyclerdashboard)

        layoutmanager=LinearLayoutManager(activity)


        auth=FirebaseAuth.getInstance()

        progresslayout=view.findViewById(R.id.relativeprogressbar)

        progressbar=view.findViewById(R.id.progressbar1111)

        progresslayout.visibility=View.VISIBLE








        setHasOptionsMenu(true)

        val queue=Volley.newRequestQueue(activity as Context)

        val url="http://13.235.250.119/v1/book/fetch_books/"



            if (connectionmanager().checkconnectivity(activity as Context)) {



                val jsonObjectRequest= object : JsonObjectRequest(Method.GET,url,null,
                    Response.Listener {

                        try {
                            progresslayout.visibility=View.GONE
                            val success=it.getBoolean("success")

                            if (success){
                                bookInfoList=ArrayList()
                                val data=it.getJSONArray("data")
                                for (i in 0 until data.length())
                                {
                                    val bookjsonobject=data.getJSONObject(i)
                                    val bookobject=Book(
                                        bookjsonobject.getString("book_id"),
                                        bookjsonobject.getString("name"),
                                        bookjsonobject.getString("author"),
                                        bookjsonobject.getString("rating"),
                                        bookjsonobject.getString("price"),
                                        bookjsonobject.getString("image")
                                    )
                                    bookInfoList.add(bookobject)
                                    recycleradapter= dashboardrecycler(activity as Context,bookInfoList)

                                    recyclerdashboard.adapter=recycleradapter
                                    recyclerdashboard.layoutManager=layoutmanager

                                    recyclerdashboard.addItemDecoration(
                                        DividerItemDecoration(
                                            recyclerdashboard.context,(layoutmanager as LinearLayoutManager).orientation
                                        )
                                    )
                                }
                            }

                            else{Toast.makeText(activity as Context,"error",Toast.LENGTH_SHORT).show()}
                        }

                        catch (e: JSONException){
                            Toast.makeText(activity as Context,"Some unexpected error occurd",Toast.LENGTH_SHORT).show()
                        }

                    },


                    Response.ErrorListener {
                        Toast.makeText(activity as Context,"Some unexpected error occured volley",Toast.LENGTH_SHORT).show()
                    }
                )

                {
                    override fun getHeaders(): MutableMap<String, String> {

                        val headers=HashMap<String, String>()
                        headers["Content-type"]="application/json"
                        headers["Token"]="e07c22a352bb4d"
                        return headers
                    }

                }
                queue.add(jsonObjectRequest)
            }
            else{
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection not found")
                dialog.setPositiveButton("settings"){text,listener->
                    val settingsintent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsintent)
                    activity?.finish()
                }
                dialog.setNegativeButton("cancel"){text, listener->

                    ActivityCompat.finishAffinity(activity as Activity)
                }
                dialog.create()
                dialog.show()
            }








        return view


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort) {
        Collections.sort(bookInfoList,ratingcomparator)
            bookInfoList.reverse()
        }

        recycleradapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)

    }



    }
