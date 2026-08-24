package mk.finki.shipments;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReportDirectoryLoader {

    public static List<ShipmentRecord> loadReports(String directoryPath) {

        List<ShipmentRecord> allRecords = new ArrayList<>();

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            System.err.println(
                    "Reports directory does not exist: "
                            + directory.getAbsolutePath()
            );
            return allRecords;
        }

        if (!directory.isDirectory()) {
            System.err.println(
                    "Path is not a directory: "
                            + directory.getAbsolutePath()
            );
            return allRecords;
        }

        /*
         * Find all XLS files recursively.
         */
        List<File> reportFiles = new ArrayList<>();

        collectXlsFiles(directory, reportFiles);

        /*
         * Sort files so that processing order is deterministic.
         */
        reportFiles.sort(
                Comparator.comparing(File::getAbsolutePath)
        );

        System.out.println();
        System.out.println("=================================");
        System.out.println("LOADING 2020 REPORTS");
        System.out.println("=================================");
        System.out.println(
                "Reports found: " + reportFiles.size()
        );

        /*
         * Parse every daily report.
         */
        for (File reportFile : reportFiles) {

            List<ShipmentRecord> records =
                    ReportParser.parse(reportFile);

            allRecords.addAll(records);

            System.out.println(
                    reportFile.getName()
                            + " -> "
                            + records.size()
                            + " records"
            );
        }

        System.out.println("---------------------------------");
        System.out.println(
                "Total records loaded: "
                        + allRecords.size()
        );
        System.out.println("=================================");

        return allRecords;
    }

    private static void collectXlsFiles(
            File directory,
            List<File> result) {

        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                collectXlsFiles(
                        file,
                        result
                );

            } else if (
                    file.getName()
                            .toLowerCase()
                            .endsWith(".xls")
            ) {

                result.add(file);
            }
        }
    }
}