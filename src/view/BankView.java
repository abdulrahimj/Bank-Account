package view;

import model.AccountsModel;
import model.BankModel;

import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankView {
   Scanner input = new Scanner(System.in);

   public void welcomeMessage () {
      System.out.println("WELCOME TO HALAL BANK");
      System.out.println("=====================");
   }

   //Bank transaction Options
   public int mainOptions () {
      System.out.println("1. Create new account");
      System.out.println("2. Deposit");
      System.out.println("3. Withdraw");
      System.out.println("4. Check balance");
      System.out.println("5. Exit");

      System.out.print("Select Choice: ");

      try {
         return input.nextInt();

      } catch (InputMismatchException e) {
         input.nextLine(); //Remove the garbage from scanner
         return -1;
      }
   }

   //DEPOSIT OPTIONS
   public int depositOptions () {
      System.out.println("\nDEPOSIT OPTION");
      System.out.println("==============");
      System.out.println("1. Self");
      System.out.println("2. Other");
      System.out.println("3. Exit");

      System.out.print("Select choice: ");

      try {
         return input.nextInt();

      } catch (InputMismatchException e) {
         input.nextLine();
         return -1;
      }
   }

   //New Account Details
   public String[] newAccountDetails () {
      System.out.println("\nNEW ACCOUNT DETAILS");
      System.out.println("===================");

      try {
         input.nextLine(); //garbage remover

         System.out.print("Name: ");
         String name = input.nextLine();

         System.out.print("Address: ");
         String address = input.nextLine();

         System.out.print("Phone: ");
         String phone = input.nextLine();

         return new String[] {name, address, phone};

      } catch (InputMismatchException e) {
         System.out.println("Invalid input. Try again!");
         return null;
      }
   }

   //Message for New Account created
   public void newAccountMessage (BankModel newAccountCreated) {
      System.out.println("\nCongratulation!!!");
      System.out.println("You have successfully created a new bank account with HALAL Bank. Account details: ");
      System.out.println("Account Name: " + newAccountCreated.getAccountHolder());
      System.out.println("Account Number: " + newAccountCreated.getAccountNumber());
      System.out.println("Balance: SLE " + newAccountCreated.getBalance());
   }

   //ERROR MESSAGES
   public void errorMessages (String message) {
      System.out.println(message);
   }

   //DATA (OBJECT) SAVED MESSAGE
   public void objectSavedMessage () {
      System.out.println("Data (obj) saved successfully to file.");
   }

   //DISPLAY ALL ACCOUNTS
   public void displayAllAccounts (List<BankModel> allAccounts) {

      for (BankModel account : allAccounts) {
         System.out.println("Acc Num: " + account.getAccountNumber());
         System.out.println("Acc Name: " + account.getAccountHolder());
         System.out.println("Balance: " + account.getBalance() + "\n");
      }
   }
}
