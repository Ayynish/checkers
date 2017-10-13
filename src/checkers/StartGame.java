/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Anish
 */
public class StartGame extends Application {
    
    static Stage stage1 = new Stage();
    static Stage stage2 = new Stage();
    static Stage stage3 = new Stage();
    
    static ChooseSessionController w1;
    static CheckersController w2;
    static SetUpController w3;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader r1 = new FXMLLoader(getClass().getResource("ChooseSession.fxml"));
        FXMLLoader r2 = new FXMLLoader(getClass().getResource("Checkers.fxml"));
        FXMLLoader r3 = new FXMLLoader(getClass().getResource("SetUp.fxml"));
        
        Parent root1 = r1.load();
        Parent root2 = r2.load();
        Parent root3 = r3.load();
        
        w1 = r1.getController();
        w2 = r2.getController();
        w3 = r3.getController();
        
        Scene scene1 = new Scene(root1);
        Scene scene2 = new Scene(root2);
        Scene scene3 = new Scene(root3);
        
        stage1.setTitle("AJ's Checkers :: Start Screen");
        stage1.setScene(scene1);
        
        stage2.setTitle("AJ's Checkers :: Game Screen");
        stage2.setScene(scene2);
        
        stage3.setTitle("AJ's Checkers :: Set Up");
        stage3.setScene(scene3);
                
        show1();
    }
    
    public static void show1() {
        stage1.show();
        stage2.hide();
        stage3.hide();
    }
    
    public static void show2() {
        stage1.hide();
        stage2.show();
        stage3.hide();
        
        stage2.setTitle("AJ's Checkers :: Session: " + GameState.filename);
    }
    
    public static void show3() {
        stage1.hide();
        stage2.hide();
        stage3.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}