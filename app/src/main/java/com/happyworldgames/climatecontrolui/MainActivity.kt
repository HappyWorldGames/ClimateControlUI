package com.happyworldgames.climatecontrolui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.happyworldgames.climatecontrolui.databinding.ActivityMainBinding
import com.happyworldgames.climatecontrolui.launcher.LauncherActivity

class MainActivity : AppCompatActivity() {

    private val activityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu == null) return false
        menu.add(0, 1, 0, "Apps")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            1 -> startActivity(Intent(this, LauncherActivity::class.java))
        }
        return true
    }
}