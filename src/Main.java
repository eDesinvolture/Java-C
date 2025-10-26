import java.util.Arrays;
import java.util.Random;



public class Main {
    // базовый размер массива
    // private static final int DEFAULT_SIZE = 10_000;
    // константа размера массива
    // private static final int DEFAULT_SIZE = 1_000;
    // private static final int DEFAULT_SIZE = 10_000;  // можно поменять на любое значение
    // private static final int DEFAULT_SIZE = 1_000_000;
    // private static final int DEFAULT_SIZE = 1024 * 1024;
    private static final int DEFAULT_SIZE = 1024 * 1024;



    public native int[] sumArrays(int[] arr1, int[] arr2);

    static {
        System.loadLibrary("Java_C_expansion");
    }

    public static void main(String[] args) {
        // читаем размер из аргументов, если передан
        int size = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_SIZE;

        System.out.printf("Размер массива: %,d%n", size);

        Random rnd = new Random();
        int[] javaArray1 = new int[size];
        int[] javaArray2 = new int[size];

        for (int i = 0; i < size; ++i) {
            javaArray1[i] = rnd.nextInt(1000);
            javaArray2[i] = rnd.nextInt(1000);
        }

        Main summer = new Main();

        // --- первый проход: JNI ---
        long startJNI = System.nanoTime();
        int[] resultArray = summer.sumArrays(javaArray1, javaArray2);
        long endJNI = System.nanoTime();
        double deltaJNIMicros = (endJNI - startJNI) / 1000.0;

        // --- второй проход: Java ---
        long startJava = System.nanoTime();
        int[] javaRef = new int[size];
        for (int i = 0; i < size; ++i)
            javaRef[i] = javaArray1[i] + javaArray2[i];
        long endJava = System.nanoTime();
        double deltaJavaMicros = (endJava - startJava) / 1000.0;

        // печатаем массивы только если маленькие
        if (size < 30) {
            System.out.println("Массив 1: " + Arrays.toString(javaArray1));
            System.out.println("Массив 2: " + Arrays.toString(javaArray2));
            System.out.println("Сумма массивов (C): " + Arrays.toString(resultArray));
            System.out.println("Сумма массивов (Java): " + Arrays.toString(javaRef));
        }

        // выводим в формате, который легко парсить Python-скриптом
        System.out.printf("Время вызова JNI: %.3f%n", deltaJNIMicros);
        System.out.printf("Время выполнения Java-версии: %.3f%n", deltaJavaMicros);

        // проверка корректности
        boolean ok = Arrays.equals(resultArray, javaRef);
        System.out.println("Результаты совпадают: " + ok);
    }
}
