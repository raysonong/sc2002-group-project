package com.sc2002;

import java.util.*;

import com.sc2002.entities.BTOProjectModel;
import com.sc2002.entities.HDBManagerModel;
import com.sc2002.Services.HDBManager;
import com.sc2002.entities.User;
import com.sc2002.entities.ApplicantModel;
import com.sc2002.utilities.MenuManager;

/**
 * The main entry point for the BTO project management application.
 */
public class Main {

    /**
     * The main method to run the application.
     *
     * @param args Command-line arguments (not used).
     */

    private ArrayList<BTOProjectModel> btoProjectModels = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello world!");

        HDBManagerModel hdbManagerModel1 = new HDBManagerModel("Tew", "123", 18, "Married", "123");
        HDBManagerModel hdbManagerModel2 = new HDBManagerModel("Huw", "126", 18, "Married", "123");

        ArrayList<HDBManagerModel> hDBManagerModels = new ArrayList<>();
        hDBManagerModels.add(hdbManagerModel1);
        hDBManagerModels.add(hdbManagerModel2);

        HDBManager hdbManager = new HDBManager(hdbManagerModel1);

        String input = null;

        Scanner sc = new Scanner(System.in);

        User currentUser = new HDBManagerModel("","",1,"","");
    }



    public void HDBManagerMenu(Scanner scanner) {
        System.out.printf("Total amount of BTO Projects: %d%n", btoProjectModels.size());

        System.out.println("\nPrinting all BTO Projects:");
        for (BTOProjectModel project : btoProjectModels) {
            project.printAll();
            System.out.println("----------------------------");
        }
    }
}