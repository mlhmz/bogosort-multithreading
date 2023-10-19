package xyz.mlhmz.bogosort;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Bogosort {
    private static final Bogosort instance = new Bogosort();
    private int trys = 0;

    public static Bogosort getInstance() {
        return instance;
    }

    public int[] multithreadSort(int threads, int[] array) throws InterruptedException {
        BlockingQueue<int[]> queue = new LinkedBlockingQueue<>();
        Runnable sortRunnable = getSortRunnable(array, queue);
        ArrayList<Thread> threadList = new ArrayList<>();
        fillThreadList(threads, sortRunnable, threadList);
        int[] take = queue.take();
        threadList.forEach(Thread::interrupt);
        return take;
    }

    public int[] sort(int[] array) throws InterruptedException {
        long startMillis = System.currentTimeMillis();
        int[] result = array;
        for (int i = 1; i < array.length; i++) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
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

    private void fillThreadList(int threadAmount, Runnable sortRunnable, ArrayList<Thread> threadList) {
        for (int i = 0; i < threadAmount; i++) {
            Thread thread = new Thread(sortRunnable);
            String name = "BogoThread-" + i;
            thread.setName(name);
            threadList.add(thread);
            thread.start();
            System.out.printf("Started Thread %s.%n", name);
        }
    }

    private Runnable getSortRunnable(int[] array, BlockingQueue<int[]> queue) {
        return () -> {
            int[] sort;
            try {
                sort = sort(array);
                queue.put(sort);
            } catch (InterruptedException e) {
                System.out.printf("The thread %s was interrupted.%n", Thread.currentThread().getName());
                // Continue Thread Interruption after catching it
                Thread.currentThread().interrupt();
            }
        };
    }
}
