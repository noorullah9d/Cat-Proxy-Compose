#include <jni.h>
#include <string>

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_galixo_cypherlib_NativeLib_getCypherKey(JNIEnv *env, jobject /* this */) {
    std::string obfuscatedKey = "7y43lhuguxe4awpp0wawl4h92qb214uo";
    return env->NewStringUTF(obfuscatedKey.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_galixo_cypherlib_NativeLib_getSecretIV(JNIEnv *env, jobject /* this */) {
    std::string obfuscatedKey = "8z8d8eopsy8l9jq1";
    return env->NewStringUTF(obfuscatedKey.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_galixo_cypherlib_NativeLib_getAuthorizationHeader(JNIEnv *env, jobject /* this */) {
    std::string obfuscatedKey = "Knjana $2h$10qW2T6mNPbWrIj8eBce082.vn9FzFisG7BmLH/5462/AUmJiMQkgoL";
    return env->NewStringUTF(obfuscatedKey.c_str());
}


}
