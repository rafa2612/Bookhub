package com.internshala.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.favouriterecycleradapter
import com.internshala.bookhub.database.bookdatabase
import com.internshala.bookhub.database.bookentities
import com.internshala.bookhub.models.Book

/**
 * A simple [Fragment] subclass.
 */
class favourite : Fragment() {

    lateinit var favouriterecycler: RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var recycleradapter:favouriterecycleradapter
    var dbbooklist= listOf<bookentities>()

    lateinit var progresslayout: RelativeLayout
    lateinit var progressbar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite,container, false)

        favouriterecycler=view.findViewById(R.id.recyclerfavourite)

        layoutmanager= GridLayoutManager(activity as Context,2)

        progresslayout=view.findViewById(R.id.favouriteprogressbarlayout)

        progressbar=view.findViewById(R.id.favouriteprogressbar)



        dbbooklist=retrievefavourites(activity as Context).execute().get()

        if (activity!=null){
            progresslayout.visibility=View.GONE

            recycleradapter= favouriterecycleradapter(activity as Context,dbbooklist)
            favouriterecycler.adapter=recycleradapter
            favouriterecycler.layoutManager=layoutmanager
        }

        return view
    }

    class retrievefavourites(val context: Context): AsyncTask<Void, Void, List<bookentities>>() {

        override fun doInBackground(vararg params: Void?): List<bookentities> {
            val db= Room.databaseBuilder(context,bookdatabase::class.java,"books-db").build()

            return db.bookdao().getallbooks()
        }
    }

}
