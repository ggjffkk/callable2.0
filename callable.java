import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.CopyOnWriteArrayList;

class ArrayMultiplier implements Callable<CopyOnWriteArrayList<Integer>> {
    private final int[] numbers;
    private final int multiplier;

    public ArrayMultiplier(int[] numbers, int multiplier) {
        this.numbers = numbers;
        this.multiplier = multiplier;
    }

    @Override
    public CopyOnWriteArrayList<Integer> call() {
        CopyOnWriteArrayList<Integer> result = new CopyOnWriteArrayList<>();
        for (int number : numbers) {
            result.add(number * multiplier);
        }
        return result;
    }
}

public class callable {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Введення діапазону
        System.out.print("Введіть мінімальне значення діапазону: ");
        int min = scanner.nextInt();
        System.out.print("Введіть максимальне значення діапазону: ");
        int max = scanner.nextInt();
        System.out.print("Введіть множник: ");
        int multiplier = scanner.nextInt();

        // Генерація масиву випадкових чисел
        int size = random.nextInt(21) + 40; // Від 40 до 60
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt((max - min) + 1) + min;
        }

        // Виведення згенерованих чисел
        System.out.println("Згенеровані числа:");
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();

        // Розбиття масиву на частини
        int chunkSize = 10; // Розмір частини
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<CopyOnWriteArrayList<Integer>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numbers.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, numbers.length);
            int[] chunk = new int[end - i];
            System.arraycopy(numbers, i, chunk, 0, end - i);
            ArrayMultiplier task = new ArrayMultiplier(chunk, multiplier);
            Future<CopyOnWriteArrayList<Integer>> future = executor.submit(task);
            futures.add(future);
        }

        // Збір результатів
        CopyOnWriteArrayList<Integer> finalResult = new CopyOnWriteArrayList<>();
        for (Future<CopyOnWriteArrayList<Integer>> future : futures) {
            try {
                finalResult.addAll(future.get()); // Збираємо результати
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Виведення результатів
        System.out.println("Результати обробки:");
        for (int result : finalResult) {
            System.out.print(result + " ");
        }
        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Час виконання програми: " + (endTime - startTime) + " мс");

        // Завершення роботи ExecutorService
        executor.shutdown();
    }
}