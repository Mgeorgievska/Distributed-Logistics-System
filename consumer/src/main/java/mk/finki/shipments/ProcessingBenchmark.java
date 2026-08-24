package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;

public class ProcessingBenchmark {

    public static void main(String[] args) {

        int[] testSizes = {
                10,
                50,
                100,
                200,
                500,
                1000
        };

        System.out.println("=================================");
        System.out.println("SHIPMENT PROCESSING BENCHMARK");
        System.out.println("=================================");

        int threads =
                Runtime.getRuntime().availableProcessors();

        System.out.println(
                "Available processors: " + threads
        );

        for (int size : testSizes) {

            System.out.println();
            System.out.println("=================================");
            System.out.println(
                    "TEST WITH " + size + " SHIPMENTS"
            );
            System.out.println("=================================");

            List<String> shipments =
                    generateShipments(size);

            /*
             * Sequential processing
             */
            ProcessingSummary sequential =
                    SequentialProcessor.process(
                            shipments
                    );

            /*
             * Parallel processing
             */
            ProcessingSummary parallel =
                    ParallelProcessor.process(
                            shipments
                    );

            /*
             * Speedup
             */
            double speedup =
                    parallel.getTotalTime() > 0
                            ? (double) sequential.getTotalTime()
                            / parallel.getTotalTime()
                            : 0;

            /*
             * Parallel efficiency
             */
            double efficiency =
                    threads > 0
                            ? speedup / threads * 100
                            : 0;

            /*
             * Improvement
             */
            double improvement =
                    sequential.getTotalTime() > 0
                            ? (
                            1.0
                                    -
                                    (double) parallel.getTotalTime()
                                    / sequential.getTotalTime()
                    ) * 100
                            : 0;

            System.out.println();
            System.out.println("----- COMPARISON -----");

            System.out.println(
                    "Shipments: " + size
            );

            System.out.println(
                    "Sequential time: "
                            + sequential.getTotalTime()
                            + " ms"
            );

            System.out.println(
                    "Parallel time: "
                            + parallel.getTotalTime()
                            + " ms"
            );

            System.out.printf(
                    "Speedup: %.2fx%n",
                    speedup
            );

            System.out.printf(
                    "Parallel efficiency: %.2f%%%n",
                    efficiency
            );

            System.out.printf(
                    "Time improvement: %.2f%%%n",
                    improvement
            );

            System.out.printf(
                    "Sequential throughput: %.2f shipments/sec%n",
                    sequential.getThroughput()
            );

            System.out.printf(
                    "Parallel throughput: %.2f shipments/sec%n",
                    parallel.getThroughput()
            );

            System.out.println(
                    "Threads: " + threads
            );

            System.out.println(
                    "======================"
            );
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("BENCHMARK FINISHED");
        System.out.println("=================================");
    }

    private static List<String> generateShipments(
            int number) {

        List<String> shipments =
                new ArrayList<>();

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