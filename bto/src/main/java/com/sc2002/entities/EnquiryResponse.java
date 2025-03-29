package com.sc2002.entities;

import java.util.Date;

public class EnquiryResponse {
    private int responseId;
    private String respondingOfficerNRIC;
    private int enquiryId;
    private String responseText;
    private Date responseDate;
    protected EnquiryResponse(int enquiryIds){
        // get next responseId from enquiry repo
        //
        enquiryId=enquiryIds;
        
    }
    // unsure how to proceed, should i do public so that services can handle it?
    //protected ,i think public 
    public int getResponseId() {
        return responseId;
    }
}
