package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelRouteSearch {

    public static List<ShipmentRecord> search(
            List<ShipmentRecord> shipments,
            String routeCode) {

        long start = System.currentTimeMillis();

        int numberOfThreads =
                Runtime.getRuntime().availableProcessors();

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        List<Callable<List<ShipmentRecord>>> tasks =
                new ArrayList<>();

        /*
         * Divide shipments into approximately equal chunks.
         */
        int chunkSize =
                (int) Math.ceil(
                        (double) shipments.size()
                                / numberOfThreads
                );

        for (int i = 0;
             i < shipments.size();
             i += chunkSize) {

            int fromIndex = i;

            int toIndex =
                    Math.min(
                            i + chunkSize,
                            shipments.size()
                    );

            tasks.add(() -> {

                List<ShipmentRecord> localResults =
                        new ArrayList<>();

                for (int j = fromIndex;
                     j < toIndex;
                     j++) {

                    ShipmentRecord shipment =
                            shipments.get(j);

                    if (shipment == null) {
                        continue;
                    }

                    if (routeCode.equalsIgnoreCase(
                            shipment.getRouteCode())) {

                        localResults.add(shipment);
                    }
                }

                return localResults;
            });
        }

        List<ShipmentRecord> results =
                new ArrayList<>();

        try {

            List<Future<List<ShipmentRecord>>> futures =
                    executor.invokeAll(tasks);

            for (Future<List<ShipmentRecord>> future
                    : futures) {

                results.addAll(future.get());
            }

        } catch (Exception e) {

            System.err.println(
                    "Error during parallel search: "
                            + e.getMessage()
            );

        } finally {

            executor.shutdown();
        }

        long totalTime =
                System.currentTimeMillis() - start;

        System.out.println();
        System.out.println("===== PARALLEL ROUTE SEARCH =====");
        System.out.println("Route: " + routeCode);
        System.out.println("Shipments searched: " + shipments.size());
        System.out.println("Matches found: " + results.size());
        System.out.println("Search time: " + totalTime + " ms");
        System.out.println("Threads used: " + numberOfThreads);
        System.out.println("=================================");

        return results;
    }
}