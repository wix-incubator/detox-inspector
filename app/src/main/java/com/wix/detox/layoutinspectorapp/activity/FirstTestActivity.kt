package com.wix.detox.layoutinspectorapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.wix.detox.layoutinspectorapp.R
import timber.log.Timber

class FirstTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_test)
    }
}