package com.example.translation.presentation.view.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.translation.R
import com.example.translation.di.ComponentHolder
import com.example.translation.presentation.Screens
import com.example.translation.presentation.view.common.BackButtonListener
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val navigator: AppNavigator = AppNavigator(this, R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentHolder.INSTANCE.getMainScreenComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigator.applyCommands(
                arrayOf(Replace(Screens.translation()))
            )
        }
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            if (navigationView.selectedItemId != it.itemId) {
                when (it.itemId) {
                    R.id.action_translation -> router.newRootScreen(Screens.translation())
                    R.id.action_favorites -> router.newRootScreen(Screens.favorites())
                }
                true
            } else {
                false
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is BackButtonListener && fragment.onBackPressed()) {
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentHolder.INSTANCE.clearMainScreenComponent()
    }
}