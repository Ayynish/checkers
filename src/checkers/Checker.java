/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
/**
 *
 * @author Anish
 */
public class Checker extends Circle {
    
    private boolean type; // true = king piece | false = regular piece
    private boolean team; // true = team 1 | false = team 2
    private int pieceNum;
    
    // create a default checker piece
    public Checker(double radius, Paint fill) {
        this.setRadius(radius);
        this.setFill(fill);
        this.setType(false);
    }
    
    // create a checker piece with the option of type
    public Checker(double radius, Paint fill, boolean type) {
        this.setRadius(radius);
        this.setFill(fill);
        this.setType(type);
    }
    
    public String toString() {
        return " [ Checker | Radius: " + this.getRadius() +
                ", Color: " + this.getFill() +
                ", Type: " + this.getType() +
                ", Team: " + this.getTeam() + " ] ";
    }
    
    public void setType(boolean type) {
        this.type = type;
        
        if (type && team)
            this.setFill(GameState.team1KingColor);
        
        if (type && !team)
            this.setFill(GameState.team2KingColor);
    }
    
    public boolean getType() {
        return type;
    }
    
    public void setTeam(boolean team) {
        this.team = team;
    }
    
    public boolean getTeam() {
        return team;
    }
    
    public void setPieceNum(int pieceNum) {
        this.pieceNum = pieceNum;
    }
    
    public int getPieceNum() {
        return pieceNum;
    }
    
}