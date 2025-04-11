electedProjectId);
      if (selectedProject == null) {
         System.out.println("Invalid Project ID. Please try again.");
         return;
     }

   
      System.out.print("Enter your enquiry: ");
      String enquiryText = appContext.getScanner().nextLine();