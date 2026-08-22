package mk.finki.shipments;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Properties;

public class ShipmentConsumer {

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

        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<>(props);

        consumer.subscribe(
                Collections.singletonList("shipments.raw")
        );

        System.out.println("=================================");
        System.out.println("SHIPMENT KAFKA CONSUMER");
        System.out.println("=================================");
        System.out.println("Waiting for shipments...");

        try {

            while (true) {

                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                if (records.isEmpty()) {
                    continue;
                }

                List<String> shipments = new ArrayList<>();

                for (ConsumerRecord<String, String> record : records) {

                    System.out.println(
                            "Received shipment from Kafka:"
                    );

                    System.out.println(
                            "Partition: " + record.partition()
                    );

                    System.out.println(
                            "Offset: " + record.offset()
                    );

                    System.out.println(
                            "Key: " + record.key()
                    );

                    System.out.println(
                            "---------------------------------"
                    );

                    shipments.add(record.value());
                }

                System.out.println(
                        "Shipments received in this batch: "
                                + shipments.size()
                );

                // Process shipments sequentially
                SequentialProcessor.process(shipments);

                // Process all received shipments in parallel
                ParallelProcessor.process(shipments);

                System.out.println(
                        "================================="
                );
            }

        } finally {

            consumer.close();

            System.out.println(
                    "Shipment Consumer stopped."
            );
        }
    }
}