package view;

import model.AccountsModel;
import model.BankModel;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankView {
   Scanner input = new Scanner(System.in);

   public void welcomeMessage () {
      System.out.println("WELCOME TO HALAL BANK");
      System.out.println("=====================");
   }

   //LOGIN
   public String[] loginForm () {
      System.out.println("-----Please Login----");

      try {
         System.out.print("Name: ");
         String name = input.nextLine();

         System.out.print("Phone: ");
         String phone = input.nextLine();

         return new String[] {name, phone};

      } catch (InputMismatchException e) {
         System.out.println("Invalid input. Please try again!");
         return null;
      }
   }

   public void welcomeLoginUser (String loginUser) {
      System.out.println("\nWelcome Back " + loginUser);
   }

   //Bank transaction Options
   public int mainOptions () {
      System.out.println("\n ACCOUNT OPTIONS");
      System.out.println("=================");
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
      //Define money format patter: #,##0.00
      DecimalFormat df = new DecimalFormat("#,##0.00");

      //Get the current date and time
      LocalDateTime now = LocalDateTime.now();
      //Format the date and time to human readable
      DateTimeFormatter dateWriter = DateTimeFormatter.ofPattern("E, dd-MMM-yyyy HH:mm:ss");
      String formattedDate = now.format(dateWriter);

      System.out.println("\nCongratulation!!!");
      System.out.println("You have successfully created a new bank account with HALAL Bank. Account details: ");
      System.out.println("Account Name: " + newAccountCreated.getAccountHolder());
      System.out.println("Account Number: " + newAccountCreated.getAccountNumber());
      System.out.println("Balance: SLE " + df.format(newAccountCreated.getBalance()));
      System.out.println("Phone: " + newAccountCreated.getPhone());
      System.out.println("Date: " + formattedDate);
   }

   //ERROR MESSAGES
   public void errorMessages (String message) {
      System.out.println(message);
   }

   //DATA (OBJECT) SAVED MESSAGE
   public void objectSavedMessage () {
      System.out.println("\nData (obj) saved successfully to file.");
   }

   //DISPLAY ALL ACCOUNTS
   public void displayAllAccounts (List<BankModel> allAccounts) {
      //Define money format patter: #,##0.00
      DecimalFormat df = new DecimalFormat("#,##0.00");

      for (BankModel account : allAccounts) {
         System.out.println("Acc Num: " + account.getAccountNumber());
         System.out.println("Acc Name: " + account.getAccountHolder());
         System.out.println("Balance NLe: " + df.format(account.getBalance()));
         System.out.println("Phone: " + account.getPhone() + "\n");
      }
   }

   //DEPOSIT FORM
   public BigDecimal depositForm () {
      System.out.println("\nDEPOSIT FORM");
      System.out.println("=============");

      try {
         input.nextLine(); //remove garbage

         System.out.print("Amount (NLe): ");
         return input.nextBigDecimal();

      } catch (InputMismatchException e) {
         input.nextLine(); //Clear the invalid text from scanner
         System.out.println("Invalid input. Try again!");
         return null;
      }
   }

   //BALANCE UPDATE
   public void balanceUpdate (BigDecimal newBalance, String name, BigDecimal amount) {
      //Define money format patter: #,##0.00
      DecimalFormat df = new DecimalFormat("#,##0.00");

      //Get the current date and time
      LocalDateTime now = LocalDateTime.now();
      //Format the date and time to human readable
      DateTimeFormatter dateWriter = DateTimeFormatter.ofPattern("E, dd-MMM-yyyy HH:mm:ss");
      String formattedDate = now.format(dateWriter);

      System.out.println("Dear " + name + ", you have successfully deposited (NLe:" + df.format(amount) + ") to your account.");
      System.out.println("Your new Balance is NLe:" + df.format(newBalance));
      System.out.println("Date: " + formattedDate);
   }
}
