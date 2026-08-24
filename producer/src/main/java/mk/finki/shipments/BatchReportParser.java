package mk.finki.shipments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatchReportParser {

    public static List<ShipmentRecord> parseAll(File reportsDirectory) {

        List<ShipmentRecord> allRecords = new ArrayList<>();

        File[] monthDirectories = reportsDirectory.listFiles(File::isDirectory);

        if (monthDirectories == null) {
            System.err.println("No month directories found.");
            return allRecords;
        }

        for (File monthDirectory : monthDirectories) {

            File[] files = monthDirectory.listFiles(
                    (dir, name) ->
                            name.toLowerCase().endsWith(".xls")
                                    && name.toLowerCase().contains("izvestaj")
            );

            if (files == null) {
                continue;
            }

            for (File file : files) {

                List<ShipmentRecord> records =
                        ReportParser.parse(file);

                if (!records.isEmpty()) {

                    System.out.println(
                            "Parsed " + records.size()
                                    + " records from "
                                    + file.getName()
                    );

                    allRecords.addAll(records);
                }
            }
        }

        return allRecords;
    }
}