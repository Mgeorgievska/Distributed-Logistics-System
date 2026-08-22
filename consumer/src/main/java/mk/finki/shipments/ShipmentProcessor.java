package mk.finki.shipments;

public class ShipmentProcessor {

    public static void process(String shipmentJson) {

        long start = System.currentTimeMillis();

        // Simulate shipment processing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        System.out.println(
                "Processed shipment by thread: "
                        + Thread.currentThread().getName()
                        + " | Time: "
                        + (end - start)
                        + " ms"
        );
    }
}