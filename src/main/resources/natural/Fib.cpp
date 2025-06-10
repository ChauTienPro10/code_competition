#include "myself_programing_coding_TestCallCLib.h"

extern "C"
{
    JNIEXPORT jint JNICALL Java_myself_programing_coding_TestCallCLib_fib(JNIEnv *env, jobject obj, jint n)
    {
        if (n <= 1)
            return n;
        return Java_myself_programing_coding_TestCallCLib_fib(env, obj, n - 1) +
               Java_myself_programing_coding_TestCallCLib_fib(env, obj, n - 2);
    }

    JNIEXPORT jint JNICALL Java_myself_programing_coding_TestCallCLib_factorial(JNIEnv *env, jobject obj, jint n)
    {
        if (n <= 1)
            return 1;
        return n * Java_myself_programing_coding_TestCallCLib_factorial(env, obj, n - 1);
    }
}
