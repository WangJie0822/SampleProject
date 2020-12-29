package cn.wj.json

import cn.wj.json.internal.NonNullJsonAdapter
import cn.wj.json.internal.NullSafeJsonAdapter
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import java.io.IOException
import java.lang.reflect.Type
import java.math.BigDecimal
import kotlin.reflect.KClass

/**
 *
 * - 创建时间：2020/12/28
 *
 * @author 王杰
 */
abstract class JsonAdapter<T> {

    @Throws(IOException::class)
    abstract fun fromJson(reader: JsonReader): T?

    @Throws(IOException::class)
    fun fromJson(source: BufferedSource): T? {
        return fromJson(JsonReader.of(source))
    }

    @Throws(IOException::class)
    fun fromJson(string: String): T? {
        val reader: JsonReader = JsonReader.of(Buffer().writeUtf8(string))
        val result = fromJson(reader)
        if (!isLenient() && reader.peek() != JsonReader.Token.END_DOCUMENT) {
            throw JsonDataException("JSON document was not fully consumed.")
        }
        return result
    }

    @Throws(IOException::class)
    abstract fun toJson(writer: JsonWriter, value: T?)

    @Throws(IOException::class)
    fun toJson(sink: BufferedSink, value: T?) {
        val writer: JsonWriter = JsonWriter.of(sink)
        toJson(writer, value)
    }

    fun toJson(value: T?): String {
        val buffer = Buffer()
        try {
            toJson(buffer, value)
        } catch (e: IOException) {
            throw AssertionError(e) // No I/O writing to a Buffer.
        }
        return buffer.readUtf8()
    }

    /**
     * Encodes `value` as a Java value object comprised of maps, lists, strings, numbers,
     * booleans, and nulls.
     *
     *
     * Values encoded using `value(double)` or `value(long)` are modeled with the
     * corresponding boxed type. Values encoded using `value(Number)` are modeled as a [ ] for boxed integer types ([Byte], [Short], [Integer], and [Long]),
     * as a [Double] for boxed floating point types ([Float] and [Double]), and as a
     * [BigDecimal] for all other types.
     */
    fun toJsonValue(value: T?): Any? {
        val writer: JsonValueWriter = JsonValueWriter()
        return try {
            toJson(writer, value)
            writer.root()
        } catch (e: IOException) {
            throw java.lang.AssertionError(e) // No I/O writing to an object.
        }
    }

    /**
     * Decodes a Java value object from `value`, which must be comprised of maps, lists,
     * strings, numbers, booleans and nulls.
     */
    fun fromJsonValue(value: Any?): T? {
        val reader = JsonValueReader(value)
        return try {
            fromJson(reader)
        } catch (e: IOException) {
            throw java.lang.AssertionError(e) // No I/O reading from an object.
        }
    }

    /**
     * Returns a JSON adapter equal to this JSON adapter, but that serializes nulls when encoding
     * JSON.
     */
    fun serializeNulls(): JsonAdapter<T> {
        val delegate: JsonAdapter<T> = this
        return object : JsonAdapter<T>() {
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): T? {
                return delegate.fromJson(reader)
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: T?) {
                val serializeNulls: Boolean = writer.getSerializeNulls()
                writer.setSerializeNulls(true)
                try {
                    delegate.toJson(writer, value)
                } finally {
                    writer.setSerializeNulls(serializeNulls)
                }
            }

            override fun isLenient(): Boolean {
                return delegate.isLenient()
            }

            override fun toString(): String {
                return "$delegate.serializeNulls()"
            }
        }
    }

    /**
     * Returns a JSON adapter equal to this JSON adapter, but with support for reading and writing
     * nulls.
     */

    fun nullSafe(): JsonAdapter<T> {
        return this as? NullSafeJsonAdapter ?: NullSafeJsonAdapter<T>(this)
    }

    /**
     * Returns a JSON adapter equal to this JSON adapter, but that refuses null values. If null is
     * read or written this will throw a [JsonDataException].
     *
     *
     * Note that this adapter will not usually be invoked for absent values and so those must be
     * handled elsewhere. This should only be used to fail on explicit nulls.
     */

    fun nonNull(): JsonAdapter<T> {
        return this as? NonNullJsonAdapter ?: NonNullJsonAdapter<T>(this)
    }

    /** Returns a JSON adapter equal to this, but is lenient when reading and writing.  */

    fun lenient(): JsonAdapter<T> {
        val delegate: JsonAdapter<T> = this
        return object : JsonAdapter<T>() {
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): T? {
                val lenient: Boolean = reader.isLenient()
                reader.setLenient(true)
                return try {
                    delegate.fromJson(reader)
                } finally {
                    reader.setLenient(lenient)
                }
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: T?) {
                val lenient: Boolean = writer.isLenient()
                writer.setLenient(true)
                try {
                    delegate.toJson(writer, value)
                } finally {
                    writer.setLenient(lenient)
                }
            }

            override fun isLenient(): Boolean {
                return true
            }

            override fun toString(): String {
                return "$delegate.lenient()"
            }
        }
    }

    /**
     * Returns a JSON adapter equal to this, but that throws a [JsonDataException] when
     * [unknown names and values][JsonReader.setFailOnUnknown] are encountered.
     * This constraint applies to both the top-level message handled by this type adapter as well as
     * to nested messages.
     */

    fun failOnUnknown(): JsonAdapter<T> {
        val delegate: JsonAdapter<T> = this
        return object : JsonAdapter<T>() {
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): T? {
                val skipForbidden: Boolean = reader.failOnUnknown()
                reader.setFailOnUnknown(true)
                return try {
                    delegate.fromJson(reader)
                } finally {
                    reader.setFailOnUnknown(skipForbidden)
                }
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: T?) {
                delegate.toJson(writer, value)
            }

            override fun isLenient(): Boolean {
                return delegate.isLenient()
            }

            override fun toString(): String {
                return "$delegate.failOnUnknown()"
            }
        }
    }

    /**
     * Return a JSON adapter equal to this, but using `indent` to control how the result is
     * formatted. The `indent` string to be repeated for each level of indentation in the
     * encoded document. If `indent.isEmpty()` the encoded document will be compact. Otherwise
     * the encoded document will be more human-readable.
     *
     * @param indent a string containing only whitespace.
     */

    open fun indent(indent: String?): JsonAdapter<T>? {
        if (indent == null) {
            throw NullPointerException("indent == null")
        }
        val delegate: JsonAdapter<T> = this
        return object : JsonAdapter<T>() {
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): T? {
                return delegate.fromJson(reader)
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: T?) {
                val originalIndent: String = writer.getIndent()
                writer.setIndent(indent)
                try {
                    delegate.toJson(writer, value)
                } finally {
                    writer.setIndent(originalIndent)
                }
            }

            override fun isLenient(): Boolean {
                return delegate.isLenient()
            }

            override fun toString(): String {
                return "$delegate.indent(\"$indent\")"
            }
        }
    }

    open fun isLenient(): Boolean {
        return false
    }

    interface Factory {

        /**
         * Attempts to create an adapter for `type` annotated with `annotations`. This
         * returns the adapter if one was created, or null if this factory isn't capable of creating
         * such an adapter.
         *
         *
         * Implementations may use [JSON.adapter] to compose adapters of other types, or
         * [JSON.nextAdapter] to delegate to the underlying adapter of the same type.
         */
        fun create(type: KClass<*>, annotations: Set<Annotation>, json: JSON): JsonAdapter<*>?
    }
}