package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankModel implements Serializable {
   private static final long serialVersionUID = 1L;
   private String accountHolder;
   private final int accountNumber;
   private BigDecimal balance;
   private String address;
   private String phone;

   public BankModel(String accountHolder, int accountNumber, BigDecimal balance, String address, String phone) {
      this.accountHolder = accountHolder;
      this.accountNumber = accountNumber;
      this.balance = balance;
      this.address = address;
      this.phone = phone;
   }

   //DEPOSIT
   public void deposit (BigDecimal amount) {
      //When deposit amount is less than or equal to zero.
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("Deposit must be greater than zero.");
      }

      //Add deposit amount to account
      this.balance= this.balance.add(amount);
   }

   //WITHDRAW
   public void withdraw (BigDecimal amount) {
      //When withdrawal amount is less than or equal zero
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("Withdrawal must be greater than zero.");
      }

      //When withdrawal amount is greater than balance
      if (amount.compareTo(this.balance) > 0) {
         //Custom exception
         throw new InsufficientFundsException("Insufficient funds.");
      }

      //Subtract withdrawal amount from balance
      this.balance = this.balance.subtract(amount);
   }

   public String getAccountHolder() {
      return accountHolder;
   }

   public int getAccountNumber() {
      return accountNumber;
   }

   public BigDecimal getBalance() {
      return balance;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   @Override
   public String toString() {
      return "BankModel{" +
              "accountHolder='" + accountHolder + '\'' +
              ", accountNumber=" + accountNumber +
              ", balance=" + balance +
              ", phone=" + phone +
              '}';
   }
}
