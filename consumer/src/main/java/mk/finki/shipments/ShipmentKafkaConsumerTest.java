package mk.finki.shipments;

import java.util.List;

public class ShipmentKafkaConsumerTest {

    public static void main(String[] args)
            throws Exception {

        String bootstrapServers =
                "localhost:9092";

        String groupId =
                "shipment-processing-test";

        int numberOfRecords = 3;

        try (ShipmentKafkaConsumer consumer =
                     new ShipmentKafkaConsumer(
                             bootstrapServers,
                             groupId)) {

            List<ShipmentRecord> shipments =
                    consumer.consumeRecords(
                            numberOfRecords
                    );

            System.out.println();
            System.out.println(
                    "Total shipments received: "
                            + shipments.size()
            );
        }
    }
}