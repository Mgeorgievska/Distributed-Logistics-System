package mk.finki.shipments;

import java.util.List;

public class KafkaProcessingBenchmark {

    public static void main(String[] args)
            throws Exception {

        String bootstrapServers =
                "localhost:9092";

        /*
         * Use a new group ID for every benchmark run
         * so Kafka starts from the beginning.
         */
        String groupId =
                "benchmark-"
                        + System.currentTimeMillis();

        /*
         * CHANGE THIS NUMBER
         * depending on how many Kafka records
         * you want to process.
         */
        int numberOfRecords = 3;

        System.out.println(
                "======================================"
        );

        System.out.println(
                "KAFKA SHIPMENT PROCESSING BENCHMARK"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Requested records: "
                        + numberOfRecords
        );

        /*
         * =====================================
         * STEP 1: READ FROM KAFKA
         * =====================================
         */

        List<ShipmentRecord> shipments;

        try (ShipmentKafkaConsumer consumer =
                     new ShipmentKafkaConsumer(
                             bootstrapServers,
                             groupId)) {

            shipments =
                    consumer.consumeRecords(
                            numberOfRecords
                    );
        }

        if (shipments.isEmpty()) {

            System.out.println(
                    "No shipments received from Kafka."
            );

            return;
        }

        System.out.println();

        System.out.println(
                "Shipments loaded from Kafka: "
                        + shipments.size()
        );

        /*
         * =====================================
         * STEP 2: SEQUENTIAL
         * =====================================
         */

        ProcessingSummary sequential =
                SequentialProcessor.process(
                        shipments
                );

        /*
         * =====================================
         * STEP 3: PARALLEL
         * =====================================
         */

        ProcessingSummary parallel =
                ParallelProcessor.process(
                        shipments
                );

        /*
         * =====================================
         * STEP 4: SPEEDUP
         * =====================================
         */

        double speedup =
                parallel.getTotalTime() > 0
                        ? (double)
                        sequential.getTotalTime()
                        / parallel.getTotalTime()
                        : 0;

        /*
         * =====================================
         * STEP 5: THREADS
         * =====================================
         */

        int threads =
                Runtime.getRuntime()
                        .availableProcessors();

        /*
         * =====================================
         * STEP 6: EFFICIENCY
         * =====================================
         */

        double efficiency =
                threads > 0
                        ? (speedup / threads) * 100
                        : 0;

        /*
         * =====================================
         * STEP 7: IMPROVEMENT
         * =====================================
         */

        double improvement =
                sequential.getTotalTime() > 0
                        ? (
                        1.0
                                -
                                (
                                        (double)
                                                parallel.getTotalTime()
                                                /
                                                sequential.getTotalTime()
                                )
                ) * 100
                        : 0;

        /*
         * =====================================
         * FINAL RESULTS
         * =====================================
         */

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "FINAL COMPARISON"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Shipments: "
                        + shipments.size()
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
                "Threads: "
                        + threads
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "BENCHMARK FINISHED"
        );

        System.out.println(
                "======================================"
        );
    }
}