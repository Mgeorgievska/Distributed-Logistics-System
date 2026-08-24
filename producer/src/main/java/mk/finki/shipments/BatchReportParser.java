package mk.finki.shipments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatchReportParser {

    public static List<ShipmentRecord> parseAll(File reportsDirectory) {

        List<ShipmentRecord> allRecords = new ArrayList<>();

        if (reportsDirectory == null
                || !reportsDirectory.exists()
                || !reportsDirectory.isDirectory()) {

            System.err.println(
                    "Reports directory does not exist: "
                            + (reportsDirectory != null
                            ? reportsDirectory.getAbsolutePath()
                            : "null")
            );

            return allRecords;
        }

        File[] monthDirectories =
                reportsDirectory.listFiles(File::isDirectory);

        if (monthDirectories == null) {
            System.err.println("No month directories found.");
            return allRecords;
        }

        /*
         * One generator is used for the whole batch.
         *
         * This is important because the same exporter/importer
         * combination should receive the same routeCode.
         */
        RouteCodeGenerator routeCodeGenerator =
                new RouteCodeGenerator();

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

                if (records.isEmpty()) {
                    continue;
                }

                /*
                 * Add routeCode to every ShipmentRecord.
                 *
                 * ReportParser already created the records,
                 * so we create new ShipmentRecord objects containing
                 * all original data + generated routeCode.
                 */
                for (ShipmentRecord record : records) {

                    String routeCode =
                            routeCodeGenerator.generate(
                                    record.getExporter(),
                                    record.getImporter()
                            );

                    ShipmentRecord recordWithRouteCode =
                            new ShipmentRecord(
                                    record.getDate(),
                                    routeCode,
                                    record.getCarrier(),
                                    record.getGoods(),
                                    record.getExporter(),
                                    record.getExporterCountry(),
                                    record.getImporter(),
                                    record.getImporterCountry(),
                                    record.getDeclarationType(),
                                    record.getDeclarationNumber(),
                                    record.getRevenueMKD(),
                                    record.getRevenueEUR()
                            );

                    allRecords.add(recordWithRouteCode);
                }

                System.out.println(
                        "Parsed " + records.size()
                                + " records from "
                                + file.getName()
                );
            }
        }

        return allRecords;
    }
}