package mk.finki.shipments;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelProcessor {

    public static void process(List<String> shipments) {

        long start = System.currentTimeMillis();

        System.out.println("\n=== PARALLEL PROCESSING ===");

        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        for (String shipment : shipments) {

            executor.submit(() ->
                    ShipmentProcessor.process(shipment)
            );
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        System.out.println(
                "Parallel processing finished in: "
                        + (end - start)
                        + " ms"
        );

        System.out.println(
                "Threads used: " + numberOfThreads
        );
    }
}