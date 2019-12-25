#include <jni.h>
#include <string>
#include <cstring>
#include <unistd.h>
#include <sys/inotify.h>
#include<pthread.h>
#include<sys/wait.h>
#include <signal.h>
#include <sys/select.h>
#include <stdio.h>
#include <sys/ptrace.h>
#include <stdlib.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/types.h>
#include <assert.h>
#include <android/log.h>

jstring Check(
        JNIEnv *env,
        jobject,
        jstring s) {
    return env->NewStringUTF("Tql");
}

static const char *gClassName = "com/example/administrator/crackme/MainActivity";

static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}


static JNINativeMethod gMethods[] = {
        {"Check", "(Ljava/lang/String;)Ljava/lang/String;", (void*)Check},
};
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    else{

        gMethods->fnPtr=(void*)Check;

    }

    if (registerNativeMethods(env, gClassName, gMethods,
                              sizeof(gMethods) / sizeof(gMethods[0])) == JNI_FALSE) {
        return -1;
    }

    return JNI_VERSION_1_6;
}