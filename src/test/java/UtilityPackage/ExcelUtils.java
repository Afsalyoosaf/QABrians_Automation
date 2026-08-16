package UtilityPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    // ===========================
    // READ DATA FROM EXCEL
    // ===========================

    public static Object[][] getExcelData(String sheetName) throws IOException {

        String path = "src/test/resources/LoginData/Login_Data.xlsx";

        FileInputStream file = new FileInputStream(path);

        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            workbook.close();
            file.close();
            throw new RuntimeException("Sheet '" + sheetName + "' not found.");
        }

        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getPhysicalNumberOfCells();

        Object[][] data = new Object[rows - 1][cols];

        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                data[i - 1][j] =
                        formatter.formatCellValue(sheet.getRow(i).getCell(j));

            }
        }

        workbook.close();
        file.close();

        return data;
    }

    // ===========================
    // WRITE PRODUCT NAMES TO EXCEL
    // ===========================

    public static void writeProductDetails(List<String[]> products) throws IOException {

        String folderPath = "src/test/resources/ProductData";

        java.io.File folder = new java.io.File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String path = folderPath + "/ProductDetails.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Products");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Product Name");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Image");
        header.createCell(3).setCellValue("Add To Cart");

        int rowNum = 1;

        for (String[] product : products) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(product[0]);
            row.createCell(1).setCellValue(product[1]);
            row.createCell(2).setCellValue(product[2]);
            row.createCell(3).setCellValue(product[3]);
        }

        FileOutputStream output = new FileOutputStream(path);

        workbook.write(output);

        output.close();
        workbook.close();

        System.out.println("Excel created successfully.");
        System.out.println("Saved at: " + path);
    }
}