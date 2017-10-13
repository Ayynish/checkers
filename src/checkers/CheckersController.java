/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Anish
 */
public class CheckersController implements Initializable {
    
    private boolean isTeam1Turn = true;
    
    private int turns;
    
    private final int ROWS = 8;
    private final int COLS = 4;
    
    private double x;
    private double y;
    
    private double translateX;
    private double translateY;
    
    private double offsetX;
    private double offsetY;
    
    private double newTranslateX;
    private double newTranslateY;
    
    private double initCenterX;
    private double initCenterY;
    
    private double newCenterX;
    private double newCenterY;
    
    private double differenceX;
    private double differenceY;
    
    private int deadTeam1Num = -1;
    private int deadTeam2Num = -1;
    
    @FXML
    Label winner;
    
    @FXML
    StackPane endScreen;
    
    @FXML
    GridPane board;
    
    @FXML
    Button reset;
    
    @FXML
    Rectangle currentTeam;
    
    Rectangle[] tiles = new Rectangle[32];
    
    ArrayList<Checker> team1 = new ArrayList<Checker>();
    ArrayList<Checker> team2 = new ArrayList<Checker>();
    
    Checker enemy;    
       
    EventHandler<MouseEvent> onPressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof Checker) {
                
                updateGameState();
                
                if (deadTeam1Num != -1) {
                    if (!GameState.team1Dead.contains(deadTeam1Num - 1))
                        GameState.team1Dead.add(deadTeam1Num - 1);
                    
                    deadTeam1Num = -1;
                }
                
                if (deadTeam2Num != -1) {
                    if (!GameState.team2Dead.contains(deadTeam2Num - 1))
                        GameState.team2Dead.add(deadTeam2Num - 1);
                    
                    deadTeam2Num = -1;
                }
                
                GameState.team1turn = isTeam1Turn;
                
                Checker piece = (Checker)event.getSource();
                
                if (piece.getTeam() == isTeam1Turn) {                    
                    Bounds bounds = piece.localToScene(piece.getBoundsInLocal());
                    
                    initCenterX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    initCenterY = getAverage(bounds.getMaxY(), bounds.getMinY());
                    
                    // coordinates onclick
                    x = event.getSceneX();
                    y = event.getSceneY();                     
                    
                    // translation from initial position
                    translateX = piece.getTranslateX();
                    translateY = piece.getTranslateY();
                }
            }
        }
    
    };
    
    EventHandler<MouseEvent> onDragEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof Checker) {
                
                Checker piece = (Checker)event.getSource();
                
                if (piece.getTeam() == isTeam1Turn) {
                    // offset from initial coordinates onclick                    
                    offsetX = event.getSceneX() - x;
                    offsetY = event.getSceneY() - y;
                    
                    // new translation from initial position after offset
                    newTranslateX = translateX + offsetX;
                    newTranslateY = translateY + offsetY;
                    
                    // set new translation values
                    piece.setTranslateX(newTranslateX);
                    piece.setTranslateY(newTranslateY);
                               
                }
            }
        }
    
    };
    
    EventHandler<MouseEvent> onReleaseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //System.out.println(x);
            //System.out.println(y);
            if (event.getSource() instanceof Checker) {
                
                Checker piece = (Checker)event.getSource();
                
                if (piece.getTeam() == isTeam1Turn) {
                    Bounds bounds = piece.localToScene(piece.getBoundsInLocal());
                    
                    newCenterX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    newCenterY = getAverage(bounds.getMaxY(), bounds.getMinY());
                    
                    differenceX = newCenterX - initCenterX;
                    differenceY = newCenterY - initCenterY;
                    
                    if (piece.getTeam()) { // top team
                        if (initCenterX >= 75 && initCenterX <= 325) {
                                                        
                            if (differenceY > 25 && differenceY < 75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                        
                                    if (!checkForAlly(piece, 50, 50)) {
                                        if (!checkForEnemy(piece, 50, 50)) {
                                            piece.setTranslateX(translateX + 50);
                                            piece.setTranslateY(translateY + 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, 100, 100) && !checkForEnemy(piece, 100, 100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX + 100);
                                            piece.setTranslateY(translateY + 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                           
                                } else if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                        
                                    if (!checkForAlly(piece, -50, 50)) {
                                        if (!checkForEnemy(piece, -50, 50)) {
                                            piece.setTranslateX(translateX - 50);
                                            piece.setTranslateY(translateY + 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, -100, 100) && !checkForEnemy(piece, -100, 100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX - 100);
                                            piece.setTranslateY(translateY + 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else  // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                               
                            
                            } else if (differenceY < -25 && differenceY > -75) {
                                // KING PIECE
                                if (piece.getType()) {
                                    
                                    if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                        if (!checkForAlly(piece, 50, -50)) {
                                            if (!checkForEnemy(piece, 50, -50)) {
                                                piece.setTranslateX(translateX + 50);
                                                piece.setTranslateY(translateY - 50);
                                                                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, 100, -100) && !checkForEnemy(piece, 100, -100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX + 100);
                                                piece.setTranslateY(translateY - 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                        
                                        if (!checkForAlly(piece, -50, -50)) {
                                            
                                            if (!checkForEnemy(piece, -50, -50)) {
                                                piece.setTranslateX(translateX - 50);
                                                piece.setTranslateY(translateY - 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, -100, -100) && !checkForEnemy(piece, -100, -100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX - 100);
                                                piece.setTranslateY(translateY - 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else  // moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                    
                                } else
                                    setOriginalPos(piece);
                                
                            } else  // moves pieces back to original position and keeps their turn
                                setOriginalPos(piece);
                                
                            
                        } else if (initCenterX < 75) {
                            
                            if (differenceY > 25 && differenceY < 75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, 50, 50)) {
                                        if (!checkForEnemy(piece, 50, 50)) {
                                            piece.setTranslateX(translateX + 50);
                                            piece.setTranslateY(translateY + 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, 100, 100) && !checkForEnemy(piece, 100, 100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX + 100);
                                            piece.setTranslateY(translateY + 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else  // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                    
                            
                            } else if (differenceY < -25 && differenceY > -75) {
                                // KING PIECE
                                if (piece.getType()) { // ensures pieces are moved within vertical boundaries
                                
                                    if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                        if (!checkForAlly(piece, 50, -50)) {
                                            if (!checkForEnemy(piece, 50, -50)) {
                                                piece.setTranslateX(translateX + 50);
                                                piece.setTranslateY(translateY - 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, 100, -100) && !checkForEnemy(piece, 100, -100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX + 100);
                                                piece.setTranslateY(translateY - 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else // moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                    
                                } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                            
                            } else  // moves pieces back to original position and keeps their turn
                                setOriginalPos(piece);
                            
                        } else if (initCenterX > 325) {
                                                        
                            if (differenceY > 25 && differenceY < 75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, -50, 50)) {
                                        if (!checkForEnemy(piece, -50, 50)) {
                                            piece.setTranslateX(translateX - 50);
                                            piece.setTranslateY(translateY + 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, -100, 100) && !checkForEnemy(piece, -100, 100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX - 100);
                                            piece.setTranslateY(translateY + 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                    
                            } else if (differenceY < -25 && differenceY > -75) {
                                // KING PIECE
                                if (piece.getType()) { // ensures pieces are moved within vertical boundaries
                                
                                    if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                        
                                        if (!checkForAlly(piece, -50, -50)) {
                                            
                                            if (!checkForEnemy(piece, -50, -50)) {
                                                
                                                piece.setTranslateX(translateX - 50);
                                                piece.setTranslateY(translateY - 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, -100, -100) && !checkForEnemy(piece, -100, -100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX - 100);
                                                piece.setTranslateY(translateY - 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else// moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                    
                                } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                            
                            } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                
                        }
                        
                        
                    } else { // BOTTOM TEAM BOTTOM TEAM BOTTOM TEAM
                        if (initCenterX >= 75 && initCenterX <= 325) {
                                                   
                            if (differenceY < -25 && differenceY > -75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, 50, -50)) {
                                        if (!checkForEnemy(piece, 50, -50)) {
                                            piece.setTranslateX(translateX + 50);
                                            piece.setTranslateY(translateY - 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, 100, -100) && !checkForEnemy(piece, 100, -100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX + 100);
                                            piece.setTranslateY(translateY - 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, -50, -50)) {
                                        if (!checkForEnemy(piece, -50, -50)) {
                                            piece.setTranslateX(translateX - 50);
                                            piece.setTranslateY(translateY - 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, -100, -100) && !checkForEnemy(piece, -100, -100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX - 100);
                                            piece.setTranslateY(translateY - 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else  // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                    
                            } else if (differenceY > 25 && differenceY < 75) {
                            
                                if (piece.getType()) {
                                
                                    if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                        
                                        if (!checkForAlly(piece, 50, 50)) {
                                            if (!checkForEnemy(piece, 50, 50)) {
                                                piece.setTranslateX(translateX + 50);
                                                piece.setTranslateY(translateY + 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, 100, 100) && !checkForEnemy(piece, 100, 100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX + 100);
                                                piece.setTranslateY(translateY + 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                           
                                    } else if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                        
                                        if (!checkForAlly(piece, -50, 50)) {
                                            if (!checkForEnemy(piece, -50, 50)) {
                                                piece.setTranslateX(translateX - 50);
                                                piece.setTranslateY(translateY + 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, -100, 100) && !checkForEnemy(piece, -100, 100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX - 100);
                                                piece.setTranslateY(translateY + 100);
                                                
                                                endTurn();
                                            } else
                                            setOriginalPos(piece);
                                            
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else  // moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                } else
                                    setOriginalPos(piece);
                            
                            } else  // moves pieces back to original position and keeps their turn
                                setOriginalPos(piece);
                                
                        } else if (initCenterX < 75) {
                            
                            if (differenceY < -25 && differenceY > -75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, 50, -50)) {
                                        if (!checkForEnemy(piece, 50, -50)) {
                                            piece.setTranslateX(translateX + 50);
                                            piece.setTranslateY(translateY - 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, 100, -100) && !checkForEnemy(piece, 100, -100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX + 100);
                                            piece.setTranslateY(translateY - 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                    
                            } else if (differenceY > 25 && differenceY < 75) {
                                
                                if (piece.getType()) {
                                    if (differenceX > 25 && differenceX < 75) { // ensures pieces are moved within horizontal boundaries
                                        if (!checkForAlly(piece, 50, 50)) {
                                            if (!checkForEnemy(piece, 50, 50)) {
                                                piece.setTranslateX(translateX + 50);
                                                piece.setTranslateY(translateY + 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, 100, 100) && !checkForEnemy(piece, 100, 100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX + 100);
                                                piece.setTranslateY(translateY + 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else  // moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                } else // moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece); 
                            } else
                                setOriginalPos(piece);
                          
                        } else if (initCenterX > 325) {
                            
                            if (differenceY < -25 && differenceY > -75) { // ensures pieces are moved within vertical boundaries
                                
                                if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                    if (!checkForAlly(piece, -50, -50)) {
                                        if (!checkForEnemy(piece, -50, -50)) {
                                            piece.setTranslateX(translateX - 50);
                                            piece.setTranslateY(translateY - 50);
                                            
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else if (!checkForAlly(piece, -100, -100) && !checkForEnemy(piece, -100, -100)) {
                                            killEnemy();
                                            piece.setTranslateX(translateX - 100);
                                            piece.setTranslateY(translateY - 100);
                                                
                                            piece.setType(isKing(piece, piece.getTeam()));
                                            endTurn();
                                        } else
                                            setOriginalPos(piece);
                                    } else 
                                        setOriginalPos(piece);
                                                                            
                                } else// moves pieces back to original position and keeps their turn
                                    setOriginalPos(piece);
                                    
                            } else if (differenceY > 25 && differenceY < 75) {
                            
                                if (piece.getType()) {
                                
                                    if (differenceX < -25 && differenceX > -75) { // ensures pieces are moved within horizontal boundaries
                                        if (!checkForAlly(piece, -50, 50)) {
                                            if (!checkForEnemy(piece, -50, 50)) {
                                                piece.setTranslateX(translateX - 50);
                                                piece.setTranslateY(translateY + 50);
                                            
                                                endTurn();
                                            } else if (!checkForAlly(piece, -100, 100) && !checkForEnemy(piece, -100, 100)) {
                                                killEnemy();
                                                piece.setTranslateX(translateX - 100);
                                                piece.setTranslateY(translateY + 100);
                                                
                                                endTurn();
                                            } else
                                                setOriginalPos(piece);
                                            
                                        } else 
                                            setOriginalPos(piece);
                                                                            
                                    } else // moves pieces back to original position and keeps their turn
                                        setOriginalPos(piece);
                                    
                                } else
                                    setOriginalPos(piece);
                                
                            } else // moves pieces back to original position and keeps their turn
                                setOriginalPos(piece);                            
                        }
                    } 
                }                 
            }
        }           
    };
    
    public void setOriginalPos(Checker piece) {
        piece.setTranslateX(translateX);
        piece.setTranslateY(translateY);
    }
    
    public void killEnemy() {
        if (isTeam1Turn) {
            for (int i = 0; i < team2.size(); i++) {
                if (team2.get(i).equals(enemy)) {
                    board.getChildren().remove(team2.get(i));
                    deadTeam2Num = team2.get(i).getPieceNum() + 1;
                    team2.remove(i);
                    
                    if (team2.size() == 0) {
                        winner.setText("P1 won in " + turns + " turns!");
                        endScreen.setTranslateY(0);
                    }
                }
            }
        } else {
            for (int i = 0; i < team1.size(); i++) {
                if (team1.get(i).equals(enemy)) {
                    board.getChildren().remove(team1.get(i));
                    deadTeam1Num = team1.get(i).getPieceNum() + 1;
                    team1.remove(i);
                    
                    if (team1.size() == 0) {
                        winner.setText("P2 won in " + turns + " turns!");
                        endScreen.setTranslateY(0);
                    }
                }
            }
        }
    }
    
    public boolean checkForEnemy(Checker piece, double offsetX, double offsetY) {
        if (isTeam1Turn) {
            for (int i = 0; i < team2.size(); i++) {
                if (team2.get(i) != null) {
                    Bounds bounds = team2.get(i).localToScene(team2.get(i).getBoundsInLocal());
                    double centerX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    double centerY = getAverage(bounds.getMaxY(), bounds.getMinY());
                    
                    if (initCenterX+offsetX < 400 && initCenterX+offsetX > 0 && initCenterY+offsetY < 400 && initCenterY+offsetY > 0){
                        if (initCenterX+offsetX == centerX && initCenterY+offsetY == centerY) {
                            enemy = team2.get(i);
                            return true;
                        }
                    } else
                        return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < team1.size(); i++) {
                if (team1.get(i) != null) {
                    Bounds bounds = team1.get(i).localToScene(team1.get(i).getBoundsInLocal());
                    double centerX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    double centerY = getAverage(bounds.getMaxY(), bounds.getMinY());
                
                    if (initCenterX+offsetX < 400 && initCenterX+offsetX > 0 && initCenterY+offsetY < 400 && initCenterY+offsetY > 0){
                        if (initCenterX+offsetX == centerX && initCenterY+offsetY == centerY) {
                            enemy = team1.get(i);
                            return true;
                        }
                    } else
                        return true;
                }
            }
            return false;
        }
    }
    
    public boolean checkForAlly(Checker piece, double offsetX, double offsetY) {
        if (isTeam1Turn) {
            for (int i = 0; i < team1.size(); i++) { // checks for pieces on the same team
                if (team1.get(i) != null && !team1.get(i).equals(piece)) {
                    Bounds bounds = team1.get(i).localToScene(team1.get(i).getBoundsInLocal());
                    double centerX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    double centerY = getAverage(bounds.getMaxY(), bounds.getMinY());
                    if (initCenterX+offsetX < 400 && initCenterX+offsetX > 0 && initCenterY+offsetY < 400 && initCenterY+offsetY > 0){
                        if (initCenterX+offsetX == centerX && initCenterY+offsetY == centerY)
                            return true;
                    } else
                        return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < team2.size(); i++) { // checks for pieces on the same team
                if (team2.get(i) != null && !team2.get(i).equals(piece)) {
                    Bounds bounds = team2.get(i).localToScene(team2.get(i).getBoundsInLocal());
                    double centerX = getAverage(bounds.getMaxX(), bounds.getMinX());
                    double centerY = getAverage(bounds.getMaxY(), bounds.getMinY());
                    if (initCenterX+offsetX < 400 && initCenterX+offsetX > 0 && initCenterY+offsetY < 400 && initCenterY+offsetY > 0){
                        if (initCenterX+offsetX == centerX && initCenterY+offsetY == centerY)
                            return true;
                    } else
                        return true;
                }
            }
            return false;
        }
    }
    
    public boolean isKing(Checker piece, boolean team) {
        Bounds bounds = piece.localToScene(piece.getBoundsInLocal());
        
        double centerY = getAverage(bounds.getMaxY(), bounds.getMinY());
        
        if (piece.getType())
            return true;
        
        if (team && centerY == 375) {
            piece.setFill(GameState.team1KingColor);
            return true;
        } 
        
        if (!team && centerY == 25) {
            piece.setFill(GameState.team2KingColor);
            return true;
        }
        
        return false;
    }
    
    public void endTurn() {        
        isTeam1Turn = !isTeam1Turn;
        setCurrentTeam();
        
        turns++;
        
        if (team2.size() == 0) {
            System.out.println("Team 1 won in " + turns + " turns!");
        }
        
        if (team1.size() == 0) {
            System.out.println("Team 2 won in " + turns + " turns!");
        }
    }
    
    @FXML
    public void playGame() {
        board.setGridLinesVisible(false);
        setBoardTiles(GameState.tilesColor);
        newGame();
        updateGameState();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        board.setGridLinesVisible(true);
        endScreen.setTranslateY(2000);
    }
    
    public void setBoardTiles(Color fill) {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Rectangle(50, 50, fill);
        }
        
        // counts each iteration of the most nested loop
        int tile = 0;
        
        // sets up the different colored tiles
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (row%2 == 0) {
                    board.add(tiles[tile], col*2, row);
                } else {
                    board.add(tiles[tile], (col*2) + 1, row);
                }
                tile++;
            }
        }
    }

    // Resets the game
    @FXML
    public void resetGame() {
        turns = 0;
        team1.clear();
        team2.clear();
        board.getChildren().clear();
        
        GameState.team1Dead.clear();
        GameState.team2Dead.clear();
        
        GameState.team1King.clear();
        GameState.team2King.clear();
        
        setBoardTiles(GameState.tilesColor);
        
        isTeam1Turn = true;
        
        endScreen.setTranslateY(2000);
        
        newGame();
    }
    
    // Starts a new game
    public void newGame() {
        for (int i = 0; i < GameState.TEAMSIZE; i++) {
            team1.add(new Checker(25, GameState.team1Color));
            team1.get(i).setTeam(true);
            team1.get(i).setOnMousePressed(onPressEventHandler);
            team1.get(i).setOnMouseDragged(onDragEventHandler);
            team1.get(i).setOnMouseReleased(onReleaseEventHandler);
            
            team2.add(new Checker(25, GameState.team2Color));
            team2.get(i).setTeam(false);
            team2.get(i).setOnMousePressed(onPressEventHandler);
            team2.get(i).setOnMouseDragged(onDragEventHandler);
            team2.get(i).setOnMouseReleased(onReleaseEventHandler);
        }
        
        // counts each iteration of the most nested loop
        int checker = 0;
        
        // adds checker pieces to board
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                if (row%2 == 0) {
                    board.add(team1.get(checker), col*2, row);
                    team1.get(checker).setPieceNum(checker);
                    
                    board.add(team2.get(checker), (col * 2) + 1, (ROWS - 1) - row);
                    team2.get(checker).setPieceNum(checker);
                } else {
                    board.add(team1.get(checker), (col*2) + 1, row);
                    team1.get(checker).setPieceNum(checker);
                    
                    board.add(team2.get(checker), col*2, (ROWS - 1) - row);
                    team2.get(checker).setPieceNum(checker);
                }
                checker++;
            }
        }
        
        setCurrentTeam();
    }
    
    // Undo the last move
    @FXML
    public void undo() {
        turns--;
        deadTeam1Num = -1;
        deadTeam2Num = -1;
        
        team1.clear();
        team2.clear();
        board.getChildren().clear();
        
        setBoardTiles(GameState.tilesColor);
        
        isTeam1Turn = GameState.team1turn;
        
        for (int i = 0; i < GameState.team1Pieces; i++) {
            team1.add(new Checker(25, GameState.team1Color));
            team1.get(i).setTeam(true);
            team1.get(i).setOnMousePressed(onPressEventHandler);
            team1.get(i).setOnMouseDragged(onDragEventHandler);
            team1.get(i).setOnMouseReleased(onReleaseEventHandler);
        }
        
        for (int i = 0; i < GameState.team2Pieces; i++) {
            team2.add(new Checker(25, GameState.team2Color));
            team2.get(i).setTeam(false);
            team2.get(i).setOnMousePressed(onPressEventHandler);
            team2.get(i).setOnMouseDragged(onDragEventHandler);
            team2.get(i).setOnMouseReleased(onReleaseEventHandler);
        }
        
        // counts each iteration of the most nested loop
        int checker = 0;
        int checker1 = 0;
        int checker2 = 0;
        
        // adds checker pieces to board if new game
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 4; col++) {
                    if (row%2 == 0) {                        
                        if (!checkDead(GameState.team1Dead, checker)) {
                            board.add(team1.get(checker1), col*2, row);
                            team1.get(checker1).setPieceNum(checker);
                            checker1++;
                        }
                        
                        if (!checkDead(GameState.team2Dead, checker)) {
                            board.add(team2.get(checker2), (col * 2) + 1, (ROWS - 1) - row);
                            team2.get(checker2).setPieceNum(checker);
                            checker2++;
                        }
                        
                    } else {
                        if (!checkDead(GameState.team1Dead, checker)) {
                            board.add(team1.get(checker1), (col*2) + 1, row);
                            team1.get(checker1).setPieceNum(checker);
                            checker1++;
                        }
                        
                        if (!checkDead(GameState.team2Dead, checker)) {
                            board.add(team2.get(checker2), col*2, (ROWS - 1) - row);
                            team2.get(checker2).setPieceNum(checker);
                            checker2++;
                        }
                        
                    }
                    checker++;
                }
            }
            
            for (int i = 0; i < GameState.team1Pieces; i++) {
                
                if (GameState.team1Dead.contains(team1.get(i).getPieceNum()))
                    GameState.team1Dead.remove((Integer)team1.get(i).getPieceNum());
                
                team1.get(i).setTranslateX(GameState.team1Pos[0][i]);
                team1.get(i).setTranslateY(GameState.team1Pos[1][i]);
            }
            
            for (int i = 0; i < GameState.team2Pieces; i++) {
                
                if (GameState.team2Dead.contains(team2.get(i).getPieceNum()))
                    GameState.team2Dead.remove((Integer)team2.get(i).getPieceNum());
                
                team2.get(i).setTranslateX(GameState.team2Pos[0][i]);
                team2.get(i).setTranslateY(GameState.team2Pos[1][i]);
            }
            
            if (GameState.team1King.containsValue(true)) {
                for (int i = 0; i < GameState.team1Pieces; i++) {
                    if (GameState.team1King.containsKey(team1.get(i).getPieceNum()))
                        team1.get(i).setType(true);
                }
            }
            
            if (GameState.team2King.containsValue(true)) {
                for (int i = 0; i < GameState.team2Pieces; i++) {
                    if (GameState.team2King.containsKey(team2.get(i).getPieceNum()))
                        team2.get(i).setType(true);
                }
            }
        
        setCurrentTeam();
    }
    
    // Sets the fill value of the current team box
    public void setCurrentTeam() {
        if (isTeam1Turn)
            currentTeam.setFill(GameState.team1Color);
        else
            currentTeam.setFill(GameState.team2Color);
    }
    
    // Gets the average between any number of numbers
    public double getAverage (double... nums) {
        double average = 0;
        
        for (int i = 0; i < nums.length; i++)
            average += nums[i];
        
        return (double)average/nums.length;
    }
    
    // Checks if the checker at a position is dead or not
    public boolean checkDead(ArrayList<Integer> dead, int pos) {
        for (int i = 0; i < dead.size(); i++) {
            if (pos == dead.get(i))
                return true;
        }
        
        return false;
    }
    
    // Saves the current state of the board to the GameState class
    public void updateGameState() {
        GameState.team1Pieces = team1.size();
        GameState.team2Pieces = team2.size();
        
        GameState.team1Pos = new double[2][GameState.team1Pieces];
        GameState.team2Pos = new double[2][GameState.team2Pieces];
        
        for (int i = 0; i < GameState.team1Pos.length; i++) {
            for (int j = 0; j < GameState.team1Pos[i].length; j++) {
                if (i == 0)
                    GameState.team1Pos[i][j] = team1.get(j).getTranslateX();
                else
                    GameState.team1Pos[i][j] = team1.get(j).getTranslateY();
            }
        }
        
        for (int i = 0; i < GameState.team2Pos.length; i++) {
            for (int j = 0; j < GameState.team2Pos[i].length; j++) {
                if (i == 0)
                    GameState.team2Pos[i][j] = team2.get(j).getTranslateX();
                else
                    GameState.team2Pos[i][j] = team2.get(j).getTranslateY();
            }
        }
        
        for (int i = 0; i < GameState.team1Pieces; i++) {
            if (team1.get(i).getType())
                GameState.team1King.put(i, team1.get(i).getType());
        }
        
        for (int i = 0; i < GameState.team2Pieces; i++) {
            if (team2.get(i).getType())
                GameState.team2King.put(i, team2.get(i).getType());
        }
    }
    
}