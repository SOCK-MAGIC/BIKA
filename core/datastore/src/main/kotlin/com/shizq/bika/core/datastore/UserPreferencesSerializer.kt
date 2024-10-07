package com.shizq.bika.core.datastore

import androidx.datastore.core.Serializer
import com.shizq.bika.core.datastore.model.UserPreferences
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * An [androidx.datastore.core.Serializer] for the [UserPreferences] proto.
 */
class UserPreferencesSerializer @Inject constructor(private val json: Json) : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserPreferences =
        json.decodeFromStream(input)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        json.encodeToStream(t, output)
    }
}
