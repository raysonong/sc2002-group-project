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
    

    public static ArrayList<List<Object>> readProjectList(String filePath){
        ArrayList<List<Object>> projectList = new ArrayList<>();
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
            List<Object> projectData = new ArrayList<>();

            // Project Name (String)
            Cell cell = row.getCell(0);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Project Name at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            // Neighborhood (String)
            cell = row.getCell(1);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Neighborhood at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            // Type 1 (String)
            cell = row.getCell(2);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Type 1 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            // Number of units for Type 1 (int)
            cell = row.getCell(3);
            int unitsType1 = 0;
            if (cell == null) {
                System.err.println("Number of units for Type 1 is missing at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                unitsType1 = (int) cell.getNumericCellValue();
            } else {
                System.err.println("Invalid Number of units for Type 1 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(unitsType1);

            // Selling price for Type 1 (int)
            cell = row.getCell(4);
            int sellingPriceType1 = 0;
            if (cell == null) {
                System.err.println("Selling price for Type 1 is missing at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                sellingPriceType1 = (int) cell.getNumericCellValue();
            } else {
                System.err.println("Invalid Selling price for Type 1 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(sellingPriceType1);

            // Type 2 (String)
            cell = row.getCell(5);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Type 2 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            // Number of units for Type 2 (int)
            cell = row.getCell(6);
            int unitsType2 = 0;
            if (cell == null) {
                System.err.println("Number of units for Type 2 is missing at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                unitsType2 = (int) cell.getNumericCellValue();
            } else {
                System.err.println("Invalid Number of units for Type 2 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(unitsType2);

            // Selling price for Type 2 (int)
            cell = row.getCell(7);
            int sellingPriceType2 = 0;
            if (cell == null) {
                System.err.println("Selling price for Type 2 is missing at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                sellingPriceType2 = (int) cell.getNumericCellValue();
            } else {
                System.err.println("Invalid Selling price for Type 2 at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(sellingPriceType2);

            // Application opening date (LocalDate)
            cell = row.getCell(8);
            if (cell == null) {
                System.err.println("Invalid Application opening date at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                projectData.add(cell.getLocalDateTimeCellValue().toLocalDate()); // Convert numeric date to LocalDate
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(cell.getStringCellValue(), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    projectData.add(date);
                } catch (java.time.format.DateTimeParseException e) {
                    System.err.println("Invalid Application opening date format at row " + row.getRowNum());
                    System.exit(1);
                }
            } else {
                System.err.println("Invalid Application opening date format at row " + row.getRowNum());
                System.exit(1);
            }

            // Application closing date (LocalDate)
            cell = row.getCell(9);
            if (cell == null) {
                System.err.println("Invalid Application closing date at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                projectData.add(cell.getLocalDateTimeCellValue().toLocalDate()); // Convert numeric date to LocalDate
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(cell.getStringCellValue(), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    projectData.add(date);
                } catch (java.time.format.DateTimeParseException e) {
                    System.err.println("Invalid Application closing date format at row " + row.getRowNum());
                    System.exit(1);
                }
            } else {
                System.err.println("Invalid Application closing date format at row " + row.getRowNum());
                System.exit(1);
            }

            // Manager (String)
            cell = row.getCell(10);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Manager at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            // Officer Slot (int)
            cell = row.getCell(11);
            int officerSlot = 0;
            if (cell == null) {
                System.err.println("Officer Slot is missing at row " + row.getRowNum());
                System.exit(1);
            }
            if (cell.getCellType() == CellType.NUMERIC) {
                officerSlot = (int) cell.getNumericCellValue();
            } else {
                System.err.println("Invalid Officer Slot at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(officerSlot);

            // Officer (String)
            cell = row.getCell(12);
            if (cell == null || cell.getCellType() != CellType.STRING) {
                System.err.println("Invalid Officer at row " + row.getRowNum());
                System.exit(1);
            }
            projectData.add(cell.getStringCellValue());

            projectList.add(projectData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return projectList;
    }

    public void printXLSXFormat(ArrayList<List<Object>> userList) {
        for (List<Object> user : userList) {
            System.out.println(user);
        }
    }
    public static void main(String[] args) {
        String filePath = "./bto/src/main/data/ProjectList.xlsx"; // Replace with the actual file path
        ArrayList<List<Object>> projectList = readProjectList(filePath);

        for (List<Object> project : projectList) {
            System.out.println(project);
        }
    }
}
