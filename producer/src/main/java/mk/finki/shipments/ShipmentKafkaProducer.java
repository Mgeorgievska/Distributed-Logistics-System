package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

public class ShipmentKafkaProducer implements AutoCloseable {

    private static final String TOPIC = "shipments.raw";

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;

    public ShipmentKafkaProducer(String bootstrapServers) {

        Properties properties = new Properties();

        properties.put(
                org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        properties.put(
                org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );

        properties.put(
                org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );

        properties.put(
                org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG,
                "all"
        );

        properties.put(
                org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
                "true"
        );

        this.producer = new KafkaProducer<>(properties);
        this.objectMapper = new ObjectMapper();
    }

    public RecordMetadata send(ShipmentRecord shipment) throws Exception {

        /*
         * routeCode is used as the Kafka message key.
         *
         * This allows all shipments belonging to the same
         * route to be consistently assigned to the same
         * Kafka partition.
         */
        String key = shipment.getRouteCode();

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "Shipment routeCode cannot be empty."
            );
        }

        String json =
                objectMapper.writeValueAsString(shipment);

        ProducerRecord<String, String> record =
                new ProducerRecord<>(
                        TOPIC,
                        key,
                        json
                );

        Future<RecordMetadata> future =
                producer.send(record);

        RecordMetadata metadata =
                future.get();

        System.out.println(
                "Sent shipment | key=" + key +
                        " | partition=" + metadata.partition() +
                        " | offset=" + metadata.offset()
        );

        return metadata;
    }

    public void sendAll(
            List<ShipmentRecord> shipments) throws Exception {

        for (ShipmentRecord shipment : shipments) {
            send(shipment);
        }

        producer.flush();
    }

    @Override
    public void close() {
        producer.close();
    }
}