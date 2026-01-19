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
   private final Scanner input = new Scanner(System.in);
   //Record Container to return different data from a method
   public record FormatResult (DecimalFormat currencyFormatter, String formattedDate) {}
   //Record Container to return int and BigDecimal
   public record OtherDepositData (int accNum, BigDecimal amount) {}

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
         int choice = input.nextInt();
         input.nextLine(); //cleanup
         return choice;

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
      System.out.println("3. Back");
      System.out.println("4. Exit");

      System.out.print("Select choice: ");

      try {
         int choice = input.nextInt();
         input.nextLine(); //cleanup
         return choice;

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
      //Call helper method
      FormatResult formats = getCurrencyAndDate();

      System.out.println("\nCongratulation!!!");
      System.out.println("You have successfully created a new bank account with HALAL Bank. Account details: ");
      System.out.println("Account Name: " + newAccountCreated.getAccountHolder());
      System.out.println("Account Number: " + newAccountCreated.getAccountNumber());
      System.out.println("Balance: NLe " + formats.currencyFormatter().format(newAccountCreated.getBalance()));
      System.out.println("Phone: " + newAccountCreated.getPhone());
      System.out.println("Date: " + formats.formattedDate());
   }

   //ERROR MESSAGES
   public void errorMessages (String message) {
      System.out.println(message);
   }

   public int errorMessages () {

      System.out.println("\nAccount not in our system. Create a new account? ");
      System.out.println("1. Yes");
      System.out.println("2. No");

      System.out.print("Choice: ");

      try {

         int choice = input.nextInt();
         input.nextLine(); //cleanup
         return choice;

      } catch (InputMismatchException e) {
         input.nextLine(); //cleanup
         return -1;
      }
   }

   //DATA (OBJECT) SAVED MESSAGE
   public void objectSavedMessage () {
      System.out.println("\nData (obj) saved successfully to file.");
   }

   //DISPLAY ALL ACCOUNTS
   public void displayAllAccounts (List<BankModel> allAccounts) {
      //Call helper method
      FormatResult formats = getCurrencyAndDate();

      for (BankModel account : allAccounts) {
         System.out.println("Acc Num: " + account.getAccountNumber());
         System.out.println("Acc Name: " + account.getAccountHolder());
         System.out.println("Balance NLe: " + formats.currencyFormatter().format(account.getBalance()));
         System.out.println("Phone: " + account.getPhone() + "\n");
      }
   }

   //DEPOSIT & WITHDRAWAL FORM FOR SELF
   public BigDecimal depositAndWithdrawForm (String heading) {
      System.out.println("\n"+ heading);
      System.out.println("=============");

      try {

         System.out.print("Amount (NLe): ");
         BigDecimal amount = input.nextBigDecimal();
         input.nextLine(); //cleanup
         return amount;

      } catch (InputMismatchException e) {
         input.nextLine(); //Clear the invalid text from scanner
         System.out.println("Invalid input. Try again!");
         return null;
      }
   }

   //DEPOSIT FORM FOR OTHER
   public OtherDepositData otherDepositForm () {
      System.out.println("\n DEPOSIT TO OTHER");
      System.out.println("===================");

      try {

         System.out.print("Recipient Account No: ");
         int accNum = input.nextInt();

         System.out.print("Amount (NLe): ");
         BigDecimal amount = input.nextBigDecimal();
         input.nextLine(); //cleanup

         return new OtherDepositData(accNum, amount);

      } catch (InputMismatchException e) {
         input.nextLine(); //Clear the invalid text from scanner
         System.out.println("Invalid input. Try again!");
         return null;
      }
   }

   //BALANCE UPDATE AFTER A TRANSACTION
   public void balanceUpdate (BigDecimal newBalance, String name, BigDecimal amount, String transactionType, String direction) {
      //Call helper method
      FormatResult formats = getCurrencyAndDate();

      System.out.println("Dear " + name + ", you have successfully " + transactionType + " (NLe:" + formats.currencyFormatter().format(amount) + ") " + direction + " your account.");
      System.out.println("Your new Balance is NLe:" + formats.currencyFormatter().format(newBalance));
      System.out.println("Date: " + formats.formattedDate());
   }

   //JUST CHECKING BALANCE WITHOUT ANY TRANSACTION
   public void balanceUpdate (String name, BigDecimal amount) {
      //Call helper method
      FormatResult formats = getCurrencyAndDate();

      System.out.println("\nMESSAGE");
      System.out.println("Dear " + name + ", your current balance is NLe: " + formats.currencyFormatter.format(amount));
      System.out.println("Date: " + formats.formattedDate());
   }

   //CURRENCY AND DATE FORMAT (helper method)
   public FormatResult getCurrencyAndDate () {
      //Define money format patter: #,##0.00
      DecimalFormat df = new DecimalFormat("#,##0.00");

      //Get the current date and time
      LocalDateTime now = LocalDateTime.now();
      //Format the date and time to human readable
      DateTimeFormatter dateWriter = DateTimeFormatter.ofPattern("E, dd-MMM-yyyy HH:mm:ss");
      String formattedDate = now.format(dateWriter);

      //Return both currency and date format
      return new FormatResult(df, formattedDate);
   }
}
