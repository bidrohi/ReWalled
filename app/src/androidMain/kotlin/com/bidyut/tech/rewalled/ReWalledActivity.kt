package com.bidyut.tech.rewalled

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bidyut.tech.rewalled.ui.AppActivity

class ReWalledActivity : AppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }
}
