/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Anish
 */
public class SetUpController implements Initializable {
    
    @FXML
    RadioButton tileGray;
    
    @FXML
    RadioButton tileBlack;
    
    @FXML
    RadioButton tileOrange;
    
    @FXML
    RadioButton tileTan;
    
    @FXML
    RadioButton team1Red;
    
    @FXML
    RadioButton team1Green;
    
    @FXML
    RadioButton team1Blue;
    
    @FXML
    RadioButton team1Black;
    
    @FXML
    RadioButton team2Red;
    
    @FXML
    RadioButton team2Green;
    
    @FXML
    RadioButton team2Blue;
    
    @FXML
    RadioButton team2Black;
    
    ToggleGroup tileColor = new ToggleGroup();
    ToggleGroup team1Color = new ToggleGroup();
    ToggleGroup team2Color = new ToggleGroup();
    
    @FXML
    public void startGame() {        
        
        if (tileColor.getSelectedToggle() != null && team1Color.getSelectedToggle() != null && team2Color.getSelectedToggle() != null) {
            
            try {
            File myFile = new File(GameState.filename + ".txt");
                
                if (!myFile.exists()) {
                    throw new Error();
                }
                
                BufferedWriter bw = new BufferedWriter(new FileWriter(myFile.getAbsoluteFile()));
                
                bw.write(tileColor.getSelectedToggle().getUserData() +
                        ":" + team1Color.getSelectedToggle().getUserData() +
                        ":" + team2Color.getSelectedToggle().getUserData());
                
                bw.close();
                
                switch ((char)tileColor.getSelectedToggle().getUserData()) {
                    case 'D':   GameState.tilesColor = Color.DARKGRAY;
                    break;
                    
                    case 'B':   GameState.tilesColor = Color.BLACK;
                                break;
                    
                    case 'O':   GameState.tilesColor = Color.DARKORANGE;
                                break;
                    
                    case 'T':   GameState.tilesColor = Color.TAN;
                                break;
                    
                    default:    GameState.tilesColor = Color.FIREBRICK;
                                break;
                }
                
                switch ((char)team1Color.getSelectedToggle().getUserData()) {
                    case 'R':   GameState.team1Color = Color.RED;
                                GameState.team1KingColor = Color.DARKRED;
                                break;
                                
                    case 'G':   GameState.team1Color = Color.GREEN;
                                GameState.team1KingColor = Color.DARKGREEN;
                                break;
                                
                    case 'B':   GameState.team1Color = Color.SKYBLUE;
                                GameState.team1KingColor = Color.DARKBLUE;
                                break;
                                
                    case 'X':   GameState.team1Color = Color.BLACK;
                                GameState.team1KingColor = Color.WHITESMOKE;
                                break;
                                
                    default:    GameState.team1Color = Color.AQUA;
                                GameState.team1KingColor = Color.AQUAMARINE;
                                break;
                }
                
                switch ((char)team2Color.getSelectedToggle().getUserData()) {
                    case 'R':   GameState.team2Color = Color.RED;
                                GameState.team2KingColor = Color.DARKRED;
                                break;
                                
                    case 'G':   GameState.team2Color = Color.GREEN;
                                GameState.team2KingColor = Color.DARKGREEN;
                                break;
                                
                    case 'B':   GameState.team2Color = Color.SKYBLUE;
                                GameState.team2KingColor = Color.DARKBLUE;
                                break;
                                
                    case 'X':   GameState.team2Color = Color.BLACK;
                                GameState.team2KingColor = Color.WHITESMOKE;
                                break;
                                
                    default:    GameState.team2Color = Color.AQUA;
                                GameState.team2KingColor = Color.AQUAMARINE;
                                break;
                }
                
                StartGame.show2();
                
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
            
        }        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tileGray.setUserData('D');
        tileBlack.setUserData('B');
        tileOrange.setUserData('O');
        tileTan.setUserData('T');
        
        team1Red.setUserData('R');
        team1Green.setUserData('G');
        team1Blue.setUserData('B');
        team1Black.setUserData('X');
        
        team2Red.setUserData('R');
        team2Green.setUserData('G');
        team2Blue.setUserData('B');
        team2Black.setUserData('X');
        
        tileGray.setToggleGroup(tileColor);
        tileBlack.setToggleGroup(tileColor);
        tileOrange.setToggleGroup(tileColor);
        tileTan.setToggleGroup(tileColor);        
        
        team1Red.setToggleGroup(team1Color);
        team1Green.setToggleGroup(team1Color);
        team1Blue.setToggleGroup(team1Color);
        team1Black.setToggleGroup(team1Color);        
        
        team2Red.setToggleGroup(team2Color);
        team2Green.setToggleGroup(team2Color);
        team2Blue.setToggleGroup(team2Color);
        team2Black.setToggleGroup(team2Color);
    }    
    
}