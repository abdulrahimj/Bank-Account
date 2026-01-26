package controller;

import model.AccountsModel;
import model.BankModel;
import view.BankView;

import java.io.IOException;
import java.math.BigDecimal;

public class BankController {
   private BankView bankView;
   private AccountsModel accountsList;
   private boolean activeLogin = true;

   public BankController (BankView bankView, AccountsModel accountsList) {
      this.bankView = bankView;
      this.accountsList = accountsList;

      //DISPLAY ALL ACCOUNTS FOR TESTING
      bankView.displayAllAccounts(accountsList.getAllAccounts());
   }

   //THIS IS THE MAIN METHOD IN CONTROLLER
   public void start () {

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

      //Check if the fetched account matches the current login account
      if (loginUserFound != null && (loginUserFound.getAccountHolder().equalsIgnoreCase(loginName ) && loginUserFound.getPhone().equalsIgnoreCase(loginPhone))) {
         bankView.welcomeLoginUser(loginName);

         //--KEEP APP RUNNING UNLESS EXIT BY USER
         while (activeLogin) {
            //Call and receive Account Options
            int mainOptionsSelection = bankView.mainOptions();

            //Use switch to branch
            switch (mainOptionsSelection) {
               case 1 -> handleCreateNewAccountMenu();
               case 2 -> handleDepositMenu(loginUserFound);
               case 3 -> handleSelfDepositAndWithdrawMenu(loginUserFound, "WITHDRAW FORM");
               case 4 -> handleBalanceMenu(loginUserFound);
               case 5 -> activeLogin = handleExitMenu("banking with");  //Call exit and Stops app (loop)
               default -> bankView.errorMessages("Invalid Input. Please select 1-5.");
            }
         }

      } else {
         if (loginUserFound != null && loginUserFound.getPhone().equalsIgnoreCase(loginPhone)) {
            bankView.errorMessages("\nUserName or Phone is incorrect. Please try again!\n");
            start();
         } else {
            int choice = bankView.errorMessages();

            switch (choice) {
               case 1 -> handleCreateNewAccountMenu(); //Takes me to create a new account option
               case 2 -> activeLogin = handleExitMenu("visiting");
               default -> bankView.errorMessages("Invalid input. Please select 1 or 2.");
            }
         }
         return;
      }
   }

   //FIVE METHODS THAT HANDLES THE MENU OPTIONS
   private void handleCreateNewAccountMenu () {
      String[] newAccountInfo = bankView.newAccountDetails();

      //Unpackage the details
      if (newAccountInfo == null || newAccountInfo.length < 3) {
         return; //stop and return to main menu options
      }

      String name = newAccountInfo[0];
      String address = newAccountInfo[1];
      String phone = newAccountInfo[2];

      try {
         /*Check if the phone number is already in the accounts list
          *Prevent user from using the same phone number to create a new account
          *One phone number per account
          */
         BankModel checkPhoneNumber = accountsList.findAccount(phone);
         if (checkPhoneNumber != null) {
            bankView.errorMessages("\nSorry, you cannot create a new account with this phone number. Please use another number!");
            return; //Stop and return to menu
         }

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
      boolean exit = true;
      switch (depositSelected) {
         case 1 -> handleSelfDepositAndWithdrawMenu(loginUserFound, formTitle);
         case 2 -> otherDeposit(loginUserFound);
         case 3 -> handleExitMenu("none"); //This will take me back to the main menu
         case 4 -> activeLogin = handleExitMenu("banking with");
         default -> bankView.errorMessages("Invalid Input. Please select 1-4.");
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
            if ("DEPOSIT FORM".equals(formTitle)) {
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

   private boolean handleExitMenu (String type) {
      if (type.equalsIgnoreCase("banking with")) {
         bankView.errorMessages("Thanks for " + type + " us.");
      }
      return false;  //This will stop the loop (app)
   }

   //METHOD THAT HANDLES OTHER DEPOSIT OPTIONS
   private void otherDeposit (BankModel sender) {
      //Get amount and accNum for recipient
      BankView.OtherDepositData data = bankView.otherDepositForm();

      //Verify if we got the data
      if (data == null) {
         bankView.errorMessages("Invalid input. Please try again.");
         return;
      }

      //Search if recipient is in list
      BankModel recipient = accountsList.findAccount(data.accNum());

      //Verify
      if (recipient == null) {
         bankView.errorMessages("\nRecipient account not found! Please check carefully!!");
         return;
      }

      try {
         //Transfer the money
         BigDecimal amountToTransfer = data.amount();
         sender.withdraw(amountToTransfer); //Withdraw it from sender's account
         recipient.deposit(amountToTransfer); //Transfer to recipient account

         //Save and notify
         accountsList.saveObjectToFile();
         bankView.objectSavedMessage();
         bankView.balanceUpdate(sender.getBalance(), sender.getAccountHolder(), amountToTransfer, "transferred ",  " to " + recipient.getAccountHolder() + " from ");

      } catch (IllegalArgumentException | IOException e) {
         bankView.errorMessages("Transaction failed: " + e.getMessage());
      }
   }
}