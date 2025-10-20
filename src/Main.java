import java.util.Arrays;

public class Main {
    // 1. Объявление нативного метода, который будет реализован в C
    // Он принимает два целочисленных массива и возвращает новый массив с результатом
    public native int[] sumArrays(int[] arr1, int[] arr2);

    // 2. Статический блок для загрузки нашей C-библиотеки
    static {
        // JVM сама найдет 'Java_C_expansion.dll' в той же папке,
        // где лежат .class файлы, или в путях, указанных в java.library.path
        System.loadLibrary("libJava_C_expansion");
    }

    public static void main(String[] args) {
        // 3. Создаем два массива в Java
        int[] javaArray1 = {10, 20, 30, 40, 50};
        int[] javaArray2 = {5, 15, 25, 35, 45};

        Main summer = new Main();

        // 4. Вызываем нативный метод для сложения массивов
        int[] resultArray = summer.sumArrays(javaArray1, javaArray2);

        // 5. Выводим результат
        System.out.println("Массив 1: " + Arrays.toString(javaArray1));
        System.out.println("Массив 2: " + Arrays.toString(javaArray2));
        System.out.println("Сумма массивов (из C): " + Arrays.toString(resultArray));
    }
}