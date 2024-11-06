package com.shizq.bika.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.shizq.bika.core.datastore.NetworkConfigSerializer
import com.shizq.bika.core.datastore.UserCredentialSerializer
import com.shizq.bika.core.datastore.UserInterestsSerializer
import com.shizq.bika.core.datastore.UserPreferencesSerializer
import com.shizq.bika.core.datastore.model.NetworkConfig
import com.shizq.bika.core.datastore.model.UserCredential
import com.shizq.bika.core.datastore.model.UserInterests
import com.shizq.bika.core.datastore.model.UserPreferences
import com.shizq.bika.core.network.BikaDispatchers.IO
import com.shizq.bika.core.network.Dispatcher
import com.shizq.bika.core.network.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import javax.inject.Singleton

internal val DatastoreJson: Json =
    Json {
        encodeDefaults = true
    }

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    internal fun providesUserCredentialDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userCredentialSerializer: UserCredentialSerializer,
    ): DataStore<UserCredential> =
        DataStoreFactory.create(
            serializer = userCredentialSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_credential.pb")
        }

    @Provides
    @Singleton
    internal fun providesUserInterestsDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userInterestsSerializer: UserInterestsSerializer,
    ): DataStore<UserInterests> =
        DataStoreFactory.create(
            serializer = userInterestsSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("user_interests.pb")
        }

    @Provides
    @Singleton
    internal fun providesBikaNetworkConfigDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        networkConfigSerializer: NetworkConfigSerializer,
    ): DataStore<NetworkConfig> =
        DataStoreFactory.create(
            serializer = networkConfigSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("network_config.pb")
        }
}
