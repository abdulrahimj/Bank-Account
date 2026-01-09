package controller;

import model.AccountsModel;
import model.BankModel;
import view.BankView;

import java.io.IOException;

public class BankController {
   private BankView bankView;
   private AccountsModel accountsList;

   public BankController (BankView bankView, AccountsModel accountsList) {
      this.bankView = bankView;
      this.accountsList = accountsList;
   }

   public void start () {

      bankView.welcomeMessage();
      int mainOptionsSelection = bankView.mainOptions();

      //Receive new account details
      //BankModel createdAccount = null;

      //Use switch to branch
      switch (mainOptionsSelection) {
         case 1 -> {
            String newAccountInfo[] = bankView.newAccountDetails();

            //Unpackage the details
            if (newAccountInfo == null) {
               return; //stop and return to main menu options
            }

            String name = newAccountInfo[0];
            String address = newAccountInfo[1];
            String phone = newAccountInfo[2];

            try {
               BankModel createdAccount = accountsList.createNewAccount(name, address, phone);

               //check if account was indeed created before saving to file and sending message
               if (createdAccount != null) {
                  accountsList.saveObjectToFile();
                  bankView.objectSavedMessage();

                  bankView.newAccountMessage(createdAccount);
               } else {
                  bankView.errorMessages("Account creation failed. Please try again!");
               }
            } catch (IOException e) {
               bankView.errorMessages("CRITICAL ERROR: Data (obj) did not save! " + e.getMessage());
            }
         }
         case 2 -> {
            int depositSelected = bankView.depositOptions();
            switch (depositSelected) {
               case 1 -> {
                  //Pass self account number to check if it exits
                  //accountsList.findAccount();

               }
            }
         }
         default -> bankView.errorMessages("Invalid Input. Please select 1-5.");
      }
   }
}
