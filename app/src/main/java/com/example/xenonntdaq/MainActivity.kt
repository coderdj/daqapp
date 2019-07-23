package com.example.xenonntdaq

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mStatusFragment: StatusFragment = StatusFragment()
    private var mRunsFragment: RunsFragment = RunsFragment()
    private var mControlFragment: ControlFragment = ControlFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_status -> {

                // funny trick, copy private to local
                //var localStatusFragment: StatusFragment? = this.mStatusFragment
                //if (localStatusFragment == null){
                //    this.mStatusFragment = StatusFragment() //StatusFragment.newInstance()
                //    localStatusFragment = this.mStatusFragment
                // }
                var ft : FragmentTransaction = getSupportFragmentManager().beginTransaction()
                ft.replace(R.id.content_frame, this.mStatusFragment)
                ft.commit()
                //return@OnNavigationItemSelectedListener true
            }
            R.id.nav_runs -> {
                var ft : FragmentTransaction = getSupportFragmentManager().beginTransaction()
                ft.replace(R.id.content_frame, this.mRunsFragment)
                ft.commit()
                //return@OnNavigationItemSelectedListener true
            }
            R.id.nav_command -> {
                var ft : FragmentTransaction = getSupportFragmentManager().beginTransaction()
                ft.replace(R.id.content_frame, this.mControlFragment)
                ft.commit()
                //return@OnNavigationItemSelectedListener true
            }
            R.id.nav_tools -> {
                //return@OnNavigationItemSelectedListener true
            }
            R.id.nav_contact -> {
                //return@OnNavigationItemSelectedListener true
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
