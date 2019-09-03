@file:Suppress("unused")

package cn.wj.android.base.ext

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * 字符串非空类型转换
 * - 调用对象为 null，则从候选列表中获取
 * - 若调用对象为 null，且获选列表为空或都为 null，则返回空字符串
 *
 * @param str 候选字符串列表
 */
fun String?.orEmpty(vararg str: String?): String {
    if (str.isEmpty()) {
        // 没有参数
        return this ?: ""
    } else {
        // 有参数
        return if (this.isNullOrEmpty()) {
            var ret: String? = null
            for (s in str) {
                if (!s.isNullOrEmpty()) {
                    ret = s
                    break
                }
            }
            ret ?: ""
        } else {
            this
        }
    }
}

/**
 * 对 json 字符串进行格式化
 *
 * @return json 格式化完成字符串
 */
fun String?.jsonFormat(): String {
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

/**
 * 对 xml 字符串进行格式化
 *
 * @return xml 格式化后的字符串
 */
fun String?.xmlFormat(): String {
    if (this.isNullOrBlank()) {
        return ""
    }
    val xml = this.orEmpty().trim()
    return try {
        val xmlInput = StreamSource(StringReader(xml))
        val xmlOutput = StreamResult(StringWriter())
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(xmlInput, xmlOutput)
        xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
    } catch (e: TransformerException) {
        ""
    }
}
