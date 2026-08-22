package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ShipmentProcessor {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void process(String shipmentJson) {

        long start = System.currentTimeMillis();

        try {
            // Convert JSON to Shipment object
            Shipment shipment =
                    mapper.readValue(shipmentJson, Shipment.class);

            // Basic shipment validation
            boolean valid = validateShipment(shipment);

            // Calculate a processing score
            double processingScore =
                    calculateProcessingScore(shipment);

            long end = System.currentTimeMillis();

            System.out.println(
                    "Processed shipment:"
                            + " ID=" + shipment.getShipmentId()
                            + " | Route=" + shipment.getRouteCode()
                            + " | " + shipment.getOrigin()
                            + " -> " + shipment.getDestination()
                            + " | Weight=" + shipment.getWeight()
                            + " kg"
                            + " | Status=" + shipment.getStatus()
                            + " | Valid=" + valid
                            + " | Score=" + processingScore
                            + " | Thread=" + Thread.currentThread().getName()
                            + " | Time=" + (end - start) + " ms"
            );

        } catch (Exception e) {

            System.err.println(
                    "Error processing shipment: "
                            + shipmentJson
            );

            e.printStackTrace();
        }
    }

    private static boolean validateShipment(Shipment shipment) {

        return shipment.getShipmentId() > 0
                && shipment.getRouteCode() != null
                && !shipment.getRouteCode().isBlank()
                && shipment.getOrigin() != null
                && !shipment.getOrigin().isBlank()
                && shipment.getDestination() != null
                && !shipment.getDestination().isBlank()
                && shipment.getWeight() > 0
                && shipment.getStatus() != null;
    }

    private static double calculateProcessingScore(Shipment shipment) {

        double score = shipment.getWeight();

        if ("DELIVERED".equalsIgnoreCase(shipment.getStatus())) {
            score *= 0.8;
        } else if ("IN_TRANSIT".equalsIgnoreCase(shipment.getStatus())) {
            score *= 1.1;
        } else if ("CREATED".equalsIgnoreCase(shipment.getStatus())) {
            score *= 1.0;
        }

        return Math.round(score * 100.0) / 100.0;
    }
}