package mk.finki.shipments;

import java.util.ArrayList;
import java.util.List;

public class ProcessingBenchmark {

    public static void main(String[] args) {

        List<String> shipments = new ArrayList<>();

        // Create test shipments
        for (int i = 1; i <= 20; i++) {
            shipments.add(
                    "{\"shipmentId\":" + i +
                    ",\"routeCode\":\"TEST-" + i +
                    "\",\"origin\":\"Skopje\"" +
                    ",\"destination\":\"Belgrade\"" +
                    ",\"weight\":500" +
                    ",\"status\":\"CREATED\"}"
            );
        }

        System.out.println("=================================");
        System.out.println("SHIPMENT PROCESSING BENCHMARK");
        System.out.println("Number of shipments: " + shipments.size());
        System.out.println("=================================");

        // Sequential processing
        SequentialProcessor.process(shipments);

        // Parallel processing
        ParallelProcessor.process(shipments);

        System.out.println("\n=================================");
        System.out.println("BENCHMARK FINISHED");
        System.out.println("=================================");
    }
}