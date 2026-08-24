package mk.finki.shipments;

import java.io.File;
import java.util.List;

public class ReportParserTest {

    public static void main(String[] args) {

        String filePath =
                "data/reports/Izvestaj 2 0 2 0/" +
                "01 Januari 2020/" +
                "Izvestaj - 01.01.2020.xls";

        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist:");
            System.err.println(file.getAbsolutePath());
            return;
        }

        List<ShipmentRecord> records =
                ReportParser.parse(file);

        System.out.println();
        System.out.println("=================================");
        System.out.println("PARSED SHIPMENT RECORDS");
        System.out.println("=================================");

        System.out.println("File: " + file.getName());
        System.out.println("Records: " + records.size());

        System.out.println();

        int limit = Math.min(20, records.size());

        for (int i = 0; i < limit; i++) {
            System.out.println(
                    (i + 1) + ". " + records.get(i)
            );
        }

        System.out.println("=================================");
    }
}