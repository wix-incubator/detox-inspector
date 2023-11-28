package com.wix.detox.inspector.features.inspect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wix.detox.inspector.features.inspect.ui.InspectScreen
import com.wix.detox.inspector.features.inspect.viewmodel.InspectViewModel
import com.wix.detox.inspector.ui.theme.DetoxInspectorTheme
import timber.log.Timber

class InspectActivity : ComponentActivity() {

    private val viewModel: InspectViewModel by viewModels()

    companion object {

        fun startActivity(context: Context) {
            Timber.i("About to start InspectorActivity")
            val intent = Intent(context, InspectActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetoxInspectorTheme {
                InspectScreen(viewModel = viewModel)
            }
        }
    }
}

