package com.galixo.proxy.compose.data.repository

import android.os.Build
import com.galixo.cypherlib.NativeLib
import com.galixo.proxy.compose.data.dto.DecodeModelDto
import com.google.gson.Gson
import com.galixo.proxy.compose.domain.repository.CypherRepository
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CypherRepositoryImpl : CypherRepository {

    override suspend fun decryptText(encryptedText: String): String {
        var plainText = ""
        if (encryptedText.isEmpty()) {
            return plainText
        }
        try {
            val cipherTextByteArray =
                decodeBase64(encryptedText) ?: return plainText
            val (iv, value) = Gson().fromJson(
                String(cipherTextByteArray),
                DecodeModelDto::class.java
            )
            if (iv.isEmpty()) {
                return plainText
            }
            if (value.isEmpty()) {
                return plainText
            }

            val keyValue = getShift(NativeLib.getCypherKey()).toByteArray()
            val key: Key = SecretKeySpec(keyValue, "AES")
            val c = Cipher.getInstance("AES/CBC/PKCS7Padding")
            c.init(
                Cipher.DECRYPT_MODE, key, IvParameterSpec(
                    iv.toByteArray()
                )
            )
            val decodedValue = decodeBase64(value)
            val decValue = c.doFinal(decodedValue)
            plainText = String(decValue)
            return plainText
        } catch (e: java.lang.Exception) {
            plainText = ""
            return plainText
        }
    }

    override suspend fun decryptAES256CBC(encryptedText: String): String {
        try {
            val secretKey = getShift(NativeLib.getCypherKey()).toByteArray()
            val iv = getShift(NativeLib.getSecretIV()).toByteArray()

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val secretKeySpec = SecretKeySpec(secretKey, "AES")
            val ivParams = IvParameterSpec(iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParams)

            val encryptedBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getDecoder().decode(encryptedText)
            } else {
                android.util.Base64.decode(encryptedText, android.util.Base64.DEFAULT)
            }
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            return String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    override suspend fun getShift(input: String): String {
        val actualShift = 26 - (9 % 26)
        val output = StringBuilder(input.length)

        for (c in input) {
            when (c) {
                in 'A'..'Z' -> {
                    val shiftedChar = 'A' + (c - 'A' + actualShift) % 26
                    output.append(shiftedChar)
                }
                in 'a'..'z' -> {
                    val shiftedChar = 'a' + (c - 'a' + actualShift) % 26
                    output.append(shiftedChar)
                }
                else -> output.append(c)
            }
        }
        return output.toString()
    }

    private fun decodeBase64(base64String: String?): ByteArray? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(base64String)
        } else
            android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
    }
}