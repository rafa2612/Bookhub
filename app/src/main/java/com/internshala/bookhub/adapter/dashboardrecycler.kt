package com.internshala.bookhub.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.descriptionbook
import com.internshala.bookhub.models.Book
import com.squareup.picasso.Picasso

class dashboardrecycler(
    val context: Context,
    val itemlist: ArrayList<Book>

    ):
    RecyclerView.Adapter<dashboardrecycler.DashboardViewholder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewholder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_singlerow,parent,false)
        return DashboardViewholder(view)
    }

    override fun getItemCount(): Int {
        return itemlist.size


    }

    override fun onBindViewHolder(holder: DashboardViewholder, position: Int) {
        val book=itemlist[position]
        holder.txtbookname.text=book.bookname
        holder.txtbookauthor.text=book.bookauthor
        holder.txtbookprice.text=book.bookprice
        holder.txtbookrating.text=book.bookrating
        Picasso.get().load(book.bookimage).error(R.drawable.book).into(holder.imgbookimg);

        holder.clicklistener.setOnClickListener{
            val intent=Intent(context,descriptionbook::class.java)
            intent.putExtra("book_id",book.bookid)
            context.startActivity(intent)
        }






    }

    class DashboardViewholder(view:View):RecyclerView.ViewHolder(view){

        val txtbookname:TextView=view.findViewById(R.id.recyclerlistview)
        val txtbookauthor:TextView=view.findViewById(R.id.authornamerecycler)
        val txtbookprice:TextView=view.findViewById(R.id.pricerecycler)
        val txtbookrating:TextView=view.findViewById(R.id.ratingrecycler)
        val imgbookimg:ImageView=view.findViewById(R.id.recyclerimg)
        val clicklistener:RelativeLayout=view.findViewById(R.id.relativelayoutrecycler)
    }
}