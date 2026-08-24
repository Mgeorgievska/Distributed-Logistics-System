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

public class ShipmentConsumer {

    private static final String TOPIC = "shipments.raw";

    public static void main(String[] args) {

        Properties props = new Properties();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "shipment-consumer-group"
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

        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                "false"
        );

        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<>(props);

        ObjectMapper objectMapper =
                new ObjectMapper();

        consumer.subscribe(
                Collections.singletonList(TOPIC)
        );

        System.out.println("=================================");
        System.out.println("SHIPMENT KAFKA CONSUMER");
        System.out.println("=================================");
        System.out.println("Waiting for shipments...");

        try {

            while (true) {

                ConsumerRecords<String, String> records =
                        consumer.poll(
                                Duration.ofMillis(1000)
                        );

                if (records.isEmpty()) {
                    continue;
                }

                List<ShipmentRecord> shipments =
                        new ArrayList<>();

                /*
                 * Convert Kafka JSON messages
                 * into ShipmentRecord objects.
                 */
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

                System.out.println();
                System.out.println(
                        "Received batch: "
                                + shipments.size()
                                + " shipments"
                );

                if (shipments.isEmpty()) {
                    continue;
                }

                // ===============================
                // SEQUENTIAL PROCESSING
                // ===============================

                ProcessingSummary sequential =
                        SequentialProcessor.process(
                                shipments
                        );

                // ===============================
                // PARALLEL PROCESSING
                // ===============================

                ProcessingSummary parallel =
                        ParallelProcessor.process(
                                shipments
                        );

                // ===============================
                // COMPARISON
                // ===============================

                double speedup =
                        parallel.getTotalTime() > 0
                                ? (double)
                                sequential.getTotalTime()
                                / parallel.getTotalTime()
                                : 0;

                System.out.println();
                System.out.println(
                        "========== COMPARISON =========="
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

                System.out.println(
                        "================================"
                );

                /*
                 * Commit only after successful processing.
                 */
                consumer.commitSync();
            }

        } finally {

            consumer.close();

            System.out.println(
                    "Shipment Consumer stopped."
            );
        }
    }
}