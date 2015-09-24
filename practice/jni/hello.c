#include<jni.h>

JNIEXPORT jstring JNICALL Java_com_example_practice_mynative_TestJni_getStringFromNative
  (JNIEnv *, jobject){
       char* str = "Hello From JNI!";
      return (*env)->NewStringUTF(env,str);
  }