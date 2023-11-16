package com.example.rocketreserver.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.rocketreserver.data.TokenRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient

import okhttp3.Response


val apolloClient = ApolloClient.Builder()

    .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
    .okHttpClient(
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    )
    .build()


private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                TokenRepository.getToken()?.let { token ->
                    addHeader("Authorization", token)
                }
            }
            .build()
        return chain.proceed(request)
    }

}