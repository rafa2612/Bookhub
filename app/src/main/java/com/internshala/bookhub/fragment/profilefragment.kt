package com.internshala.bookhub.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.MainActivity
import com.internshala.bookhub.database.user
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 */
class profilefragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var database:FirebaseDatabase
    lateinit var fullname:TextView
    lateinit var emailid:TextView
    lateinit var mobileno:TextView
    lateinit var gender:TextView
    lateinit var progressbarlayout:RelativeLayout
    lateinit var progressbarprofile:ProgressBar
    lateinit var imgid:ImageView
    lateinit var mstorageref: StorageReference
    lateinit var url:String





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profilefragment,container, false)

        auth= FirebaseAuth.getInstance()
        fullname=view.findViewById(R.id.usernameprofile)
        emailid=view.findViewById(R.id.emailidprofile)
        mobileno=view.findViewById(R.id.mobileprofile)
        gender=view.findViewById(R.id.genderprofile)
        progressbarlayout=view.findViewById(R.id.profileprogresslayout)
        progressbarprofile=view.findViewById(R.id.progressbarprofile)
        imgid=view.findViewById(R.id.profileimage)
        mstorageref= FirebaseStorage.getInstance().getReference("images")
        progressbarlayout.visibility=View.VISIBLE
        progressbarprofile.visibility=View.VISIBLE




        val ref= FirebaseDatabase.getInstance().getReference()





        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {


                   for (x in p0.children)
                   {
                       for(y in x.children)
                       {
                           if (y.child("email").getValue().toString()==auth.currentUser?.email.toString())
                           {fullname.text=y.child("fullname").getValue().toString()
                               emailid.text=y.child("email").getValue().toString()
                               mobileno.text=y.child("mobileno").getValue().toString()
                               gender.text=y.child("gender").getValue().toString()
                               url=y.child("image").getValue().toString()
                               Picasso.get().load(url).error(R.drawable.book).into(imgid)
                               progressbarprofile.visibility=View.GONE
                               progressbarlayout.visibility=View.GONE


                           }
                       }
                   }



            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


        return view


    }



}




