package mk.finki.shipments;

import java.util.List;

public class SequentialProcessor {

    public static void process(List<String> shipments) {

        long start = System.currentTimeMillis();

        System.out.println("\n=== SEQUENTIAL PROCESSING ===");

        for (String shipment : shipments) {
            ShipmentProcessor.process(shipment);
        }

        long end = System.currentTimeMillis();

        System.out.println(
                "Sequential processing finished in: "
                        + (end - start)
                        + " ms"
        );
    }
}