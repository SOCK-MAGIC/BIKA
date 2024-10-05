package com.shizq.bika.core.datastore

import androidx.datastore.core.Serializer
import com.shizq.bika.core.datastore.model.UserCredential
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
class UserCredentialSerializer @Inject constructor(private val json: Json) :
    Serializer<UserCredential> {
    override val defaultValue: UserCredential = UserCredential()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserCredential =
        json.decodeFromStream(input)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserCredential, output: OutputStream) {
        json.encodeToStream(t, output)
    }
}
