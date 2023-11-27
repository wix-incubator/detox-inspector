package com.wix.detox.layoutinspectorapp.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import com.wix.detox.inspector.Inspector
import com.wix.detox.layoutinspectorapp.R
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.i("onKeyDown: $keyCode")

        if (keyCode == KeyEvent.KEYCODE_A) {
            Inspector.inspect(this)
        }

        return super.onKeyDown(keyCode, event)
    }
}