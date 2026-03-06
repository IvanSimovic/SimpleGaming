package com.simovic.simplegaming.base

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.simovic.simplegaming.BuildConfig
import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResultAdapterFactory
import com.simovic.simplegaming.base.presentation.AppViewModel
import com.simovic.simplegaming.base.presentation.ScaffoldController
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber

val appModule =
    module {

        viewModel { ScaffoldController() }

        viewModelOf(::AppViewModel)

        /*
         * OkHttp logging interceptor with custom Timber logger.
         *
         * By default, HttpLoggingInterceptor uses the calling class name as the log tag which clutters Logcat and makes filtering harder.
         *
         * This custom configuration ensures:
         * - All HTTP logs are tagged consistently as `"Network"`.
         * - Logs are printed through Timber (instead of Android's `Log`).
         * - Logging level is set to BODY to include headers and payloads.
         */
        single {
            HttpLoggingInterceptor { message ->
                Timber.tag("Network").d(message)
            }.apply {
                /*
                Use BODY logging only in debug builds.
                Even if Timber.DebugTree() is planted only in debug, the interceptor still
                reads/constructs request/response bodies when level = BODY.
                This adds unnecessary overhead and may leak sensitive data if any logger
                is active in production. Setting NONE in release avoids both risks.
                 */
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
            }
        }

        single {
            Json {
                // By default Kotlin serialization will serialize all of the keys present in JSON object and throw an
                // exception if given key is not present in the Kotlin class. This flag allows to ignore JSON fields
                ignoreUnknownKeys = true
            }
        }

        single(named("rawgOkHttp")) {
            OkHttpClient
                .Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(get<HttpLoggingInterceptor>())
                    }
                    addInterceptor { chain ->
                        val url =
                            chain
                                .request()
                                .url
                                .newBuilder()
                                .addQueryParameter("key", BuildConfig.rawgApiKey)
                                .build()
                        chain.proceed(
                            chain
                                .request()
                                .newBuilder()
                                .url(url)
                                .build(),
                        )
                    }
                }.build()
        }

        single(named("rawgRetrofit")) {
            val contentType = "application/json".toMediaType()

            @OptIn(ExperimentalSerializationApi::class)
            Retrofit
                .Builder()
                .baseUrl("https://api.rawg.io/api/")
                .addConverterFactory(get<Json>().asConverterFactory(contentType))
                .client(get(named("rawgOkHttp")))
                .addCallAdapterFactory(ApiResultAdapterFactory())
                .build()
        }
    }
