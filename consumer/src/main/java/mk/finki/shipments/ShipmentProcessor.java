package mk.finki.shipments;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ShipmentProcessor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProcessingResult process(String json) {

        long startTime = System.currentTimeMillis();

        try {

            // Convert JSON string to Shipment object
            Shipment shipment = objectMapper.readValue(json, Shipment.class);

            // Validate shipment
            boolean valid = validateShipment(shipment);

            // Calculate shipment score
            double score = calculateScore(shipment);

            // Simulate processing work
            Thread.sleep(100);

            long processingTime =
                    System.currentTimeMillis() - startTime;

            System.out.println(
                    "Processed shipment: "
                            + "ID=" + shipment.getShipmentId()
                            + " | Route=" + shipment.getRouteCode()
                            + " | " + shipment.getOrigin()
                            + " -> " + shipment.getDestination()
                            + " | Weight=" + shipment.getWeight()
                            + " kg"
                            + " | Status=" + shipment.getStatus()
                            + " | Valid=" + valid
                            + " | Score=" + score
                            + " | Thread=" + Thread.currentThread().getName()
                            + " | Time=" + processingTime + " ms"
            );

            return new ProcessingResult(
                    shipment.getShipmentId(),
                    valid,
                    score,
                    processingTime,
                    Thread.currentThread().getName()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error processing shipment: "
                            + e.getMessage()
            );

            return new ProcessingResult(
                    -1,
                    false,
                    0.0,
                    System.currentTimeMillis() - startTime,
                    Thread.currentThread().getName()
            );
        }
    }

    private static boolean validateShipment(Shipment shipment) {

        if (shipment == null) {
            return false;
        }

        if (shipment.getShipmentId() <= 0) {
            return false;
        }

        if (shipment.getRouteCode() == null
                || shipment.getRouteCode().isBlank()) {
            return false;
        }

        if (shipment.getOrigin() == null
                || shipment.getOrigin().isBlank()) {
            return false;
        }

        if (shipment.getDestination() == null
                || shipment.getDestination().isBlank()) {
            return false;
        }

        if (shipment.getWeight() <= 0) {
            return false;
        }

        if (shipment.getStatus() == null
                || shipment.getStatus().isBlank()) {
            return false;
        }

        return true;
    }

    private static double calculateScore(Shipment shipment) {

        double score = shipment.getWeight();

        switch (shipment.getStatus().toUpperCase()) {

            case "DELIVERED":
                score *= 0.8;
                break;

            case "IN_TRANSIT":
                score *= 1.1;
                break;

            case "CREATED":
                score *= 1.0;
                break;

            default:
                score *= 0.5;
                break;
        }

        return score;
    }
}