package com.v2ray.ang.compose.domain.repository

interface CypherRepository {
    suspend fun decryptText(encryptedText: String): String

    suspend fun decryptAES256CBC(encryptedText: String): String

    suspend fun getShift(input: String):String
}