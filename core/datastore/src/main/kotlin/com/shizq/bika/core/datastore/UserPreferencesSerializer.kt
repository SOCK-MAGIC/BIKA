package com.shizq.bika.core.datastore

import androidx.datastore.core.Serializer
import com.shizq.bika.core.datastore.di.DatastoreJson
import com.shizq.bika.core.datastore.model.UserPreferences
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserPreferences =
        DatastoreJson.decodeFromStream(input)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        DatastoreJson.encodeToStream(t, output)
    }
}
