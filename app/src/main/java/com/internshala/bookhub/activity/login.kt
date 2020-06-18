package com.internshala.bookhub.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.internshala.bookhub.R
import kotlinx.android.synthetic.main.activity_login.*



class login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var passward: EditText
    lateinit var login: Button
    lateinit var forgot: TextView
    lateinit var register: TextView
    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="log in"

        auth = FirebaseAuth.getInstance()






        email =findViewById(R.id.loginmobileno)
        passward = findViewById(R.id.loginpassward)
        login = findViewById(R.id.loginbutton)
        forgot = findViewById(R.id.loginforgotpass)
        register = findViewById(R.id.loginregister)

        login.setOnClickListener{

            login()
        }

        register.setOnClickListener{
            startActivity(Intent(this,signup::class.java))


        }

        forgot.setOnClickListener{
            val dialog= AlertDialog.Builder(this@login)
            dialog.setTitle("Reset Password")
            val view=layoutInflater.inflate(R.layout.reset_pass,null)
            dialog.setView(view)
            val email:EditText=view.findViewById(R.id.resetpass)
            dialog.setPositiveButton("reset"){text,listener->
                resetpassword(email)
            }
            dialog.setNegativeButton("cancel"){text, listener->

                dialog.show()
            }
            dialog.create()
            dialog.show()
        }



    }




    fun login(){
        if(loginmobileno.text.toString().isEmpty()){
            loginmobileno.error="Enter email"
            loginmobileno.requestFocus()
            return
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(loginmobileno.text.toString()).matches()){
            loginmobileno.error="Enter valid email"
            loginmobileno.requestFocus()
            return
        }




        if(loginpassward.text.toString().isEmpty()){
            loginpassward.error="Enter password"
            loginpassward.requestFocus()
            return
        }



        auth.signInWithEmailAndPassword(loginmobileno.text.toString(), loginpassward.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user= auth.currentUser

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.


                    updateUI(null)
                    // ...
                }

                // ...
            }
    }




    private fun updateUI( currentUser: FirebaseUser?){

        if (currentUser!=null){

            startActivity(Intent(this,MainActivity::class.java))


            finish()
        }
        else{
            Toast.makeText(this@login, "Email or Password Incorrect",
                Toast.LENGTH_SHORT).show()
        }

    }

    fun resetpassword(email:EditText){
        if(email.text.toString().isEmpty()){

            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }



        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this@login, "Email sent",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@login, "Try again",
                        Toast.LENGTH_SHORT).show()
                }
            }


    }










}
