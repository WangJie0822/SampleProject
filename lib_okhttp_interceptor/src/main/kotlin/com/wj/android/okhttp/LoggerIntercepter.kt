package com.wj.android.okhttp

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
 *
 * The format of the logs created by this class should not be considered stable and may
 * change slightly between releases. If you need a stable logging format, use your own interceptor.
 */
class LoggerInterceptor @JvmOverloads constructor(
        private val logger: InterceptorLogger = InterceptorLogger.DEFAULT,
        level: Level = Level.BASIC
) : Interceptor {

    @Volatile
    private var headersToRedact = emptySet<String>()

    @set:JvmName("level")
    @Volatile
    var mLevel = level

    enum class Level {
        /** No logs. */
        NONE,

        /**
         * Logs request and response lines.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * ```
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * ```
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * ```
         */
        BODY
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.mLevel

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body

        val connection = chain.connection()
        var requestStartMessage =
                ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (!logHeaders && requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        val sb = StringBuilder()
        sb.appendNewline(requestStartMessage)

        if (logHeaders) {
            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                requestBody.contentType()?.let {
                    sb.appendNewline("Content-Type: $it")
                }
                if (requestBody.contentLength() != -1L) {
                    sb.appendNewline("Content-Length: ${requestBody.contentLength()}")
                }
            }

            val headers = request.headers
            for (i in 0 until headers.size) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) &&
                        !"Content-Length".equals(name, ignoreCase = true)) {
                    sb.appendNewline(logHeader(headers, i))
                }
            }

            if (!logBody || requestBody == null) {
                sb.appendNewline("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                sb.appendNewline("--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                sb.appendNewline("--> END ${request.method} (duplex request body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8

                sb.appendNewline("")
                if (buffer.isProbablyUtf8()) {
                    sb.appendNewline(buffer.readString(charset))
                    sb.appendNewline("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    sb.appendNewline(
                            "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            sb.appendNewline("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        sb.appendNewline(
                "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})")

        if (logHeaders) {
            val headers = response.headers
            for (i in 0 until headers.size) {
                sb.appendNewline(logHeader(headers, i))
            }

            if (!logBody || !response.promisesBody()) {
                sb.appendNewline("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                sb.appendNewline("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8

                if (!buffer.isProbablyUtf8()) {
                    sb.appendNewline("")
                    sb.appendNewline("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    logger.log(sb.toString())
                    return response
                }

                if (contentLength != 0L) {
                    sb.appendNewline("")
                    sb.appendNewline(buffer.clone().readString(charset))
                }

                if (gzippedLength != null) {
                    sb.appendNewline("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    sb.appendNewline("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }

        logger.log(sb.toString())
        return response
    }

    private fun logHeader(headers: Headers, i: Int): String {
        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
        return headers.name(i) + ": " + value
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small
     * sample of code points to detect unicode control characters commonly used in binary file
     * signatures.
     */
    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun StringBuilder.appendNewline(str: String) {
        this.append(str).append("\n")
    }
}


