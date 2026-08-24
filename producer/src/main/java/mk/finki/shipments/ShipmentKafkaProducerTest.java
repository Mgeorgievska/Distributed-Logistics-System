package mk.finki.shipments;

import java.util.List;

public class ShipmentKafkaProducerTest {

    public static void main(String[] args) throws Exception {

        String reportsDirectory =
                "C:\\Users\\Marija\\OneDrive\\Desktop\\ShipmentDataProcessing\\producer\\data\\reports\\Izvestaj 2 0 2 0";

        System.out.println("=================================");
        System.out.println("LOADING SHIPMENTS");
        System.out.println("=================================");

        List<ShipmentRecord> shipments =
                ReportDirectoryLoader.loadReports(reportsDirectory);

        System.out.println();
        System.out.println(
                "Total shipments to send: "
                        + shipments.size()
        );

        if (shipments.isEmpty()) {
            System.out.println("No shipments found!");
            return;
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("STARTING KAFKA PRODUCER");
        System.out.println("=================================");

        try (ShipmentKafkaProducer producer =
                     new ShipmentKafkaProducer("localhost:9092")) {

            for (ShipmentRecord shipment : shipments) {

                producer.send(shipment);
            }
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("ALL SHIPMENTS SENT");
        System.out.println("=================================");
    }
}