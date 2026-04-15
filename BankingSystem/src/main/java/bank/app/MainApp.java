package bank.app;

import bank.controller.MainController;
import bank.repository.AccountRepository;
import bank.repository.AccountRepositoryInterface;
import bank.service.BankService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application{

    //Single shared instances injected into controllers
    private static AccountRepositoryInterface repository;
    private static BankService bankService;

    @Override
    public void init(){
        //Wire up the layers at once - all controllers share these
        repository = new AccountRepository();
        bankService = new BankService(repository);
    }

    @Override
    public void start(Start stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //Pass service to main controller
        MainController controller = loader.getController();
        controller.setBankService(bankService);

        stage.setTitle("Banking System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static  void main(String[] args) {
        launch(args);
    }
}
