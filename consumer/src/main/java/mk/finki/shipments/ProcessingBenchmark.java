package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ProcessingBenchmark {

    private static final ObjectMapper objectMapper =
            new ObjectMapper();

    public static void main(String[] args) throws Exception {

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

            List<ShipmentRecord> shipments =
                    generateShipments(size);

            // Sequential
            ProcessingSummary sequential =
                    SequentialProcessor.process(
                            shipments
                    );

            // Parallel
            ProcessingSummary parallel =
                    ParallelProcessor.process(
                            shipments
                    );

            // Speedup
            double speedup =
                    parallel.getTotalTime() > 0
                            ? (double) sequential.getTotalTime()
                            / parallel.getTotalTime()
                            : 0;

            // Efficiency
            double efficiency =
                    threads > 0
                            ? speedup / threads * 100
                            : 0;

            // Improvement
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

    private static List<ShipmentRecord> generateShipments(
            int number) throws Exception {

        List<ShipmentRecord> shipments =
                new ArrayList<>();

        for (int i = 1; i <= number; i++) {

            String json =
                    "{"
                            + "\"date\":\"24.08.2026\","
                            + "\"routeCode\":\"TEST-" + i + "\","
                            + "\"carrier\":\"Test Carrier\","
                            + "\"goods\":\"Test Goods\","
                            + "\"exporter\":\"Test Exporter\","
                            + "\"exporterCountry\":\"MK\","
                            + "\"importer\":\"Test Importer\","
                            + "\"importerCountry\":\"RS\","
                            + "\"declarationType\":\"TEST\","
                            + "\"declarationNumber\":\"TEST-" + i + "\","
                            + "\"revenueMKD\":500.0,"
                            + "\"revenueEUR\":8.0"
                            + "}";

            ShipmentRecord shipment =
                    objectMapper.readValue(
                            json,
                            ShipmentRecord.class
                    );

            shipments.add(shipment);
        }

        return shipments;
    }
}