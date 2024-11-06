package com.shizq.bika.core.datastore

import androidx.datastore.core.Serializer
import com.shizq.bika.core.datastore.di.DatastoreJson
import com.shizq.bika.core.datastore.model.UserInterests
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
@Deprecated("")
class UserInterestsSerializer @Inject constructor() : Serializer<UserInterests> {
    override val defaultValue: UserInterests = UserInterests()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserInterests =
        DatastoreJson.decodeFromStream(input)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserInterests, output: OutputStream) {
        DatastoreJson.encodeToStream(t, output)
    }
}
