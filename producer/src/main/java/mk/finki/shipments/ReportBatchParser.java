package mk.finki.shipments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportBatchParser {

    public static List<ShipmentRecord> parseDirectory(File directory) {
        

        List<ShipmentRecord> allRecords = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println(
                    "Directory does not exist: "
                            + directory.getAbsolutePath()
            );
            return allRecords;
        }

        File[] monthDirectories = directory.listFiles(File::isDirectory);

        if (monthDirectories == null) {
            return allRecords;
        }

        for (File monthDirectory : monthDirectories) {

            File[] files = monthDirectory.listFiles(
                    file -> file.isFile()
                            && file.getName().toLowerCase().endsWith(".xls")
            );

            if (files == null) {
                continue;
            }

            for (File file : files) {

                System.out.println(
                        "Processing: "
                                + monthDirectory.getName()
                                + " / "
                                + file.getName()
                );

                List<ShipmentRecord> records =
                        ReportParser.parse(file);

                allRecords.addAll(records);

                System.out.println(
                        "Records extracted: "
                                + records.size()
                );

                System.out.println("---------------------------------");
            }
        }

        return allRecords;
    }
}