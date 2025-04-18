package com.sc2002.utilities;

/**
 * Utility class for validating NRIC numbers
 */
public class NRICValidator {

    /**
     * Default constructor for NRICValidator.
     * As this class only contains static methods, instantiation is generally not needed.
     */
    public NRICValidator() {
        // Default constructor
    }
    
    /**
     * Validates if a string is a valid NRIC
     * 
     * Format:
     * - First character: S, T
     * - Next 7 digits: numeric characters
     * - Last character: A-Z
     * 
     * @param nric The NRIC string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNRIC(String nric) {
        if (nric == null || nric.length() != 9) {
            return false;
        }
        
        // Convert to uppercase for checking
        nric = nric.toUpperCase();
        
        // Check first character is S or T,
        char firstChar = nric.charAt(0);
        if (firstChar != 'S' && firstChar != 'T') {
            return false;
        }
        
        // followed by 7 characters are digits
        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(nric.charAt(i))) {
                return false;
            }
        }
        
        // Check last character is a letter
        char lastChar = nric.charAt(8);
        return Character.isLetter(lastChar);
        
    }
    
    /**
     * Formats the NRIC to uppercase
     * 
     * @param nric The NRIC string to format
     * @return The formatted NRIC in uppercase
     */
    public static String formatNRIC(String nric) {
        if (nric == null) {
            return null;
        }
        return nric.toUpperCase();
    }
}
