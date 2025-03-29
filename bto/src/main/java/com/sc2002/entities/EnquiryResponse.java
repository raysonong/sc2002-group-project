package com.sc2002.entities;

import java.util.Date;

public class EnquiryResponse {
    private static int nextResponseId = 0; // Static counter for auto-incrementing IDs
    private int responseId;
    private int respondingOfficerUserID;
    private int enquiryId;
    private String responseText;
    private Date responseDate;
    EnquiryResponse(String officerResponse,int enquiryIds,int officerUserID){
        responseId = nextResponseId++; // Assign current ID and increment for next use
        enquiryId=enquiryIds;
        responseText=officerResponse;
        respondingOfficerUserID=officerUserID;
        responseDate = new Date(); // Initialize response date
    }
    public int getResponseId() {
        return responseId;
    }
    public Date getResponseDate() {
        return responseDate;
    }

    public String getResponseText() {
        return responseText;
    }
    public int getOfficerUserId() {
        return respondingOfficerUserID;
    }
}
