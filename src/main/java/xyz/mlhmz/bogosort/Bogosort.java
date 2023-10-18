package xyz.mlhmz.bogosort;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Bogosort {
    private static Bogosort instance = new Bogosort();
    private int trys = 0;

    public static Bogosort getInstance() {
        return instance;
    }

    public int[] multithreadSort(int threads, int[] array) throws InterruptedException {
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
            if (reference.get() != null) {
                threadList.forEach(Thread::interrupt);
                return reference.get();
            }
            Thread.sleep(100);
        }
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
