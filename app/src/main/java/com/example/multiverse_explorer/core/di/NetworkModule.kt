package com.example.multiverse_explorer.core.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.example.multiverse_explorer.characterdetail.data.network.rest.CharacterDetailApi
import com.example.multiverse_explorer.characterdetail.data.network.rest.EpisodeApi
import com.example.multiverse_explorer.characters.data.network.rest.CharactersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesCharactersApi(retrofit: Retrofit): CharactersApi =
        retrofit.create(CharactersApi::class.java)

    @Singleton
    @Provides
    fun providesCharacterDetailApi(retrofit: Retrofit): CharacterDetailApi =
        retrofit.create(CharacterDetailApi::class.java)

    @Singleton
    @Provides
    fun providesEpisodeApi(retrofit: Retrofit): EpisodeApi =
        retrofit.create(EpisodeApi::class.java)

    @Singleton
    @Provides
    fun providesApolloClient(okHttpClient: OkHttpClient): ApolloClient =
        ApolloClient.Builder().serverUrl("https://rickandmortyapi.com/graphql")
            .okHttpClient(okHttpClient)
            .build()


}