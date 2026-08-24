package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;

public class SequentialRouteSearch {

    public static List<ShipmentRecord> search(
            List<ShipmentRecord> shipments,
            String routeCode) {

        long start = System.currentTimeMillis();

        List<ShipmentRecord> results = new ArrayList<>();

        for (ShipmentRecord shipment : shipments) {

            if (shipment == null) {
                continue;
            }

            if (routeCode.equalsIgnoreCase(
                    shipment.getRouteCode())) {

                results.add(shipment);
            }
        }

        long totalTime =
                System.currentTimeMillis() - start;

        System.out.println();
        System.out.println("===== SEQUENTIAL ROUTE SEARCH =====");
        System.out.println("Route: " + routeCode);
        System.out.println("Shipments searched: " + shipments.size());
        System.out.println("Matches found: " + results.size());
        System.out.println("Search time: " + totalTime + " ms");
        System.out.println("===================================");

        return results;
    }
}