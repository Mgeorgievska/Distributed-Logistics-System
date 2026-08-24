package mk.finki.shipments;

import java.util.List;

public class AllReportsTest {

    public static void main(String[] args) {

        String reportsDirectory =
                "data/reports/Izvestaj 2 0 2 0";

        long start = System.currentTimeMillis();

        List<ShipmentRecord> records =
                ReportDirectoryLoader.loadReports(
                        reportsDirectory
                );

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("=================================");
        System.out.println("ALL REPORTS TEST");
        System.out.println("=================================");

        System.out.println(
                "Total records: "
                        + records.size()
        );

        System.out.println(
                "Loading time: "
                        + (end - start)
                        + " ms"
        );

        System.out.println();

        int limit =
                Math.min(10, records.size());

        System.out.println("First records:");

        for (int i = 0; i < limit; i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + records.get(i)
            );
        }

        System.out.println(
                "================================="
        );
    }
}