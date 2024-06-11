package com.tristarvoid.kasper.location.data

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long, priority: Int): Flow<Location>

    class LocationException(message: String) : Exception(message)
}