package com.sc2002.model;

import java.util.Date;

/**
 * Holds the response provided by an HDB staff member to an applicant's enquiry.
 */
public class EnquiryResponseModel {
    /** Counter for unique response IDs. */
    private static int nextResponseID = 0; // Static counter for auto-incrementing IDs
    /** Unique ID for this response. */
    private int responseID;
    /** User ID of the HDB staff who responded. */
    private int respondingOfficerUserID;
    /** ID of the enquiry this response is for. */
    private int enquiryID;
    /** The actual text of the response. */
    private String responseText;
    /** When the response was given. */
    private Date responseDate;

    /**
     * Creates a new response.
     * Sets the response ID and date automatically.
     *
     * @param officerResponse The response text.
     * @param enquiryID The ID of the original enquiry.
     * @param officerUserID The ID of the responding staff.
     */
    EnquiryResponseModel(String officerResponse, int enquiryID, int officerUserID) {
        responseID = nextResponseID++; // Assign current ID and increment for next use
        this.enquiryID = enquiryID; // The enquiry this response is for. Not really needed but sure
        responseText = officerResponse;
        respondingOfficerUserID = officerUserID;
        responseDate = new Date(); // Initialize response date
    }

    /**
     * Gets the unique ID of this response.
     * @return The response ID.
     */
    public int getResponseID() {
        return responseID;
    }

    /**
     * Gets the date when this response was created.
     * @return The response date.
     */
    public Date getResponseDate() {
        return responseDate;
    }

    /**
     * Gets the text content of the response.
     * @return The response text.
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Gets the User ID of the HDB staff who wrote the response.
     * @return The officer's User ID.
     */
    public int getOfficerUserID() {
        return respondingOfficerUserID;
    }
}
