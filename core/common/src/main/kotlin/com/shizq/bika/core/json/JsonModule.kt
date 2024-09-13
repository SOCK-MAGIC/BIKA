package com.shizq.bika.core.json

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JsonModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        // 使用默认值覆盖 null
        coerceInputValues = true
        prettyPrintIndent = "  "
    }
}
