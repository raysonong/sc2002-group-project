package com.sc2002.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXReader {

    /**
     * The function `readUserList` reads user data from an Excel file and returns a list of user
     * information as objects.
     * 
     * @param filePath The `readUserList` method you provided reads user data from an Excel file
     * specified by the `filePath` parameter. The Excel file is expected to have data in a specific
     * format with 5 columns representing Name, NRIC, Age, Marital Status, and Password for each user.
     * 
     * @return The method `readUserList` returns an `ArrayList` of `List<Object>`, which represents a
     * list of user data read from the specified file path. Each `List<Object>` in the `ArrayList`
     * contains user information such as Name (String), NRIC (String), Age (int), Marital Status
     * (String), and Password (String) for each user entry in the file.
     */
    public static ArrayList<List<Object>> readUserList(String filePath) {
        ArrayList<List<Object>> userList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() < 2) {
                System.err.println("The file does not contain any data rows.");
                System.exit(1);
            }
            // Skip header row
            boolean firstRow = true;
            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                if (row.getPhysicalNumberOfCells() != 5) {
                    System.err.println("Row " + row.getRowNum() + " does not contain exactly 5 cells.");
                    System.exit(1);
                }
                List<Object> userData = new ArrayList<>();

                // Name (String)
                Cell cell = row.getCell(0);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    System.err.println("Invalid Name at row " + row.getRowNum());
                    System.exit(1);
                }
                userData.add(cell.getStringCellValue());

                // NRIC (String)
                cell = row.getCell(1);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    System.err.println("Invalid NRIC at row " + row.getRowNum());
                    System.exit(1);
                }
                userData.add(cell.getStringCellValue());

                // Age (int)
                cell = row.getCell(2);
                int age = 0; // Initialize with default value
                if (cell == null) {
                    System.err.println("Age cell is missing at row " + row.getRowNum());
                    System.exit(1);
                }
                if (cell.getCellType() == CellType.NUMERIC) {
                    age = (int) cell.getNumericCellValue();
                } else if (cell.getCellType() == CellType.STRING) {
                    try {
                        age = Integer.parseInt(cell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Age is not a valid integer at row " + row.getRowNum());
                        System.exit(1);
                    }
                } else {
                    System.err.println("Invalid Age type at row " + row.getRowNum());
                    System.exit(1);
                }
                userData.add(age);
                // Marital Status (String)
                cell = row.getCell(3);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    System.err.println("Invalid Marital Status at row " + row.getRowNum());
                    System.exit(1);
                }
                userData.add(cell.getStringCellValue());

                // Password (String)
                cell = row.getCell(4);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    System.err.println("Invalid Password at row " + row.getRowNum());
                    System.exit(1);
                }
                userData.add(cell.getStringCellValue());

                userList.add(userData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return userList;
    }

    /**
     * The function prints the contents of an ArrayList of lists in XLSX format.
     * 
     * @param userList An ArrayList of Lists of Objects containing user data.
     */
    public void printXLSXFormat(ArrayList<List<Object>> userList) {
        for (List<Object> user : userList) {
            System.out.println(user);
        }
    }

    public static void main(String[] args) {
        String filePath = "ApplicantList.xlsx"; // Replace with your file path
        ArrayList<List<Object>> userList = readUserList(filePath);
        XLSXReader xlsxReader = new XLSXReader();
        xlsxReader.printXLSXFormat(userList);
    }
}
