package com.internshala.bookhub.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.internshala.bookhub.R
import com.internshala.bookhub.database.user
import kotlinx.android.synthetic.main.activity_signup.*


class signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var signupbutton:Button
    lateinit var emailid:EditText
    lateinit var passward:EditText
    lateinit var phoneno:EditText
    lateinit var fullname:EditText
    lateinit var gender:String
    lateinit var male:RadioButton
    lateinit var female:RadioButton
    lateinit var group1:RadioGroup
    lateinit var choose:Button
    lateinit var imageview: ImageView
    lateinit var imguri:Uri
    lateinit var imageid:String
    lateinit var mstorageref:StorageReference
    lateinit var url:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupbutton=findViewById(R.id.signupbutton)
        auth = FirebaseAuth.getInstance()
        emailid=findViewById(R.id.signupmobileno)
        passward=findViewById(R.id.signuppassward)
        phoneno=findViewById(R.id.signupphoneno)
        fullname=findViewById(R.id.signupusername)
        group1=findViewById(R.id.radiogroupgender)
        male=findViewById(R.id.signupmale)
        female=findViewById(R.id.signupfemale)
        choose=findViewById(R.id.chooseimage)
        imageview=findViewById(R.id.viewimage)
        mstorageref=FirebaseStorage.getInstance().getReference("images")

        group1.setOnCheckedChangeListener { _, checkedId ->
            //if catButton was selected add 1 to variable cat
            if(checkedId == R.id.signupmale) {
                gender=male.text.toString()
                Toast.makeText(this@signup,"$gender",Toast.LENGTH_SHORT).show()
            }
            //if dogButton was selected add 1 to variable dog
            if(checkedId == R.id.signupfemale) {

                gender=female.text.toString()
                Toast.makeText(this@signup,"$gender",Toast.LENGTH_SHORT).show()

            }
        }

        choose.setOnClickListener {

            filechoose()

        }


        signupbutton.setOnClickListener{


            fileuploader()


        }
    }



    fun signup(x:String,img:String){

        val username=fullname.text.toString()
        val email=emailid.text.toString()
        val phone=phoneno.text.toString()



        if(signupmobileno.text.toString().isEmpty()){
            signupmobileno.error="Enter email"
            signupmobileno.requestFocus()
            return
        }

        if(signupphoneno.text.toString().isEmpty()){
            signupphoneno.error="Enter phone no"
            signupphoneno.requestFocus()
            return
        }

        if(signupmobileno.text.toString().isEmpty()){
            signupusername.error="Enter your name"
            signupusername.requestFocus()
            return
        }

        if(signupmobileno.text.toString().isEmpty()){
            signupmobileno.error="Enter email"
            signupmobileno.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signupmobileno.text.toString()).matches()){
            signupmobileno.error="Enter valid email"
            signupmobileno.requestFocus()
            return
        }


        if(signuppassward.text.toString().isEmpty()){
            signuppassward.error="Enter password"
            signuppassward.requestFocus()
            return
        }


        val ref=FirebaseDatabase.getInstance().getReference("users")

        val userid=ref.push().key

        val userr=user(userid!!,username,email,phone,x,img)

        ref.child(userid).setValue(userr).addOnCompleteListener {
            Toast.makeText(baseContext,"done",Toast.LENGTH_SHORT).show()
        }

        auth.createUserWithEmailAndPassword(signupmobileno.text.toString(),signuppassward.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,login::class.java))
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext,"Signup failed",Toast.LENGTH_SHORT).show()
                }

                // ...
            }





    }


    private fun filechoose(){
        val intent:Intent= Intent()
        intent.setType("image/")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode== Activity.RESULT_OK && data!=null && data.data!=null){
            imguri= data.data!!
            imageview.setImageURI(imguri)

        }
    }


    private fun getextension(uri:Uri): String? {
        var cr:ContentResolver=contentResolver
        var mimetypemap:MimeTypeMap= MimeTypeMap.getSingleton()
        return mimetypemap.getExtensionFromMimeType(cr.getType(uri))
    }

    private fun fileuploader(){


        imageid="${System.currentTimeMillis()}.${getextension(imguri)}"
        val ref:StorageReference=mstorageref.child(imageid)

        ref.putFile(imguri)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content

                ref.getDownloadUrl()
                    .addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        url=uri.toString()
                        signup(gender,url)
                    })





            })
            .addOnFailureListener(OnFailureListener {
                // Handle unsuccessful uploads
                // ...
            })



    }

}




