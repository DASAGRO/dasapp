package kz.das.dasaccounting.data.source.network.interceptors

import kz.das.dasaccounting.core.extensions.hexToString
import okhttp3.Interceptor
import okhttp3.Response

internal val HEADER_ACCESS_TOKEN = "417574686f72697a6174696f6e".hexToString()
internal val HEADER_ROLE = "582d526f6c65".hexToString()

class HeadersInterceptor(
    private val accessTokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        accessTokenProvider.invoke()?.let {
            requestBuilder.addHeader(HEADER_ACCESS_TOKEN, "Bearer $it")
        }

            // TODO add version name android
            //requestBuilder.addHeader(HEADER_APP_VERSION, BuildConfig.APP_VERSION_NAME)

        return chain.proceed(requestBuilder.build())
    }
}