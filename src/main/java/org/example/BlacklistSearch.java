package org.example;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlacklistSearch {

    public static final int BLACK_LIST_ALARM_COUNT = 5; // Límite de ocurrencias para considerar un host no confiable
    private final List<Integer> blacklistServers;
    private final ExecutorService executor;
    private final AtomicInteger occurrences;

    public BlacklistSearch(List<Integer> blacklistServers, int numThreads) {
        this.blacklistServers = blacklistServers;
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.occurrences = new AtomicInteger(0);
    }

    public int search(String ipAddress) {
        int chunkSize = (int) Math.ceil((double) blacklistServers.size() / Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < blacklistServers.size(); i += chunkSize) {
            int start = i;
            int end = Math.min(i + chunkSize, blacklistServers.size());

            Future<Integer> future = executor.submit(() -> {
                int localCount = 0;
                for (int j = start; j < end && occurrences.get() < BLACK_LIST_ALARM_COUNT; j++) {
                    if (checkBlacklist(ipAddress, blacklistServers.get(j))) {
                        localCount++;
                        if (occurrences.incrementAndGet() >= BLACK_LIST_ALARM_COUNT) {
                            return localCount;
                        }
                    }
                }
                return localCount;
            });

            futures.add(future);
        }

        int totalOccurrences = 0;
        for (Future<Integer> future : futures) {
            try {
                totalOccurrences += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdownNow(); // Detener todos los hilos restantes
        return totalOccurrences;
    }

    private boolean checkBlacklist(String ipAddress, int serverId) {
        // Simula la verificación en la lista negra (devuelve true aleatoriamente)
        return Math.random() > 0.8;
    }
}
