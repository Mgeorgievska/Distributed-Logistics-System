package mk.finki.shipments;

import java.util.List;

public class SequentialProcessor {

    public static ProcessingSummary process(List<String> shipments) {

        long start = System.currentTimeMillis();

        System.out.println("\n=== SEQUENTIAL PROCESSING ===");

        int validShipments = 0;
        int invalidShipments = 0;

        for (String shipment : shipments) {

            ProcessingResult result =
                    ShipmentProcessor.process(shipment);

            if (result.isValid()) {
                validShipments++;
            } else {
                invalidShipments++;
            }
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
                        "SEQUENTIAL",
                        shipments.size(),
                        validShipments,
                        invalidShipments,
                        totalTime,
                        throughput,
                        1
                );

        printSummary(summary);

        return summary;
    }

    private static void printSummary(
            ProcessingSummary summary) {

        System.out.println("\n----- SEQUENTIAL RESULTS -----");

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
                "Threads used: 1"
        );

        System.out.println(
                "=============================="
        );
    }
}