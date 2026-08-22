package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;

public class ProcessingBenchmark {

    public static void main(String[] args) {

        int[] testSizes = {10, 20, 50, 100, 200};

        System.out.println("=================================");
        System.out.println("SHIPMENT PROCESSING BENCHMARK");
        System.out.println("=================================");

        for (int size : testSizes) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("TEST WITH " + size + " SHIPMENTS");
            System.out.println("=================================");

            List<String> shipments = generateShipments(size);

            // Sequential
            long sequentialStart = System.currentTimeMillis();

            SequentialProcessor.process(shipments);

            long sequentialTime =
                    System.currentTimeMillis() - sequentialStart;

            // Parallel
            long parallelStart = System.currentTimeMillis();

            ParallelProcessor.process(shipments);

            long parallelTime =
                    System.currentTimeMillis() - parallelStart;

            // Metrics
            double speedup =
                    (double) sequentialTime / parallelTime;

            int threads =
                    Runtime.getRuntime().availableProcessors();

            double efficiency =
                    (speedup / threads) * 100;

            System.out.println();
            System.out.println("----- RESULTS -----");
            System.out.println("Shipments: " + size);
            System.out.println(
                    "Sequential time: "
                            + sequentialTime
                            + " ms"
            );

            System.out.println(
                    "Parallel time: "
                            + parallelTime
                            + " ms"
            );

            System.out.printf(
                    "Speedup: %.2fx%n",
                    speedup
            );

            System.out.printf(
                    "Efficiency: %.2f%%%n",
                    efficiency
            );

            System.out.println(
                    "Threads: " + threads
            );
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("BENCHMARK FINISHED");
        System.out.println("=================================");
    }

    private static List<String> generateShipments(int number) {

        List<String> shipments = new ArrayList<>();

        for (int i = 1; i <= number; i++) {

            String shipment =
                    "{"
                            + "\"shipmentId\":" + i + ","
                            + "\"routeCode\":\"TEST-" + i + "\","
                            + "\"origin\":\"Skopje\","
                            + "\"destination\":\"Belgrade\","
                            + "\"weight\":500.0,"
                            + "\"status\":\"CREATED\""
                            + "}";

            shipments.add(shipment);
        }

        return shipments;
    }
}