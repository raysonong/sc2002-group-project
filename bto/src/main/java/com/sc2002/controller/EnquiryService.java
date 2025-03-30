package com.sc2002.controller;

import com.sc2002.model.BTOProjectModel;
import com.sc2002.model.Enquiry;
import com.sc2002.model.User;
import com.sc2002.enums.UserRole;
import com.sc2002.repositories.EnquiryRepo;

public class EnquiryService {
    public void submitEnquiry(EnquiryRepo EnquiryRepo,User Applicant,BTOProjectModel BTOProject,String enquiryMessage){

        // Constructor for public Enquiry(
        //              String applicantNRIC, 
        //              int applicantProjectId, 
        //              String applicantEnquiryText
        //                  )
        if(Applicant.getUsersRole()==UserRole.APPLICANT || Applicant.getUsersRole()==UserRole.HDB_OFFICER){
            Enquiry newEnq = new Enquiry(Applicant.getNRIC(), BTOProject.getProjectID(), enquiryMessage);
            EnquiryRepo.saveEnquiry(newEnq);
        }
        

    }
    public void editEnquiry(EnquiryRepo EnquiryRepo,String ApplicantNRIC){

    }
}
