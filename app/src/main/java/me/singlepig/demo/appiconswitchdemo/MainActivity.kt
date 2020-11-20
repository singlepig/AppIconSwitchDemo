package me.singlepig.demo.appiconswitchdemo

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 添加切换图标的任务
        LauncherIconManager.instance.addTask(
            SwitchIconTask(
                MainActivity::class.java.name,
                "$packageName.vip618",
                formatter.parse("2020-06-17")!!.time,
                formatter.parse("2020-06-19")!!.time
            ),
            SwitchIconTask(
                MainActivity::class.java.name,
                "$packageName.vip11",
                formatter.parse("2020-11-10")!!.time,
                formatter.parse("2020-11-12")!!.time
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
}
