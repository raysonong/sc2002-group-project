package com.sc2002.Services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.sc2002.entities.HDBManagerModel;

/**
 * Manages the creation and operations of BTO projects.
 */
public class HDBManager {

    private HDBManagerModel hdbManagerModel = null;

    /**
     * Constructs an HDBManager with the specified HDBManagerModel.
     *
     * @param hdbManagerModel The model representing the HDB manager.
     */
    public HDBManager(HDBManagerModel hdbManagerModel) {
        this.hdbManagerModel = hdbManagerModel;
    }

    
    
}
