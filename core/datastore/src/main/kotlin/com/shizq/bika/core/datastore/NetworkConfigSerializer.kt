package com.shizq.bika.core.datastore

import androidx.datastore.core.Serializer
import com.shizq.bika.core.datastore.di.DatastoreJson
import com.shizq.bika.core.datastore.model.NetworkConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class NetworkConfigSerializer @Inject constructor() : Serializer<NetworkConfig> {
    override val defaultValue: NetworkConfig = NetworkConfig()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): NetworkConfig =
        DatastoreJson.decodeFromStream(input)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: NetworkConfig, output: OutputStream) {
        DatastoreJson.encodeToStream(t, output)
    }
}
