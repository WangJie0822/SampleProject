@file:Suppress("unused")

package com.wj.android.okhttp

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * OkHttp 日志打印拦截器
 * ```
 * OkHttpClient.Builder()
 *       ...
 *       .addNetworkInterceptor(LoggerInterceptor())
 *       ...
 *       .build()
 * ```
 *
 * @param logger 日志打印接口
 * @param level 日志打印等级
 */
class LoggerInterceptor
@JvmOverloads
constructor(
        private val logger: InterceptorLogger = DEFAULT_LOGGER,
        private var level: Int = LEVEL_NONE
) : Interceptor {
    
    @Volatile
    private var headersToRedact = emptySet<String>()
    
    fun redactHeader(name: String) {
        val newHeadersToRedact = TreeSet(String.CASE_INSENSITIVE_ORDER)
        newHeadersToRedact += headersToRedact
        newHeadersToRedact += name
        headersToRedact = newHeadersToRedact
    }
    
    //    @Throws(IOException::class)
    //    override fun intercept(chain: Interceptor.Chain): Response {
    //        // 获取请求对象
    //        val request = chain.request()
    //
    //        // 判断是否打印
    //        if (level == LEVEL_NONE) {
    //            // 不打印日志，直接返回
    //            return chain.proceed(request)
    //        }
    //
    //        // 声明打印文本
    //        val logStr = StringBuilder()
    //
    //        // 标记 - 是否打印数据实体
    //        val logBody = level == LEVEL_BODY
    //        // 标记 - 是否打印头
    //        val logHeaders = logBody || level == LEVEL_HEADERS
    //
    //        // 获取请求数据
    //        val requestBody = request.body
    //
    //        // 获取连接
    //        val connection = chain.connection()
    //        // 获取请求开始信息
    //        val url = request.url
    //        var requestStartMessage =
    //                ("--> ${request.method} $url${if (connection != null) " " + connection.protocol() else ""}")
    //        if (!logHeaders && requestBody != null) {
    //            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
    //        }
    //        logStr.appendln(requestStartMessage)
    //
    //        if (logHeaders) {
    //            val headers = request.headers
    //
    //            if (requestBody != null) {
    //                // Request body headers are only present when installed as a network interceptor. When not
    //                // already present, force them to be included (if available) so their values are known.
    //                requestBody.contentType()?.let {
    //                    if (headers["Content-Type"] == null) {
    //                        logStr.appendln("Content-Type: $it")
    //                    }
    //                }
    //                if (requestBody.contentLength() != -1L) {
    //                    if (headers["Content-Length"] == null) {
    //                        logStr.appendln("Content-Length: ${requestBody.contentLength()}")
    //                    }
    //                }
    //            }
    //
    //            // 获取拼接参数
    //            logStr.appendln(">> START QueryParameters")
    //            val parametersMaxLength = url.queryParameterNames.maxBy { it.length }?.length ?: 0
    //            for (name in url.queryParameterNames) {
    //                for (value in url.queryParameterValues(name)) {
    //                    logStr.appendln("\t${name.fixLength(parametersMaxLength)}\t: \t${value}")
    //                }
    //            }
    //            logStr.appendln(">> END QueryParameters\n")
    //
    //            // 获取请求头
    //            logStr.appendln(">> START Headers")
    //            for (i in 0 until headers.size) {
    //                logHeader(logStr, headers, i)
    //            }
    //            logStr.appendln(">> END Headers")
    //
    //            if (!logBody || requestBody == null) {
    //                logStr.appendln("--> END ${request.method}")
    //            } else if (bodyHasUnknownEncoding(request.headers)) {
    //                logStr.appendln("--> END ${request.method} (encoded body omitted)")
    //            } else if (requestBody.isDuplex()) {
    //                logStr.appendln("--> END ${request.method} (duplex request body omitted)")
    //            } else {
    //                val buffer = Buffer()
    //                requestBody.writeTo(buffer)
    //
    //                val contentType = requestBody.contentType()
    //                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
    //
    //                logStr.appendln("")
    //                if (buffer.isProbablyUtf8()) {
    //                    logStr.appendln(buffer.readString(charset))
    //                    logStr.appendln("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
    //                } else {
    //                    logStr.appendln(
    //                            "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
    //                }
    //            }
    //        }
    //
    //        // 记录请求开始时间
    //        val startNs = System.nanoTime()
    //        val response: Response
    //        try {
    //            response = chain.proceed(request)
    //        } catch (e: Exception) {
    //            logStr.appendln("<-- HTTP FAILED: $e")
    //            logger.invoke(logStr.toString())
    //            throw e
    //        }
    //
    //        // 计算请求耗时
    //        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
    //
    //        // 获取响应体
    //        val responseBody = response.body!!
    //        val contentLength = responseBody.contentLength()
    //        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
    //        logStr.appendln(
    //                "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})")
    //
    //        if (logHeaders) {
    //            val headers = response.headers
    //            for (i in 0 until headers.size) {
    //                logHeader(logStr, headers, i)
    //            }
    //
    //            if (!logBody || !response.promisesBody()) {
    //                logStr.appendln("<-- END HTTP")
    //            } else if (bodyHasUnknownEncoding(response.headers)) {
    //                logStr.appendln("<-- END HTTP (encoded body omitted)")
    //            } else {
    //                val source = responseBody.source()
    //                source.request(Long.MAX_VALUE) // Buffer the entire body.
    //                var buffer = source.buffer
    //
    //                var gzippedLength: Long? = null
    //                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
    //                    gzippedLength = buffer.size
    //                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
    //                        buffer = Buffer()
    //                        buffer.writeAll(gzippedResponseBody)
    //                    }
    //                }
    //
    //                val contentType = responseBody.contentType()
    //                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
    //
    //                if (!buffer.isProbablyUtf8()) {
    //                    logStr.appendln("")
    //                    logStr.appendln("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
    //                    // 打印日志
    //                    logger.invoke(logStr.toString())
    //                    return response
    //                }
    //
    //                if (contentLength != 0L) {
    //                    // 打印返回数据
    //                    logStr.appendln("")
    //                    val json = buffer.clone().readString(charset)
    //                    logStr.appendln(json)
    //                    // 对 json 数据进行格式化
    //                    val jsonFormat = json.jsonFormat()
    //                    // 如果数据长度超过 200，截取中间部分打印
    //                    logStr.appendln(
    //                            if (jsonFormat.length > 200)
    //                                "${jsonFormat.substring(0, 100)}\n\n The Json String was too long...\n\n ${
    //                                jsonFormat.substring(jsonFormat.length - 100)}\n"
    //                            else
    //                                "$jsonFormat\n\n"
    //                    )
    //                }
    //
    //                if (gzippedLength != null) {
    //                    logStr.appendln("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
    //                } else {
    //                    logStr.appendln("<-- END HTTP (${buffer.size}-byte body)")
    //                }
    //            }
    //        }
    //
    //        // 打印日志
    //        logger.invoke(logStr.toString())
    //        return response
    //    }
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        
        // 获取请求对象
        val request = chain.request()
        
        // 判断是否打印
        if (level == LEVEL_NONE) {
            // 不打印日志，直接返回
            return chain.proceed(request)
        }
        
        // 标记-是否打印数据实体
        val logBody = level == LEVEL_BODY
        // 标记-是否打印头
        val logHeaders = logBody || level == LEVEL_HEADERS
        
        // 声明打印文本
        val logStr = StringBuilder()
        
        // 获取请求数据
        val requestBody = request.body
        
        // 获取连接
        val connection = chain.connection()
        // 获取协议
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        // 拼接文本
        val url = request.url
        logStr.appendln("--> ${request.method} $url $protocol")
        if (!logHeaders && requestBody != null) {
            logStr.appendln(" (${requestBody.contentLength()}-byte body")
        }
        
        if (logHeaders) {
            if (requestBody != null) {
                if (requestBody.contentType() != null) {
                    logStr.appendln("Content-Type ${requestBody.contentType()}")
                }
                if (requestBody.contentLength() != -1L) {
                    logStr.appendln("Content-Length: ${requestBody.contentLength()}")
                }
            }
            
            // 获取拼接参数
            logStr.appendln(">> START QueryParameters")
            val parametersMaxLength = url.queryParameterNames.maxBy { it.length }?.length ?: 0
            for (name in url.queryParameterNames) {
                for (value in url.queryParameterValues(name)) {
                    logStr.appendln("\t${name.fixLength(parametersMaxLength)}\t: \t${value}")
                }
            }
            logStr.appendln(">> END QueryParameters\n")
            
            // 获取请求头
            val headers = request.headers
            val headersMaxLength = headers.names().maxBy { it.length }?.length ?: 0
            logStr.appendln(">> START Headers")
            for (i in 0 until headers.size) {
                val name = headers.name(i)
                if (!"Content-Type".equals(name, true) && !"Content-Length".equals(name, true)) {
                    logStr.appendln("\t${name.fixLength(headersMaxLength)}\t: \t${headers.value(i)}")
                }
            }
            logStr.appendln(">> END Headers")
            
            when {
                !logBody || requestBody == null ->
                    logStr.appendln("--> END ${request.method}")
                bodyEncoded(request.headers) ->
                    logStr.appendln("--> END ${request.method} (encoded body omitted)")
                else -> {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    
                    val contentType = requestBody.contentType()
                    val charset = contentType?.charset(UTF_8) ?: UTF_8
                    
                    logStr.appendln()
                    if (isPlaintext(buffer)) {
                        logStr.appendln(">> ${contentType.toString()}")
                        logStr.appendln("  |${buffer.readString(charset)}\n")
                        logStr.appendln("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                    } else {
                        logStr.appendln("--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted")
                    }
                }
            }
        }
        
        // 记录请求开始时间
        val startMs = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            logStr.appendln("<-- HTTP FAILED: $e")
            logger.invoke(logStr.toString())
            throw e
        }
        // 计算请求耗时
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startMs)
        
        // 获取响应体
        val responseBody = response.body
        
        if (responseBody != null) {
            logStr.appendln(
                    "<-- ${response.code} ${response.message} ${response.request.url}" +
                            " (${tookMs}ms${if (!logHeaders)
                                ", ${if (responseBody.contentLength() != -1L) "$responseBody-byte"
                                else "unknown-length"} body"
                            else ""})"
            )
            
            if (logHeaders) {
                val headers = response.headers
                for (i in 0 until headers.size) {
                    logStr.appendln("${headers.name(i)}: ${headers.value(i)}")
                }
                
                when {
                    !logBody ->
                        logStr.appendln("<-- END HTTP")
                    bodyEncoded(response.headers) ->
                        logStr.appendln("--< END HTTP (encoded body omitted")
                    else -> {
                        val source = responseBody.source()
                        source.request(Long.MAX_VALUE)
                        val buffer = source.buffer
                        
                        val contentType = responseBody.contentType()
                        val charset = contentType?.charset(UTF_8) ?: UTF_8
                        
                        if (!isPlaintext(buffer)) {
                            logStr.appendln()
                            logStr.appendln("<-- END HTTP (binary ${buffer.size}-byte body omitted")
                            logger.invoke(logStr.toString())
                            return response
                        }
                        
                        if (responseBody.contentLength() != 0L) {
                            logStr.appendln()
                            val json = buffer.clone().readString(charset)
                            logStr.appendln(json)
                            val jsonFormat = json.jsonFormat()
                            logStr.appendln(
                                    if (jsonFormat.length > 200)
                                        "${jsonFormat.substring(0, 100)}\n\n The Json String was too long...\n\n ${
                                        jsonFormat.substring(jsonFormat.length - 100)}"
                                    else
                                        "$jsonFormat\n"
                            )
                        }
                        logStr.appendln("<-- END HTTP (${buffer.size}-byte body)")
                    }
                }
            }
        }
        logger.invoke(logStr.toString())
        return response
    }
    
    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }
    
    /**
     * 检测是否包含可读文本
     *
     * @return 是否包含
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            var plaintext = true
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    plaintext = false
                }
            }
            plaintext
        } catch (e: EOFException) {
            false // Truncated UTF-8 sequence.
        }
    }
    
    private fun logHeader(logStr: StringBuilder, headers: Headers, i: Int) {
        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
        logStr.appendln(headers.name(i) + ": " + value)
    }
    
    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
    
    companion object {
        
        /** 没有日志输出 */
        const val LEVEL_NONE = 0
        
        /**
         * 输出请求和响应行
         *
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
        const val LEVEL_BASIC = 1
        
        /**
         * 输出请求和响应行及其各自的头
         *
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
        const val LEVEL_HEADERS = 2
        
        /**
         * 输出请求和响应行及其各自的头和正文(如果存在)
         *
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
         *
         */
        const val LEVEL_BODY = 3
    }
}

//private fun Buffer.isProbablyUtf8(): Boolean {
//    try {
//        val prefix = Buffer()
//        val byteCount = size.coerceAtMost(64)
//        copyTo(prefix, 0, byteCount)
//        for (i in 0 until 16) {
//            if (prefix.exhausted()) {
//                break
//            }
//            val codePoint = prefix.readUtf8CodePoint()
//            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
//                return false
//            }
//        }
//        return true
//    } catch (_: EOFException) {
//        return false
//    }
//}

/**
 * 补全长度
 */
private fun String.fixLength(maxLength: Int): String {
    return if (this.length >= maxLength) {
        this
    } else {
        val sb = StringBuilder(this)
        for (i in 0 until maxLength - this.length) {
            sb.append(" ")
        }
        sb.toString()
    }
}

/**
 * 对 json 字符串进行格式化
 *
 * @return json 格式化完成字符串
 */
private fun String?.jsonFormat(): String {
    val json = this.orEmpty().trim()
    return try {
        when {
            json.startsWith("{") -> JSONObject(json).toString(2)
            json.startsWith("[") -> JSONArray(json).toString(2)
            else -> ""
        }
    } catch (e: JSONException) {
        ""
    }
}