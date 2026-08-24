package mk.finki.shipments;

import java.util.List;

public class ShipmentKafkaProducerTest {

    public static void main(String[] args) throws Exception {

        String bootstrapServers = "localhost:9092";

        /*
         * Test records with explicit routeCodes.
         *
         * routeCode is used later as the Kafka message key.
         */

        ShipmentRecord record1 = new ShipmentRecord(
                "02.01.2020",
                "MK-GR-001",
                "Mdr Transport Bogdanci",
                "igracki",
                "Sigal Diametamor Grcija",
                "",
                "Atlantik FM Skopje",
                "",
                "607990T9",
                "",
                600.0,
                0.0
        );

        ShipmentRecord record2 = new ShipmentRecord(
                "03.01.2020",
                "MK-BA-002",
                "Tehno transport Srebrenica Bih",
                "portokali",
                "Best fruit Grcija",
                "",
                "Merfruit Brcko Bih",
                "",
                "608357T4",
                "",
                0.0,
                0.0
        );

        ShipmentRecord record3 = new ShipmentRecord(
                "03.01.2020",
                "MK-RS-003",
                "Ino prevoz Nis",
                "aditivi",
                "Geohelas Grcija",
                "",
                "Vitafor balkan Belgrad",
                "",
                "608613T1",
                "",
                0.0,
                0.0
        );

        try (ShipmentKafkaProducer producer =
                     new ShipmentKafkaProducer(bootstrapServers)) {

            producer.sendAll(
                    List.of(
                            record1,
                            record2,
                            record3
                    )
            );
        }

        System.out.println(
                "Kafka test finished."
        );
    }
}