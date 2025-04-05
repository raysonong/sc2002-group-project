package com.sc2002.utilities;

/**
 * Utility class for validating Singapore NRIC/FIN numbers
 */
public class NRICValidator {
    
    /**
     * Validates if a string is a valid Singapore NRIC/FIN
     * 
     * Format:
     * - First character: S, T, F, or G
     * - Next 7 digits: numeric characters
     * - Last character: checksum letter (A-Z)
     * 
     * @param nric The NRIC/FIN string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNRIC(String nric) {
        if (nric == null || nric.length() != 9) {
            return false;
        }
        
        // Convert to uppercase for checking
        nric = nric.toUpperCase();
        
        // Check first character
        char firstChar = nric.charAt(0);
        if (firstChar != 'S' && firstChar != 'T' && firstChar != 'F' && firstChar != 'G') {
            return false;
        }
        
        // Check middle 7 characters are digits
        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(nric.charAt(i))) {
                return false;
            }
        }
        
        // Check last character is a letter
        char lastChar = nric.charAt(8);
        return Character.isLetter(lastChar);
        
        // Note: In a real system, we would also validate the checksum
        // but for simplicity, we're just checking the format
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
