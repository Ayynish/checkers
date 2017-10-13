/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;

/**
 *
 * @author Anish
 */
public class GameState {
    
    public static String filename;
    
    public static Color tilesColor = Color.DARKGRAY;
    
    public static Color team1Color = Color.RED;
    public static Color team2Color = Color.BLACK;
    
    public static Color team1KingColor = Color.DARKRED;
    public static Color team2KingColor = Color.WHITESMOKE;
    
    public static final int TEAMSIZE = 8;
    
    public static int team1Pieces = 8;
    public static int team2Pieces = 8;
    
    public static ArrayList<Integer> team1Dead = new ArrayList<Integer>();
    public static ArrayList<Integer> team2Dead = new ArrayList<Integer>();
    
    public static HashMap<Integer,Boolean> team1King = new HashMap<Integer,Boolean>();
    public static HashMap<Integer,Boolean> team2King = new HashMap<Integer,Boolean>();
    
    public static double[][] team1Pos;
    public static double[][] team2Pos;
    
    public static boolean team1turn = true;
    
}