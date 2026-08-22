package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelProcessor {

    public static void process(List<String> shipments) {

        long start = System.currentTimeMillis();

        System.out.println("\n=== PARALLEL PROCESSING ===");

        int numberOfThreads =
                Runtime.getRuntime().availableProcessors();

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        List<Future<ProcessingResult>> futures = new ArrayList<>();

        // Submit every shipment as a separate parallel task
        for (String shipment : shipments) {

            Future<ProcessingResult> future =
                    executor.submit(
                            () -> ShipmentProcessor.process(shipment)
                    );

            futures.add(future);
        }

        int validShipments = 0;
        int invalidShipments = 0;
        long totalProcessingTime = 0;

        // Collect results
        for (Future<ProcessingResult> future : futures) {

            try {

                ProcessingResult result = future.get();

                if (result.isValid()) {
                    validShipments++;
                } else {
                    invalidShipments++;
                }

                totalProcessingTime += result.getProcessingTime();

            } catch (Exception e) {

                System.err.println(
                        "Error getting processing result: "
                                + e.getMessage()
                );
            }
        }

        executor.shutdown();

        try {

            if (!executor.awaitTermination(
                    5,
                    TimeUnit.MINUTES
            )) {

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        long totalTime = end - start;

        double averageTime =
                shipments.isEmpty()
                        ? 0
                        : (double) totalProcessingTime
                        / shipments.size();

        double throughput =
                totalTime > 0
                        ? (double) shipments.size()
                        / (totalTime / 1000.0)
                        : 0;

        System.out.println("\n----- PARALLEL RESULTS -----");

        System.out.println(
                "Shipments: " + shipments.size()
        );

        System.out.println(
                "Valid shipments: " + validShipments
        );

        System.out.println(
                "Invalid shipments: " + invalidShipments
        );

        System.out.println(
                "Total processing time: "
                        + totalTime
                        + " ms"
        );

        System.out.printf(
                "Average processing time: %.2f ms%n",
                averageTime
        );

        System.out.printf(
                "Throughput: %.2f shipments/sec%n",
                throughput
        );

        System.out.println(
                "Threads used: " + numberOfThreads
        );

        System.out.println(
                "Thread pool size: " + numberOfThreads
        );

        System.out.println(
                "============================"
        );
    }
}