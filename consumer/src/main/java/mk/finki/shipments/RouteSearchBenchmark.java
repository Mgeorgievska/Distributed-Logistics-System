package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class RouteSearchBenchmark {

    private static final String TOPIC = "shipments.raw";

    public static void main(String[] args) {

        System.out.println();
        System.out.println("=================================");
        System.out.println("ROUTE SEARCH BENCHMARK");
        System.out.println("=================================");

        List<ShipmentRecord> shipments =
                loadShipmentsFromKafka();

        System.out.println();
        System.out.println(
                "Total shipments loaded: "
                        + shipments.size()
        );

        if (shipments.isEmpty()) {

            System.out.println(
                    "No shipments received from Kafka."
            );

            return;
        }

        /*
         * Find a real route from the dataset.
         */
        String routeCode =
                findFirstValidRoute(shipments);

        if (routeCode == null) {

            System.out.println(
                    "No valid routeCode found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "Searching route: "
                        + routeCode
        );

        /*
         * ===============================
         * SEQUENTIAL SEARCH
         * ===============================
         */

        long sequentialStart =
                System.currentTimeMillis();

        List<ShipmentRecord> sequentialResults =
                SequentialRouteSearch.search(
                        shipments,
                        routeCode
                );

        long sequentialTime =
                System.currentTimeMillis()
                        - sequentialStart;

        /*
         * ===============================
         * PARALLEL SEARCH
         * ===============================
         */

        long parallelStart =
                System.currentTimeMillis();

        List<ShipmentRecord> parallelResults =
                ParallelRouteSearch.search(
                        shipments,
                        routeCode
                );

        long parallelTime =
                System.currentTimeMillis()
                        - parallelStart;

        /*
         * ===============================
         * COMPARISON
         * ===============================
         */

        double speedup =
                parallelTime > 0
                        ? (double) sequentialTime
                        / parallelTime
                        : 0;

        System.out.println();
        System.out.println(
                "========== SEARCH COMPARISON =========="
        );

        System.out.println(
                "Route: "
                        + routeCode
        );

        System.out.println(
                "Total shipments: "
                        + shipments.size()
        );

        System.out.println(
                "Sequential matches: "
                        + sequentialResults.size()
        );

        System.out.println(
                "Parallel matches: "
                        + parallelResults.size()
        );

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

        System.out.println(
                "========================================"
        );
    }


    private static List<ShipmentRecord>
    loadShipmentsFromKafka() {

        Properties props =
                new Properties();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        /*
         * Different group so that we do not
         * interfere with the normal consumer.
         */
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "route-search-benchmark"
        );

        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer"
        );

        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer"
        );

        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<>(props);

        ObjectMapper objectMapper =
                new ObjectMapper();

        List<ShipmentRecord> shipments =
                new ArrayList<>();

        consumer.subscribe(
                Collections.singletonList(TOPIC)
        );

        System.out.println(
                "Reading shipments from Kafka..."
        );

        /*
         * Read Kafka records for a limited period.
         */
        long start =
                System.currentTimeMillis();

        try {

            while (System.currentTimeMillis() - start < 5000) {

                ConsumerRecords<String, String> records =
                        consumer.poll(
                                Duration.ofMillis(500)
                        );

                for (ConsumerRecord<String, String> record
                        : records) {

                    try {

                        ShipmentRecord shipment =
                                objectMapper.readValue(
                                        record.value(),
                                        ShipmentRecord.class
                                );

                        shipments.add(shipment);

                    } catch (Exception e) {

                        System.err.println(
                                "Error parsing shipment: "
                                        + e.getMessage()
                        );
                    }
                }
            }

        } finally {

            consumer.close();
        }

        return shipments;
    }


    private static String findFirstValidRoute(
            List<ShipmentRecord> shipments) {

        for (ShipmentRecord shipment : shipments) {

            if (shipment != null
                    && shipment.getRouteCode() != null
                    && !shipment.getRouteCode().isBlank()) {

                return shipment.getRouteCode();
            }
        }

        return null;
    }
}