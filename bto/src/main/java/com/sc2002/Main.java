package com.sc2002;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main entry point for the BTO project management application.
 */
public class Main {

    /**
     * The main method to run the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");

        HDBManagerModel hdbManagerModel1 = new HDBManagerModel("Tew", "123", 18, "Married", "123");
        HDBManagerModel hdbManagerModel2 = new HDBManagerModel("Huw", "126", 18, "Married", "123");
        
        ArrayList<HDBManagerModel> hDBManagerModels = new ArrayList<>();
        hDBManagerModels.add(hdbManagerModel1);
        hDBManagerModels.add(hdbManagerModel2);

        HDBManager hdbManager = new HDBManager(hdbManagerModel1);

        ArrayList<BTOProjectModel> btoProjectModels = new ArrayList<>();

        String input = null;

        Scanner sc = new Scanner(System.in);

        do { 
            System.out.println("Creating a new BTO Projects ... ...");
            BTOProjectModel btoProjectModel;
            if (btoProjectModels.isEmpty()) {
                btoProjectModel = hdbManager.createProject(1, sc);
            } else {
                btoProjectModel = hdbManager.createProject(btoProjectModels.get(btoProjectModels.size() - 1).getProjectID() + 1, sc);
            }
            
            btoProjectModels.add(btoProjectModel);

            System.out.println("Continue? : ");
            input = sc.nextLine();

        } while ("yes".equals(input));

        sc.close();

        System.out.printf("Total amount of BTO Projects: %d%n", btoProjectModels.size());

        System.out.println("\nPrinting all BTO Projects:");
        for (BTOProjectModel project : btoProjectModels) {
            project.printAll();
            System.out.println("----------------------------");
        }
    }
}