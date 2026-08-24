package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShipmentProducer {

    public static void main(String[] args) {

        String topic = "shipments.raw";

        Properties props = new Properties();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        ObjectMapper mapper = new ObjectMapper();

        List<Shipment> shipments = new ArrayList<>();

        shipments.add(new Shipment(
                1001,
                "MK-SK-001",
                "Skopje",
                "Belgrade",
                520.0,
                "CREATED"
        ));

        shipments.add(new Shipment(
                1002,
                "MK-GR-002",
                "Skopje",
                "Thessaloniki",
                340.0,
                "CREATED"
        ));

        shipments.add(new Shipment(
                1003,
                "MK-RS-003",
                "Skopje",
                "Nis",
                710.0,
                "IN_TRANSIT"
        ));

        shipments.add(new Shipment(
                1004,
                "DE-MK-004",
                "Berlin",
                "Skopje",
                1200.0,
                "CREATED"
        ));

        shipments.add(new Shipment(
                1005,
                "MK-HR-005",
                "Skopje",
                "Zagreb",
                450.0,
                "IN_TRANSIT"
        ));

        shipments.add(new Shipment(
                1006,
                "MK-BG-006",
                "Skopje",
                "Sofia",
                680.0,
                "CREATED"
        ));

        shipments.add(new Shipment(
                1007,
                "MK-AL-007",
                "Skopje",
                "Tirana",
                390.0,
                "DELIVERED"
        ));

        shipments.add(new Shipment(
                1008,
                "IT-MK-008",
                "Milan",
                "Skopje",
                950.0,
                "IN_TRANSIT"
        ));

        shipments.add(new Shipment(
                1009,
                "MK-AT-009",
                "Skopje",
                "Vienna",
                1100.0,
                "CREATED"
        ));

        shipments.add(new Shipment(
                1010,
                "MK-HU-010",
                "Skopje",
                "Budapest",
                870.0,
                "CREATED"
        ));

        try (KafkaProducer<String, String> producer =
                     new KafkaProducer<>(props)) {

            for (Shipment shipment : shipments) {

                String json =
                        mapper.writeValueAsString(shipment);

                ProducerRecord<String, String> record =
                        new ProducerRecord<>(
                                topic,
                                shipment.getRouteCode(),
                                json
                        );

                producer.send(record);

        
            }

            producer.flush();

            System.out.println(
                    "All shipments sent successfully."
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}