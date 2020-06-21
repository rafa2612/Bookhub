package com.internshala.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.internshala.bookhub.R

class splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val background=object :Thread(){
            override fun run() {
                Thread.sleep(5000)
                val intent:Intent
                intent= Intent(baseContext,MainActivity::class.java)
                startActivity(intent)
            }
        }

        background.start()
    }

    override fun onPause() {
        finish()
    }
}
