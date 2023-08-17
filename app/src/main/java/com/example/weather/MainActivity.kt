package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.weather.presentation.ListForecastDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme( R.style.Theme_Weather )

        setContentView(R.layout.activity_main)

        if ( savedInstanceState == null ) {
            supportFragmentManager.commit {
                replace<ListForecastDayFragment>( R.id.fragment_container )
            }
        }
    }
}