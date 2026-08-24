package mk.finki.shipments;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;

public class ReportScannerTest {

    public static void main(String[] args) {

        String rootPath =
                "data/reports/Izvestaj 2 0 2 0";

        File root = new File(rootPath);

        System.out.println("=================================");
        System.out.println("SCANNING 2020 REPORTS");
        System.out.println("=================================");
        System.out.println("Directory: " + root.getAbsolutePath());
        System.out.println();

        if (!root.exists() || !root.isDirectory()) {

            System.err.println(
                    "Reports directory does not exist!"
            );

            return;
        }

        File[] months = root.listFiles(File::isDirectory);

        if (months == null) {

            System.err.println(
                    "No month directories found."
            );

            return;
        }

        int totalFiles = 0;
        int filesWithData = 0;

        for (File month : months) {

            File[] reports =
                    month.listFiles((dir, name) ->
                            name.toLowerCase().endsWith(".xls")
                    );

            if (reports == null) {
                continue;
            }

            for (File report : reports) {

                totalFiles++;

                int dataRows =
                        countDataRows(report);

                if (dataRows > 0) {

                    filesWithData++;

                    System.out.println(
                            "FOUND DATA:"
                    );

                    System.out.println(
                            "Month: " + month.getName()
                    );

                    System.out.println(
                            "File: " + report.getName()
                    );

                    System.out.println(
                            "Data rows: " + dataRows
                    );

                    System.out.println(
                            "Path: " +
                                    report.getAbsolutePath()
                    );

                    System.out.println(
                            "---------------------------------"
                    );

                    /*
                     * Stop after finding first few useful reports.
                     */
                    if (filesWithData >= 10) {

                        System.out.println();
                        System.out.println(
                                "Found 10 reports with data."
                        );

                        printSummary(
                                totalFiles,
                                filesWithData
                        );

                        return;
                    }
                }
            }
        }

        printSummary(
                totalFiles,
                filesWithData
        );
    }

    private static int countDataRows(File file) {

        int count = 0;

        try (
                FileInputStream inputStream =
                        new FileInputStream(file);

                Workbook workbook =
                        new HSSFWorkbook(inputStream)
        ) {

            Sheet sheet =
                    workbook.getSheet("Sheet1");

            if (sheet == null) {
                return 0;
            }

            /*
             * Rows 0-2 are headers/information.
             * We look from row 3 onward.
             */
            for (
                    int rowIndex = 3;
                    rowIndex <= sheet.getLastRowNum();
                    rowIndex++
            ) {

                Row row =
                        sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                String ref =
                        getCellValue(row, 1);

                String declaration =
                        getCellValue(row, 2);

                String carrier =
                        getCellValue(row, 5);

                String goods =
                        getCellValue(row, 6);

                String exporter =
                        getCellValue(row, 7);

                String importer =
                        getCellValue(row, 8);

                /*
                 * Ignore formula/total rows.
                 *
                 * A real shipment should have
                 * at least some identification data.
                 */
                boolean hasRealData =
                        !ref.isBlank()
                                || !declaration.isBlank()
                                || !carrier.isBlank()
                                || !goods.isBlank()
                                || !exporter.isBlank()
                                || !importer.isBlank();

                /*
                 * Ignore the special "0" total row.
                 */
                if (declaration.equals("0")
                        && ref.isBlank()
                        && carrier.isBlank()
                        && goods.isBlank()
                        && exporter.isBlank()
                        && importer.isBlank()) {

                    continue;
                }

                if (hasRealData) {
                    count++;
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error reading: " +
                            file.getName()
            );
        }

        return count;
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

    private static void printSummary(
            int totalFiles,
            int filesWithData) {

        System.out.println();
        System.out.println(
                "================================="
        );

        System.out.println(
                "SCAN FINISHED"
        );

        System.out.println(
                "Total XLS files: " +
                        totalFiles
        );

        System.out.println(
                "Reports with data: " +
                        filesWithData
        );

        System.out.println(
                "================================="
        );
    }
}