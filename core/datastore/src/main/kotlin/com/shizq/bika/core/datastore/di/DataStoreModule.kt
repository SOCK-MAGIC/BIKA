package com.shizq.bika.core.datastore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    internal fun providesAsCookieStoreDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        asCookieStoreSerializer: AsCookieStoreSerializer,
    ): DataStore<AsCookieStore> =
        DataStoreFactory.create(
            serializer = asCookieStoreSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("as_cookie.pb")
        }

    @Provides
    @Singleton
    internal fun providesUsersDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        usersSerializer: UsersSerializer,
    ): DataStore<Users> =
        DataStoreFactory.create(
            serializer = usersSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("users.pb")
        }
}