package com.wix.detox.inspector.service

import android.service.quicksettings.TileService
import com.wix.detox.inspector.features.inspect.InspectActivity
import timber.log.Timber

class InspectorTileService : TileService() {

    override fun onClick() {
        super.onClick()
        Timber.i("onClick")
        InspectActivity.startActivity(this)
    }

    override fun onStartListening() {
        super.onStartListening()
        Timber.i("onStartListening")
    }

    override fun onStopListening() {
        super.onStopListening()
        Timber.i("onStopListening")
    }
}