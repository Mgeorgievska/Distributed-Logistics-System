package mk.finki.shipments;

import java.io.File;
import java.util.List;

public class ReportBatchParserTest {

    public static void main(String[] args) {

        String directoryPath =
                "data/reports/Izvestaj 2 0 2 0";

        File directory = new File(directoryPath);

        System.out.println("=================================");
        System.out.println("BATCH REPORT PARSER");
        System.out.println("=================================");

        System.out.println(
                "Directory: "
                        + directory.getAbsolutePath()
        );

        System.out.println();

        long start = System.currentTimeMillis();

        List<ShipmentRecord> records =
                ReportBatchParser.parseDirectory(directory);

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println("=================================");
        System.out.println("BATCH PARSING RESULT");
        System.out.println("=================================");

        System.out.println(
                "Total shipment records: "
                        + records.size()
        );

        System.out.println(
                "Processing time: "
                        + (end - start)
                        + " ms"
        );

        System.out.println();

        int limit = Math.min(20, records.size());

        System.out.println("First records:");

        for (int i = 0; i < limit; i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + records.get(i)
            );
        }

        System.out.println("=================================");
    }
}