package mk.finki.shipments;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ShipmentConsumer {

    public static void main(String[] args) {

        Properties props = new Properties();
        ObjectMapper mapper = new ObjectMapper();

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

        System.out.println("Shipment Consumer started...");
        System.out.println("Waiting for shipments...");

        try {

            while (true) {

                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {

    System.out.println("Received shipment:");
    System.out.println("Partition: " + record.partition());
    System.out.println("Offset: " + record.offset());
    System.out.println("Key: " + record.key());
    System.out.println("Value: " + record.value());

    try {
        Shipment shipment = mapper.readValue(
                record.value(),
                Shipment.class
        );

        System.out.println("Parsed shipment:");
        System.out.println("ID: " + shipment.getShipmentId());
        System.out.println("Route: " + shipment.getRouteCode());
        System.out.println("Origin: " + shipment.getOrigin());
        System.out.println("Destination: " + shipment.getDestination());
        System.out.println("Weight: " + shipment.getWeight());
        System.out.println("Status: " + shipment.getStatus());

    } catch (Exception e) {
        System.err.println("Failed to deserialize shipment:");
        e.printStackTrace();
    }

    System.out.println("--------------------------------");
}
            }

        } finally {

            consumer.close();
        }
    }
}