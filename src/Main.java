import controller.BankController;
import model.AccountsModel;
import view.BankView;

public class Main {
   public static void main(String[] args) {
      BankView bankView = new BankView();
      AccountsModel accountsList = new AccountsModel();
      BankController bankController = new BankController(bankView, accountsList);

      bankController.start();
   }
}