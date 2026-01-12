package controller;

import model.AccountsModel;
import model.BankModel;
import view.BankView;

import java.io.IOException;
import java.math.BigDecimal;

public class BankController {
   private BankView bankView;
   private AccountsModel accountsList;

   public BankController (BankView bankView, AccountsModel accountsList) {
      this.bankView = bankView;
      this.accountsList = accountsList;
   }

   //THIS IS THE MAIN METHOD IN CONTROLLER
   public void start () {
      //DISPLAY ALL ACCOUNTS FOR TESTING
      bankView.displayAllAccounts(accountsList.getAllAccounts());

      bankView.welcomeMessage();

      //LOGIN
      String[] loginUser = bankView.loginForm();

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
      //int loginUserAccNumber = loginUserFound.getAccountNumber();

      int mainOptionsSelection = 0;
      //Check if the fetched account matches the current login account
      if (loginUserFound != null) {
         bankView.welcomeLoginUser(loginName);

         //Call and receive Account Options
         mainOptionsSelection = bankView.mainOptions();

      } else {
         bankView.errorMessages("\nAccount not in our system. Please create a new account!");
         //Take me to only create a new account option, not menu options -- not yet implemented
         return;
      }

      //Use switch to branch
      switch (mainOptionsSelection) {
         case 1 -> handleCreateNewAccountMenu();
         case 2 -> handleDepositMenu(loginUserFound);
         case 3 -> handleSelfDepositAndWithdrawMenu(loginUserFound, "WITHDRAW FORM");
         case 4 -> handleBalanceMenu(loginUserFound);
         case 5 -> handleExitMenu();
         default -> bankView.errorMessages("Invalid Input. Please select 1-5.");
      }
   }

   //FIVE METHODS THAT HANDLES THE MENU OPTIONS
   private void handleCreateNewAccountMenu () {
      String[] newAccountInfo = bankView.newAccountDetails();

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

   private void handleDepositMenu (BankModel loginUserFound) {
      int depositSelected = bankView.depositOptions();
      String formTitle = "DEPOSIT FORM";
      switch (depositSelected) {
         case 1 -> handleSelfDepositAndWithdrawMenu(loginUserFound, formTitle);
      }
   }

   private void handleSelfDepositAndWithdrawMenu (BankModel loginUserFound, String formTitle) {
      BigDecimal amount = bankView.depositAndWithdrawForm(formTitle);

      String balanceType = "";
      String balanceDirection = "";

      try {
         if (amount == null) {
            bankView.errorMessages("Invalid input. Please enter numbers only.");
         } else {
            if (formTitle == "DEPOSIT FORM") {
               //Send the money to deposit method in bank model
               loginUserFound.deposit(amount);
               balanceType = "deposited";
               balanceDirection = "to";

            } else if (formTitle.equalsIgnoreCase("WITHDRAW FORM")) {
               //Send the money to withdraw method in bank model
               loginUserFound.withdraw(amount);
               balanceType = "withdrawn";
               balanceDirection = "from";
            }

            //Save the updated list to file
            accountsList.saveObjectToFile();

            //Inform user about their new balance
            bankView.objectSavedMessage();
            if (balanceType.equalsIgnoreCase("deposited")) {
               bankView.balanceUpdate(loginUserFound.getBalance(), loginUserFound.getAccountHolder(), amount, balanceType, balanceDirection);
            } else if (balanceType.equalsIgnoreCase("withdrawn")) {
               bankView.balanceUpdate(loginUserFound.getBalance(), loginUserFound.getAccountHolder(), amount, balanceType, balanceDirection);
            }
         }
      } catch (IllegalArgumentException | IOException e) {
         bankView.errorMessages("Transaction failed. " + e.getMessage());
      }
   }

   private void handleBalanceMenu (BankModel loginUserFound) {
      BigDecimal checkBalance =  loginUserFound.getBalance();
      bankView.balanceUpdate(loginUserFound.getAccountHolder(), checkBalance);
   }

   private void handleExitMenu () {

   }

   //METHOD THAT HANDLES THE DEPOSIT OPTIONS


   private void otherDeposit () {

   }
   /*
   //LOGIN METHOD TO FIND CURRENT LOGIN USER
   public void findCurrentLoginUser () {
      //LOGIN
      String[] loginUser = bankView.loginForm();

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
   }*/
   /*
   //DEPOSIT AND WITHDRAW CHECK (Helper Method)
   public void keepAndTake (BigDecimal amount, String transactionType, String direction) {
      try {
         if (amount == null) {
            bankView.errorMessages("Invalid input. Please enter numbers only.");
         } else {
            //Send the money to the deposit method
            loginUserFound.deposit(amount);

            //Save the updated list to the file
            accountsList.saveObjectToFile();

            //Inform user about their new balance
            bankView.objectSavedMessage(); //For admin
            bankView.balanceUpdate(loginUserFound.getBalance(), loginUserFound.getAccountHolder(), amount, transactionType, direction);
         }
      } catch (IllegalArgumentException | IOException e) {
         bankView.errorMessages("Transaction failed. " + e.getMessage());
      }
   }*/
}
