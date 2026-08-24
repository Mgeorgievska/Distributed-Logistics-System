package mk.finki.shipments;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;

public class ReportDebugTest {

    public static void main(String[] args) {

        String filePath =
                "data/reports/Izvestaj 2 0 2 0/"
                        + "01 Januari 2020/"
                        + "Izvestaj - 01.01.2020.xls";

        File file = new File(filePath);

        System.out.println("=================================");
        System.out.println("REPORT DEBUG");
        System.out.println("=================================");
        System.out.println("File: " + file.getAbsolutePath());

        try (
                FileInputStream input =
                        new FileInputStream(file);

                Workbook workbook =
                        new HSSFWorkbook(input)
        ) {

            System.out.println(
                    "Number of sheets: "
                            + workbook.getNumberOfSheets()
            );

            for (
                    int sheetIndex = 0;
                    sheetIndex < workbook.getNumberOfSheets();
                    sheetIndex++
            ) {

                Sheet sheet =
                        workbook.getSheetAt(sheetIndex);

                System.out.println();
                System.out.println(
                        "================================="
                );

                System.out.println(
                        "SHEET "
                                + sheetIndex
                                + ": "
                                + sheet.getSheetName()
                );

                System.out.println(
                        "Last row: "
                                + sheet.getLastRowNum()
                );

                System.out.println(
                        "================================="
                );

                DataFormatter formatter =
                        new DataFormatter();

                for (
                        int rowIndex = 0;
                        rowIndex <= sheet.getLastRowNum();
                        rowIndex++
                ) {

                    Row row =
                            sheet.getRow(rowIndex);

                    if (row == null) {
                        continue;
                    }

                    System.out.print(
                            "Row "
                                    + rowIndex
                                    + ": "
                    );

                    boolean hasValue = false;

                    for (
                            int col = 0;
                            col < 15;
                            col++
                    ) {

                        Cell cell =
                                row.getCell(
                                        col,
                                        Row.MissingCellPolicy
                                                .RETURN_BLANK_AS_NULL
                                );

                        if (cell != null) {

                            String value =
                                    formatter
                                            .formatCellValue(cell)
                                            .trim();

                            if (!value.isBlank()) {

                                hasValue = true;

                                System.out.print(
                                        "["
                                                + col
                                                + "]="
                                                + value
                                                + " | "
                                );
                            }
                        }
                    }

                    if (hasValue) {
                        System.out.println();
                    }
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error reading report:"
            );

            e.printStackTrace();
        }

        System.out.println();
        System.out.println(
                "================================="
        );
        System.out.println("DEBUG FINISHED");
        System.out.println(
                "================================="
        );
    }
}