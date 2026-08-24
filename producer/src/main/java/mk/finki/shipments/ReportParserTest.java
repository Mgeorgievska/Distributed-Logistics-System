package mk.finki.shipments;

import java.io.File;
import java.util.List;

public class ReportParserTest {

    public static void main(String[] args) {

        String filePath =
                "data/reports/Izvestaj 2 0 2 0/"
                        + "01 Januari 2020/"
                        + "Izvestaj - 01.01.2020.xls";

        File file = new File(filePath);

        if (!file.exists()) {

            System.err.println(
                    "File does not exist:"
            );

            System.err.println(
                    file.getAbsolutePath()
            );

            return;
        }

        System.out.println(
                "================================="
        );

        System.out.println(
                "REPORT PARSER TEST"
        );

        System.out.println(
                "================================="
        );

        System.out.println(
                "File: " + file.getAbsolutePath()
        );

        List<ShipmentRecord> records =
                ReportParser.parse(file);

        System.out.println();
        System.out.println(
                "Total shipment records: "
                        + records.size()
        );

        System.out.println();
        System.out.println(
                "First shipment records:"
        );

        System.out.println(
                "---------------------------------"
        );

        int limit =
                Math.min(20, records.size());

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