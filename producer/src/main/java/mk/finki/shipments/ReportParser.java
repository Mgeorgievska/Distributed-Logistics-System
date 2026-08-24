package mk.finki.shipments;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportParser {

    public static List<ShipmentRecord> parse(File file) {

        List<ShipmentRecord> records = new ArrayList<>();

        try (FileInputStream inputStream =
                     new FileInputStream(file);
             Workbook workbook =
                     new HSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheet("Sheet1");

            if (sheet == null) {
                System.err.println(
                        "Sheet1 not found in: " +
                                file.getName()
                );
                return records;
            }

            /*
             * Row 0 = report title
             * Row 1 = column headers
             * Row 2 = previous day information
             * Row 3+ = data/formulas
             */

            for (int rowIndex = 3;
                 rowIndex <= sheet.getLastRowNum();
                 rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                String refNumber =
                        getCellValue(row, 1);

                String declarationNumber =
                        getCellValue(row, 2);

                String carrier =
                        getCellValue(row, 5);

                String goods =
                        getCellValue(row, 6);

                String exporter =
                        getCellValue(row, 7);

                String importer =
                        getCellValue(row, 8);

                String revenueCash =
                        getCellValue(row, 9);

                String revenueMKD =
                        getCellValue(row, 10);

                String revenueEUR =
                        getCellValue(row, 11);

                /*
                 * Ignore completely empty rows.
                 */

                if (isEmpty(
                        refNumber,
                        declarationNumber,
                        carrier,
                        goods,
                        exporter,
                        importer,
                        revenueCash,
                        revenueMKD,
                        revenueEUR)) {

                    continue;
                }

                /*
                 * Extract date from file name.
                 *
                 * Example:
                 * Izvestaj - 01.01.2020.xls
                 */

                String date =
                        extractDate(file.getName());

                double mkd =
                        parseNumber(revenueMKD);

                double eur =
                        parseNumber(revenueEUR);

                ShipmentRecord record =
                        new ShipmentRecord(
                                date,
                                carrier,
                                goods,
                                exporter,
                                "",
                                importer,
                                "",
                                declarationNumber,
                                refNumber,
                                mkd,
                                eur
                        );

                records.add(record);
            }

        } catch (Exception e) {

            System.err.println(
                    "Error parsing report: " +
                            file.getAbsolutePath()
            );

            e.printStackTrace();
        }

        return records;
    }

    private static String getCellValue(
            Row row,
            int columnIndex) {

        Cell cell =
                row.getCell(
                        columnIndex,
                        Row.MissingCellPolicy
                                .RETURN_BLANK_AS_NULL
                );

        if (cell == null) {
            return "";
        }

        DataFormatter formatter =
                new DataFormatter();

        return formatter
                .formatCellValue(cell)
                .trim();
    }

    private static boolean isEmpty(
            String... values) {

        for (String value : values) {

            if (value != null &&
                    !value.isBlank()) {

                return false;
            }
        }

        return true;
    }

    private static double parseNumber(
            String value) {

        if (value == null ||
                value.isBlank()) {

            return 0.0;
        }

        try {

            String normalized =
                    value
                            .replace(" ", "")
                            .replace(",", ".");

            return Double.parseDouble(
                    normalized
            );

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }

    private static String extractDate(
            String fileName) {

        /*
         * Expected:
         * Izvestaj - 01.01.2020.xls
         */

        String[] parts =
                fileName.split("-");

        if (parts.length < 2) {
            return "";
        }

        return parts[1]
                .replace(".xls", "")
                .trim();
    }
}