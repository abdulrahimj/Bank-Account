package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountsModel {
   private List<BankModel> accounts;
   private final String FILE_PATH = "bank_data.dat";

   public AccountsModel () {
      this.accounts = new ArrayList<>();
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

   //SAVE ACCOUNT OBJECT
   public void saveObjectToFile () throws IOException {
      try (ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
         obj.writeObject(this.accounts);
      } //error will be caught by the controller
   }

   //Find a specific account Number to deposit/withdraw
   public BankModel findAccount (BankModel accountNumber) {
      //Loop through the lists of accounts
      System.out.println("TEST" + accountNumber);
      for (BankModel account : this.accounts) {
         if (account == accountNumber) {
            System.out.println("TEST" + account);
            return account; //Specific account found
         }
      }
      return null; //account does not exist
   }
}
