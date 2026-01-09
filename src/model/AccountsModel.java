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
      //Load all accounts from a file
      this.accounts = loadObjectFromFile();
   }

   //GETTER
   public List<BankModel> getAllAccounts () {
      return accounts;
   }

   //CREATE NEW BANK ACCOUNT
   public BankModel createNewAccount (String name, String address, String  phone) {
      //Generate random account number
      int accountNumber = new Random().nextInt(1000) + 10000;

      //Initial balance
      BigDecimal initialBalance= BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP);

      //Create an object
      BankModel newAccount = new BankModel(name, accountNumber, initialBalance, address, phone);

      //Save a new Account to list
      this.accounts.add(newAccount);

      return newAccount;
   }

   //SAVE ACCOUNT OBJECTS
   public void saveObjectToFile () throws IOException {
      try (ObjectOutputStream objSave = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
         objSave.writeObject(this.accounts);
      } //an error will be caught by the controller
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

   //Find a specific account to deposit/withdraw based on user login (phone)
   public BankModel findAccount (String phone) {
      //Loop through the lists of accounts
      for (BankModel account : this.accounts) {
         if (account.getPhone().equalsIgnoreCase(phone)) {
            return account; //Specific account found
         }
      }
      return null; //account does not exist
   }
}
