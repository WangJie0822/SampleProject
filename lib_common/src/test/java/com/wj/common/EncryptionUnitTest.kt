package com.wj.common

import cn.wj.common.tools.toHexByteArray
import cn.wj.common.tools.toHexString
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 加密相关单元测试
 */
class EncryptionUnitTest {

    @Test
    fun toHexByteArrayTest() {
        val source = "呵呵哈各位各位"
        println("source: $source")
        val hexString = source.toByteArray().toHexString()
        println("hexString: $hexString")
        val hexByteArray = hexString.toHexByteArray()
        println("hexByteArray: $hexByteArray")
        val resultString = hexByteArray.toHexString()
        println("resultString: $resultString")
        val resultSource = hexByteArray.decodeToString()
        println("resultSource: $resultSource")

        assertEquals(hexString, resultString)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
