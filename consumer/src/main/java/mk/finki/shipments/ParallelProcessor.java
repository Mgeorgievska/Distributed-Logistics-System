package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelProcessor {

    public static ProcessingSummary process(
            List<String> shipments) {

        long start = System.currentTimeMillis();

        System.out.println("\n=== PARALLEL PROCESSING ===");

        int numberOfThreads =
                Runtime.getRuntime().availableProcessors();

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        numberOfThreads
                );

        List<Future<ProcessingResult>> futures =
                new ArrayList<>();

        for (String shipment : shipments) {

            Future<ProcessingResult> future =
                    executor.submit(
                            () -> ShipmentProcessor.process(shipment)
                    );

            futures.add(future);
        }

        int validShipments = 0;
        int invalidShipments = 0;

        for (Future<ProcessingResult> future : futures) {

            try {

                ProcessingResult result =
                        future.get();

                if (result.isValid()) {
                    validShipments++;
                } else {
                    invalidShipments++;
                }

            } catch (Exception e) {

                System.err.println(
                        "Error getting result: "
                                + e.getMessage()
                );

                invalidShipments++;
            }
        }

        executor.shutdown();

        try {

            if (!executor.awaitTermination(
                    5,
                    TimeUnit.MINUTES)) {

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long totalTime =
                System.currentTimeMillis() - start;

        double throughput =
                totalTime > 0
                        ? (double) shipments.size()
                        / (totalTime / 1000.0)
                        : 0;

        ProcessingSummary summary =
                new ProcessingSummary(
                        "PARALLEL",
                        shipments.size(),
                        validShipments,
                        invalidShipments,
                        totalTime,
                        throughput,
                        numberOfThreads
                );

        printSummary(summary);

        return summary;
    }

    private static void printSummary(
            ProcessingSummary summary) {

        System.out.println("\n----- PARALLEL RESULTS -----");

        System.out.println(
                "Shipments: "
                        + summary.getShipments()
        );

        System.out.println(
                "Valid shipments: "
                        + summary.getValidShipments()
        );

        System.out.println(
                "Invalid shipments: "
                        + summary.getInvalidShipments()
        );

        System.out.println(
                "Total processing time: "
                        + summary.getTotalTime()
                        + " ms"
        );

        System.out.printf(
                "Throughput: %.2f shipments/sec%n",
                summary.getThroughput()
        );

        System.out.println(
                "Threads used: "
                        + summary.getThreads()
        );

        System.out.println(
                "Thread pool size: "
                        + summary.getThreads()
        );

        System.out.println(
                "============================"
        );
    }
}