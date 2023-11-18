package com.example.rocketreserver.di

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.rocketreserver.remote.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): Pair<String, String> = Pair(
        "https://apollo-fullstack-tutorial.herokuapp.com/graphql", // base URL
        "wss://apollo-fullstack-tutorial.herokuapp.com/graphql"  //Socket URL
    )


    @Singleton
    @Provides
    fun provideApolloClient(
        baseURL: Pair<String, String>
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(baseURL.first)
            .webSocketServerUrl(baseURL.second)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor())
                    .build()
            )
            .webSocketReopenWhen { throwable, attempt ->
                Log.d("Apollo", "WebSocket got disconnected, reopening after a delay", throwable)
                delay(attempt * 1000)
                true
            }
            .build()
    }
}