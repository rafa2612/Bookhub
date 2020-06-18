package com.internshala.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhub.R
import com.internshala.bookhub.database.bookentities
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerfavourite_single_row.view.*

class favouriterecycleradapter(val context: Context,val booklist:List<bookentities>):
    RecyclerView.Adapter<favouriterecycleradapter.favouriteviewholder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favouriteviewholder {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.recyclerfavourite_single_row,parent,false)
        return favouriteviewholder(view)

    }

    override fun getItemCount(): Int {

        return booklist.size

    }

    override fun onBindViewHolder(holder: favouriteviewholder, position: Int) {

        val book=booklist[position]
        holder.txtbookname.text=book.bookname
        holder.txtbookauthor.text=book.bookauthor
        holder.txtbookprice.text=book.bookprice
        holder.txtbookrating.text=book.bookrating
        Picasso.get().load(book.bookimage).error(R.drawable.book).into(holder.txtbookimage);

    }

    class favouriteviewholder(view: View):RecyclerView.ViewHolder(view){

        val txtbookname:TextView=view.findViewById(R.id.recclerfavbookname)
        val txtbookauthor:TextView=view.findViewById(R.id.recyclerfavbookauthor)
        val txtbookprice:TextView=view.findViewById(R.id.recyclerfavbookprice)
        val txtbookrating:TextView=view.findViewById(R.id.recyclerfavbookrating)
        val txtbookimage:ImageView=view.findViewById(R.id.recyclerfavimage)





    }
}