#include "Main.h" // Подключаем сгенерированный заголовок

// Реализация нативного метода
extern "C" {
  JNIEXPORT jintArray JNICALL Java_Main_sumArrays
  (JNIEnv *env, jobject obj, jintArray arr1, jintArray arr2) {

    // 1. Получаем указатели на элементы Java-массивов
    jint *body1 = env->GetIntArrayElements(arr1, 0);
    jint *body2 = env->GetIntArrayElements(arr2, 0);

    // 2. Получаем размеры массивов
    jsize len = env->GetArrayLength(arr1);

    // 3. Создаем новый Java-массив для результата
    jintArray resultArray = env->NewIntArray(len);
    jint *resultBody = env->GetIntArrayElements(resultArray, 0);

    // 4. Выполняем сложение элементов
    for (int i = 0; i < len; i++) {
      resultBody[i] = body1[i] + body2[i];
    }

    // 5. Освобождаем ресурсы (копируем изменения обратно в Java-массивы и освобождаем память)
    // Для массивов, которые мы только читали, используем JNI_ABORT, чтобы не копировать данные обратно
    env->ReleaseIntArrayElements(arr1, body1, JNI_ABORT);
    env->ReleaseIntArrayElements(arr2, body2, JNI_ABORT);
    // Для результирующего массива используем 0, чтобы скопировать результат в Java-массив
    env->ReleaseIntArrayElements(resultArray, resultBody, 0);

    // 6. Возвращаем Java-массив с результатом
    return resultArray;
  }
}
