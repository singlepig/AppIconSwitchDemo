package me.singlepig.demo.appiconswitchdemo

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author singlepig
 * @time 2020/11/19 17:07
 * @desc description
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LauncherIconManager.instance.register(this)
    }

}
