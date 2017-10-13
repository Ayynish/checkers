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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Anish
 */
public class ChooseSessionController implements Initializable {
    
    private int x;
    
    @FXML
    TextField newSession;
    
    @FXML
    TextField oldSession;
    
    @FXML
    public void loadSession() {
        try {            
            File myFile = new File(oldSession.getText().trim().toUpperCase() + ".txt");
            
            if (!myFile.exists())
                throw new FileNotFoundException();
            
            BufferedReader br = new BufferedReader(new FileReader(myFile.getAbsoluteFile()));

            String s;
            
            while ((s = br.readLine()) != null) {
                String[] state = s.split(":");
                
                GameState.filename = myFile.getName();
                
                // Tile Color
                if (state[0].equalsIgnoreCase("D"))
                    GameState.tilesColor = Color.DARKGRAY;
                else if (state[0].equalsIgnoreCase("B"))
                    GameState.tilesColor = Color.BLACK;
                else if (state[0].equals("O"))
                    GameState.tilesColor = Color.DARKORANGE;
                else
                    GameState.tilesColor = Color.TAN;
                
                // Piece Colors
                if (state[1].equalsIgnoreCase("R")) {
                    GameState.team1Color = Color.RED;
                    GameState.team1KingColor = Color.DARKRED;
                    
                    if (state[2].equalsIgnoreCase("G")) {
                        GameState.team2Color = Color.GREEN;
                        GameState.team2Color = Color.DARKGREEN;
                    } else if (state[2].equals("B")) {
                        GameState.team2Color = Color.SKYBLUE;
                        GameState.team2Color = Color.DARKBLUE;
                    } else {
                        GameState.team2Color = Color.BLACK;
                        GameState.team2Color = Color.WHITESMOKE;
                    }
                } else if (state[1].equalsIgnoreCase("G")) {
                    GameState.team1Color = Color.GREEN;
                    GameState.team1Color = Color.DARKGREEN;
                    
                    if (state[2].equalsIgnoreCase("R")) {
                        GameState.team2Color = Color.RED;
                        GameState.team2Color = Color.DARKRED;
                    } else if (state[2].equals("B")) {
                        GameState.team2Color = Color.SKYBLUE;
                        GameState.team2Color = Color.DARKBLUE;
                    } else {
                        GameState.team2Color = Color.BLACK;
                        GameState.team2Color = Color.WHITESMOKE;
                    }
                } else if (state[1].equals("B")) {
                    GameState.team1Color = Color.SKYBLUE;
                    GameState.team1Color = Color.DARKBLUE;
                    
                    if (state[2].equalsIgnoreCase("G")) {
                        GameState.team2Color = Color.GREEN;
                        GameState.team2Color = Color.DARKGREEN;
                    } else if (state[2].equals("R")) {
                        GameState.team2Color = Color.RED;
                        GameState.team2Color = Color.DARKRED;
                    } else {
                        GameState.team2Color = Color.BLACK;
                        GameState.team2Color = Color.WHITESMOKE;
                    }
                } else {
                    GameState.team1Color = Color.BLACK;
                    GameState.team1Color = Color.WHITESMOKE;
                    
                    if (state[2].equalsIgnoreCase("G")) {
                        GameState.team2Color = Color.GREEN;
                        GameState.team2Color = Color.DARKGREEN;
                    } else if (state[2].equals("R")) {
                        GameState.team2Color = Color.RED;
                        GameState.team2Color = Color.DARKRED;
                    } else {
                        GameState.team2Color = Color.SKYBLUE;
                        GameState.team2Color = Color.DARKBLUE;
                    }
                }
                
                // WORK IN PROGRESS
                /*
                GameState.team1Pieces = Integer.parseInt(state[3]);
                GameState.team2Pieces = Integer.parseInt(state[4]);
                
                // Check for Dead
                String[] team1Dead = state[5].split(";");                
                for (int i = 0; i < team1Dead.length; i++)
                    GameState.team1Dead.add(Integer.parseInt(team1Dead[i]));
                
                String[] team2Dead = state[6].split(";");
                for (int i = 0; i < team2Dead.length; i++)
                    GameState.team2Dead.add(Integer.parseInt(team2Dead[i]));
                
                // Check for King
                String[] team1King = state[7].split("-");                
                for (int i = 0; i < team1King.length; i+=2)
                    GameState.team1King.put(Integer.parseInt(team1King[i]), Boolean.valueOf(team1King[i+1]));
                
                String[] team2King = state[8].split("-");                
                for (int i = 0; i < team2King.length; i+=2)
                    GameState.team2King.put(Integer.parseInt(team2King[i]), Boolean.valueOf(team2King[i+1]));
                */
                
            }
            
            StartGame.show2();
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        
    }
    
    @FXML
    public void createSession() {
        
            File myFile = new File(newSession.getText().trim().toUpperCase());
            
            createFile(myFile, myFile.getName());
            
            StartGame.show3();
    }
    
    public void createFile(File file, String filename) {
        try {
            file = new File(filename + x);
            File fileTest = new File(filename + x + ".txt");
            
            if (!fileTest.exists()) {
                fileTest.createNewFile();                
                GameState.filename = file.getName();
            } else {                
                x++;
                createFile(file, filename);
            }
            
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}