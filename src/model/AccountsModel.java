package model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountsModel {
   private List<BankModel> accounts;
   private final String FILE_PATH = "bank_data.dat";

   public AccountsModel () {
      //Load all accounts from file
      this.accounts = loadObjectFromFile();
   }

   //CREATE NEW BANK ACCOUNT
   public BankModel createNewAccount (String name, String address, String  phone) {
      //Generate random account number
      int accountNumber = new Random().nextInt(1000) + 10000;

      //Initial balance
      BigDecimal initialBalance= BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP);

      //Create object
      BankModel newAccount = new BankModel(name, accountNumber, initialBalance, address, phone);

      //Save new Account to list
      this.accounts.add(newAccount);

      return newAccount;
   }

   //SAVE ACCOUNT OBJECTS
   public void saveObjectToFile () throws IOException {
      try (ObjectOutputStream objSave = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
         objSave.writeObject(this.accounts);
      } //error will be caught by the controller
   }

   //LOAD ACCOUNT OBJECTS
   @SuppressWarnings("unchecked")
   public List<BankModel> loadObjectFromFile() {
      File file = new File(FILE_PATH);

      //If file does not exist, return a new empty list
      //This happens at the very first time when I run the app
      if (!file.exists()) {
         return new ArrayList<>();
      }

      try (ObjectInputStream objLoad = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
         return (List<BankModel>) objLoad.readObject();
      } catch (IOException  | ClassNotFoundException e) {
         System.out.println("No account (Starting fresh): " + e.getMessage());
         return new ArrayList<>();
      }
   }

   //Find a specific account Number to deposit/withdraw
   public BankModel findAccount (BankModel accountNumber) {
      //Loop through the lists of accounts
      System.out.println("TEST" + accountNumber);
      for (BankModel account : this.accounts) {
         if (account == accountNumber) {
            return account; //Specific account found
         }
      }
      return null; //account does not exist
   }
}
