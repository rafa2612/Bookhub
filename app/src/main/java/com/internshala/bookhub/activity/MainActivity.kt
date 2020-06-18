package com.internshala.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.internshala.bookhub.R
import com.internshala.bookhub.fragment.aboutfragment
import com.internshala.bookhub.fragment.dashboardfragment
import com.internshala.bookhub.fragment.favourite
import com.internshala.bookhub.fragment.profilefragment

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var drawerlayout: DrawerLayout
    lateinit var coordinatorlayout: CoordinatorLayout
    lateinit var toolbar:Toolbar
    lateinit var framelayout:FrameLayout
    lateinit var navigation: NavigationView
    var previousmenuitem:MenuItem?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerlayout=findViewById(R.id.drawerlayout)
        coordinatorlayout=findViewById(R.id.coordinatorlayout)
        toolbar=findViewById(R.id.toolbar)
        framelayout=findViewById(R.id.framelayout)
        navigation=findViewById(R.id.navigationbar)
        auth=FirebaseAuth.getInstance()

        setUpToolbar()
        opendash()

        var actionbartoggle=ActionBarDrawerToggle(
            this@MainActivity,
            drawerlayout,
            R.string.opendrawer,
            R.string.closedrawer
        )

            drawerlayout.addDrawerListener(actionbartoggle)
        actionbartoggle.syncState()






        navigation.setNavigationItemSelectedListener {
            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it

            when(it.itemId){
                R.id.dashboard ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            dashboardfragment()
                        )
                        .addToBackStack("dashboardfragment")

                        .commit()
                    supportActionBar?.title="Dashboard"
                    drawerlayout.closeDrawers()
                }
                R.id.favourites ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            favourite()
                        )
                        .addToBackStack("favourite")
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerlayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            profilefragment()
                        )
                        .addToBackStack("profilefragment")
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerlayout.closeDrawers()
                }
                R.id.about ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            aboutfragment()
                        )
                        .addToBackStack("aboutfragment")
                        .commit()
                    supportActionBar?.title="About App"
                    drawerlayout.closeDrawers()
                }

                R.id.logout ->{
                    auth.signOut()
                    finish()
                   startActivity(Intent(this,login::class.java))


                }


            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Tool Bar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var id=item.itemId
        if(id==android.R.id.home){
            drawerlayout.openDrawer(GravityCompat.START)
        }


        return super.onOptionsItemSelected(item)
    }

    fun opendash(){
        var fragment= dashboardfragment()
        var transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout,fragment)
        transaction.commit()
        supportActionBar?.title="FRIENDS"
        navigation.setCheckedItem(R.id.dashboard)
        drawerlayout.closeDrawers()

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
           return



        }else{
            startActivity(Intent(this,login::class.java))
            finish()
        }
    }


    }





