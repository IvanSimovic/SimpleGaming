package com.simovic.simplegaming.base

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.simovic.simplegaming.BuildConfig
import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResultAdapterFactory
import com.simovic.simplegaming.base.data.retrofit.interceptor.AuthenticationInterceptor
import com.simovic.simplegaming.base.data.retrofit.interceptor.UserAgentInterceptor
import com.simovic.simplegaming.base.presentation.ScaffoldController
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber

val appModule =
    module {

        single { AuthenticationInterceptor(BuildConfig.apiToken) }

        viewModel { ScaffoldController() }

        singleOf(::UserAgentInterceptor)

        single {
            HttpLoggingInterceptor { message ->
                Timber.d("Http: $message")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

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
            OkHttpClient
                .Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(get<HttpLoggingInterceptor>())
                    }
                    addInterceptor(get<AuthenticationInterceptor>())
                    addInterceptor(get<UserAgentInterceptor>())
                }.build()
        }

        single {
            val contentType = "application/json".toMediaType()

            val json =
                Json {
                    // By default Kotlin serialization will serialize all of the keys present in JSON object and throw an
                    // exception if given key is not present in the Kotlin class. This flag allows to ignore JSON fields
                    ignoreUnknownKeys = true
                }

            @OptIn(ExperimentalSerializationApi::class)
            Retrofit
                .Builder()
                .baseUrl(BuildConfig.apiBaseUrl)
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(get())
                .addCallAdapterFactory(ApiResultAdapterFactory())
                .build()
        }

        single(named("xmlRetrofit")) {
            Retrofit
                .Builder()
                .baseUrl("https://indiegamesplus.com/")
                .addConverterFactory(
                    TikXmlConverterFactory.create(
                        TikXml
                            .Builder()
                            .exceptionOnUnreadXml(false)
                            .build(),
                    ),
                ).build()
        }
    }
