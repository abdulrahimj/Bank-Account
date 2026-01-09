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
      //DISPLAY ALL ACCOUNTS FOR TESTING
      bankView.displayAllAccounts(accountsList.getAllAccounts());

      bankView.welcomeMessage();

      //LOGIN
      String loginUser[] = bankView.loginForm();

      //Check if user actually filled the form
      if (loginUser == null) {
         bankView.errorMessages("Login cancelled or invalid input.");
         return; //Stop the app or return to start
      }

      //Unpackage user login details and save to variables. And find his account.
      String loginName = loginUser[0];
      String loginPhone = loginUser[1];

      //Find account of login user
      BankModel loginUserFound = accountsList.findAccount(loginPhone);

      //Extract account number of login user
      int loginUserAccNumber = loginUserFound.getAccountNumber();

      int mainOptionsSelection = 0;
      //Check if the fetched account matches the current login account
      if (loginUserFound != null) {
         bankView.welcomeLoginUser(loginName);

         //Call and receive Account Options
         mainOptionsSelection = bankView.mainOptions();

      } else {
         bankView.errorMessages("\nAccount not in our system. Please create a new account!");
         //Take me to only create a new account option, not menu options
      }

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

               //check if an account was indeed created before saving to file and sending message
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
                  bankView.depositForm();
               }
            }
         }
         default -> bankView.errorMessages("Invalid Input. Please select 1-5.");
      }
   }
}
