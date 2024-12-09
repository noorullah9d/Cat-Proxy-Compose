package com.galixo.cypherlib

import androidx.annotation.Keep

@Keep
object NativeLib {
    external fun getCypherKey(): String
    external fun getSecretIV(): String
    external fun getAuthorizationHeader(): String

    fun loadLibrary() {
        try {
            System.loadLibrary("cypher")

        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }
}