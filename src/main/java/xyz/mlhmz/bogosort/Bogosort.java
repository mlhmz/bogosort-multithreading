package xyz.mlhmz.bogosort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Bogosort {
    private static final Bogosort instance = new Bogosort();
    private int trys = 0;

    public static Bogosort getInstance() {
        return instance;
    }

    public int[] multithreadSort(int threads, int[] array) {
        AtomicReference<int[]> reference = new AtomicReference<>();
        Runnable runnable = () -> {
            int[] sort = sort(array);
            if (sort != null) {
                reference.set(sort);
            }
        };
        ArrayList<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(runnable);
            thread.setName("BogoThread-" + i);
            threadList.add(thread);
            thread.start();
        }
        while (true) {
            System.out.println("Multithreaded Bogosort");
            System.out.println("As soon as a benchmark result appears, a sort was finished and a stop can be triggered");
            System.out.println("Commands:");
            System.out.println("stop - Ends all threads and gets the reference result");
            System.out.println("info - Shows all threads information");
            String input = new Scanner(System.in).next();
            switch (input) {
                case "stop" -> {
                    if (reference.get() == null) {
                        System.out.println("No thread resulted successfully. Returning the unsorted version");
                        return array;
                    }
                    threadList.forEach(Thread::interrupt);
                    return reference.get();
                }
                case "info" -> printThreadInformation(threadList);
                default -> System.out.println("Unknown commands. Available commands: 'stop', 'info'.");
            }
        }
    }

    private void printThreadInformation(List<Thread> threadList) {
        System.out.println("Thread Name\t\tThread State");
        threadList.forEach(thread -> System.out.printf("%s\t\t%s%n", thread.getName(), thread.getState().toString()));
    }

    public int[] sort(int[] array) {
        long startMillis = System.currentTimeMillis();
        int[] result = array;
        for (int i = 1; i < array.length; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            if (array[i - 1] > array[i]) {
                trys++;
                i = 0;
                array = IntArrayUtils.shuffleArray(array);
            } else {
                result = array;
            }
        }
        long endMillis = System.currentTimeMillis();
        long requiredMillis = endMillis - startMillis;
        long requiredTimeInSeconds = requiredMillis / 1000;
        System.out.printf("Result Benchmark: Thread %s, Required millis %d, Required seconds %d, Required trys %d%n",
                Thread.currentThread().getName(), requiredMillis, requiredTimeInSeconds, trys);
        return result;
    }
}
