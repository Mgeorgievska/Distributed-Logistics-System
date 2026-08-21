package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class ShipmentProducer {

    private static final String TOPIC = "shipments.raw";

    public static void main(String[] args) throws Exception {

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

        KafkaProducer<String, String> producer =
                new KafkaProducer<>(props);

        ObjectMapper mapper = new ObjectMapper();

        String filePath = "data/shipments.csv";

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            // Skip header
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                int shipmentId = Integer.parseInt(data[0]);
                String routeCode = data[1];
                String origin = data[2];
                String destination = data[3];
                double weight = Double.parseDouble(data[4]);
                String status = data[5];

                Shipment shipment = new Shipment(
                        shipmentId,
                        routeCode,
                        origin,
                        destination,
                        weight,
                        status
                );

                String json = mapper.writeValueAsString(shipment);

                ProducerRecord<String, String> record =
                        new ProducerRecord<>(
                                TOPIC,
                                shipment.getRouteCode(),
                                json
                        );

                producer.send(record);

                System.out.println(
                        "Sent shipment: " + shipment.getShipmentId()
                );
            }
        }

        producer.flush();
        producer.close();

        System.out.println("All shipments sent successfully.");
    }
}