package mk.finki.shipments;

import java.io.File;
import java.util.List;

public class BatchReportParserTest {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("BATCH REPORT PARSER TEST");
        System.out.println("=================================");

        File reportsDirectory =
                new File("data/reports/Izvestaj 2 0 2 0");

        if (!reportsDirectory.exists()) {

            System.err.println(
                    "Reports directory does not exist:"
            );

            System.err.println(
                    reportsDirectory.getAbsolutePath()
            );

            return;
        }

        long start = System.currentTimeMillis();

        List<ShipmentRecord> records =
                BatchReportParser.parseAll(reportsDirectory);

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("=================================");
        System.out.println("BATCH PARSING RESULT");
        System.out.println("=================================");

        System.out.println(
                "Total records: " + records.size()
        );

        System.out.println(
                "Processing time: "
                        + (end - start)
                        + " ms"
        );

        System.out.println();
        System.out.println("First records:");

        int limit = Math.min(20, records.size());

        for (int i = 0; i < limit; i++) {

            System.out.println(
                    (i + 1) + ". "
                            + records.get(i)
            );
        }

        System.out.println("=================================");
    }
}