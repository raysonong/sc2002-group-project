package com.sc2002.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static ArrayList<List<Object>> readUserList(String filePath) {
        ArrayList<List<Object>> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header row
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.err.println("The file is empty.");
                System.exit(1);
            }
            
            String line;
            int rowNum = 1; // Start counting from 1 after header
            while ((line = reader.readLine()) != null) {
                rowNum++;
                String[] values = line.split(","); // comma separated 
                
                if (values.length != 5) { // should have 5 cells in user listssssss
                    System.err.println("File: "+filePath+" Row " + rowNum + " does not contain exactly 5 cells. Actual Count: "+values.length);
                    System.exit(1);
                }
                
                List<Object> userData = new ArrayList<>();
                
                // Name (String)
                if (values[0] == null || values[0].isEmpty()) {
                    System.err.println("Invalid Name at row " + rowNum);
                    System.exit(1);
                }
                userData.add(values[0]);
                
                // NRIC (String)
                if (values[1] == null || values[1].isEmpty()) {
                    System.err.println("Invalid NRIC at row " + rowNum);
                    System.exit(1);
                }
                userData.add(values[1]);
                
                // Age (int)
                try {
                    int age = Integer.parseInt(values[2]);
                    userData.add(age);
                } catch (NumberFormatException e) {
                    System.err.println("Age is not a valid integer at row " + rowNum);
                    System.exit(1);
                }
                
                // Marital Status (String)
                if (values[3] == null || values[3].isEmpty()) {
                    System.err.println("Invalid Marital Status at row " + rowNum);
                    System.exit(1);
                }
                userData.add(values[3]);
                
                // Password (String)
                if (values[4] == null || values[4].isEmpty()) {
                    System.err.println("Invalid Password at row " + rowNum);
                    System.exit(1);
                }
                userData.add(values[4]);
                
                userList.add(userData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return userList;
    }

    public static ArrayList<List<Object>> readProjectList(String filePath) {
        ArrayList<List<Object>> projectList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header row
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.err.println("The file is empty.");
                System.exit(1);
            }
            
            String line;
            int rowNum = 1;
            while ((line = reader.readLine()) != null) {
                rowNum++;
                String[] values = line.split(",");
                
                if (values.length < 12) {
                    System.err.println("Row " + rowNum + " does not contain enough cells.");
                    System.exit(1);
                }
                
                List<Object> projectData = new ArrayList<>();
                
                // Project Name (String)
                if (values[0] == null || values[0].isEmpty()) {
                    System.err.println("Invalid Project Name at row " + rowNum);
                    System.exit(1);
                }
                projectData.add(values[0]);
                
                // Neighborhood (String)
                if (values[1] == null || values[1].isEmpty()) {
                    System.err.println("Invalid Neighborhood at row " + rowNum);
                    System.exit(1);
                }
                projectData.add(values[1]);
                
                // Continue with remaining fields similarly to the original code
                // Type 1
                projectData.add(values[2]);
                
                // Number of units for Type 1
                try {
                    projectData.add(Integer.parseInt(values[3]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Number of units for Type 1 at row " + rowNum);
                    System.exit(1);
                }
                
                // Selling price for Type 1
                try {
                    projectData.add(Integer.parseInt(values[4]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Selling price for Type 1 at row " + rowNum);
                    System.exit(1);
                }
                
                // Type 2
                projectData.add(values[5]);
                
                // Number of units for Type 2
                try {
                    projectData.add(Integer.parseInt(values[6]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Number of units for Type 2 at row " + rowNum);
                    System.exit(1);
                }
                
                // Selling price for Type 2
                try {
                    projectData.add(Integer.parseInt(values[7]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Selling price for Type 2 at row " + rowNum);
                    System.exit(1);
                }
                
                // Application opening date
                try {
                    LocalDate openingDate = LocalDate.parse(values[8], DateTimeFormatter.ofPattern("M/d/yy"));
                    projectData.add(openingDate);
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid Application opening date format at row " + rowNum);
                    System.exit(1);
                }
                
                // Application closing date
                try {
                    LocalDate closingDate = LocalDate.parse(values[9], DateTimeFormatter.ofPattern("M/d/yy"));
                    projectData.add(closingDate);
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid Application closing date format at row " + rowNum);
                    System.exit(1);
                }
                
                // Manager
                projectData.add(values[10]);
                
                // Officer Slot
                try {
                    projectData.add(Integer.parseInt(values[11]));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Officer Slot at row " + rowNum);
                    System.exit(1);
                }
                
                // Officer (can be empty)
                projectData.add(values.length > 12 ? values[12] : "");
                
                projectList.add(projectData);
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return projectList;
    }

    public void printCSVFormat(ArrayList<List<Object>> userList) { // For debugging purposes
        for (List<Object> user : userList) {
            System.out.println(user);
        }
    }
    
}
