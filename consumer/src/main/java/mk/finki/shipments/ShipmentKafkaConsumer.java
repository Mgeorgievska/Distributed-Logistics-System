package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ShipmentKafkaConsumer implements AutoCloseable {

    private static final String TOPIC = "shipments.raw";

    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;

    public ShipmentKafkaConsumer(
            String bootstrapServers,
            String groupId) {

        Properties properties = new Properties();

        properties.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        properties.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId
        );

        properties.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName()
        );

        properties.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName()
        );

        properties.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        properties.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                "false"
        );

        this.consumer = new KafkaConsumer<>(properties);
        this.objectMapper = new ObjectMapper();
    }

    /*
     * Reads shipments from Kafka and returns them
     * as a List<ShipmentRecord>.
     *
     * This method is used by the benchmark.
     */
    public List<ShipmentRecord> consumeRecords(int maxRecords)
            throws Exception {

        consumer.subscribe(
                Collections.singletonList(TOPIC)
        );

        List<ShipmentRecord> shipments =
                new ArrayList<>();

        while (shipments.size() < maxRecords) {

            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(1000));

            if (records.isEmpty()) {
                continue;
            }

            for (ConsumerRecord<String, String> record : records) {

                ShipmentRecord shipment =
                        objectMapper.readValue(
                                record.value(),
                                ShipmentRecord.class
                        );

                System.out.println(
                        "Received shipment | "
                                + "key=" + record.key()
                                + " | partition=" + record.partition()
                                + " | offset=" + record.offset()
                );

                System.out.println(shipment);

                shipments.add(shipment);

                if (shipments.size() >= maxRecords) {
                    break;
                }
            }

            consumer.commitSync();
        }

        System.out.println(
                "Consumer finished. Records received: "
                        + shipments.size()
        );

        return shipments;
    }

    /*
     * Older test method.
     * Keeps compatibility with ShipmentKafkaConsumerTest.
     */
    public void consume(int maxRecords)
            throws Exception {

        consumeRecords(maxRecords);
    }

    @Override
    public void close() {
        consumer.close();
    }
}