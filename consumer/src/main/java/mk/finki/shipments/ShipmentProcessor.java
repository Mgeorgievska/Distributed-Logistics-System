package mk.finki.shipments;

public class ShipmentProcessor {

    public static ProcessingResult process(
            ShipmentRecord shipment) {

        long startTime =
                System.currentTimeMillis();

        try {

            /*
             * Validate real shipment data.
             */
            boolean valid =
                    validateShipment(shipment);

            /*
             * Calculate processing score.
             */
            double score =
                    calculateScore(shipment);

            /*
             * Simulate processing work.
             *
             * This is intentionally included so that
             * the difference between sequential and
             * parallel processing becomes measurable.
             */
            Thread.sleep(100);

            long processingTime =
                    System.currentTimeMillis()
                            - startTime;

            /*
             * Do NOT print every shipment here.
             *
             * The benchmark will print only the
             * final processing statistics.
             */

            /*
             * ShipmentRecord does not currently have
             * shipmentId, so routeCode is used as identifier.
             */
            return new ProcessingResult(
                    shipment.getRouteCode(),
                    valid,
                    score,
                    processingTime,
                    Thread.currentThread().getName()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error processing shipment: "
                            + e.getMessage()
            );

            return new ProcessingResult(
                    shipment != null
                            ? shipment.getRouteCode()
                            : "",
                    false,
                    0.0,
                    System.currentTimeMillis()
                            - startTime,
                    Thread.currentThread().getName()
            );
        }
    }

    private static boolean validateShipment(
            ShipmentRecord shipment) {

        if (shipment == null) {
            return false;
        }

        if (shipment.getRouteCode() == null
                || shipment.getRouteCode().isBlank()) {
            return false;
        }

        if (shipment.getCarrier() == null
                || shipment.getCarrier().isBlank()) {
            return false;
        }

        if (shipment.getExporter() == null
                || shipment.getExporter().isBlank()) {
            return false;
        }

        if (shipment.getImporter() == null
                || shipment.getImporter().isBlank()) {
            return false;
        }

        return true;
    }

    private static double calculateScore(
            ShipmentRecord shipment) {

        double score = 0.0;

        /*
         * Revenue contributes to the score.
         */
        score += shipment.getRevenueMKD();

        score +=
                shipment.getRevenueEUR() * 61.5;

        /*
         * Route code gives a small deterministic
         * contribution based on its length.
         */
        if (shipment.getRouteCode() != null) {
            score +=
                    shipment.getRouteCode().length() * 10;
        }

        return score;
    }
}