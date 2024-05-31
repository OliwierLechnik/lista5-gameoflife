package org.example.gameoflife;

import static java.util.Collections.min;

public class Settings {
    private static Settings instance;
    private int columns;
    private int rows;
    private double probability;
    private int speed;
    private int cellHeight;
    private int cellWidth;
    private int sceneWidth;
    private int sceneHeight;

    /**
     * Singleton constructor that sets default values for the simulation settings.
     * It defines the framework within which the cellular automaton operates.
     */
    private Settings(){
        this.columns = 0;
        this.rows = 0;
        this.probability = 0;
        this.speed = 0;
        this.cellHeight = 0;
        this.cellWidth = 0;
        this.sceneWidth = 1600;
        this.sceneHeight = 960;
    }

    /**
     * Calculates the size of each cell based on the preferred dimensions of the grid.
     * This method ensures that cells fit pixel-perfect within the user interface.
     */
    public void calculateDimensions(){
        cellWidth = cellHeight = Math.min(sceneWidth /columns, sceneHeight /rows);
        sceneHeight = cellHeight*rows;
        sceneWidth = cellWidth*columns;
    }
    /**
     *
     * Singleton accessor that ensures consistent simulation settings across the entire program.
     * This method provides a centralized point of configuration for the cellular automaton.
     */
    public static Settings getSettings(){
        if(instance == null){
            instance = new Settings();
        }
        return instance;
    }

    public int getSceneWidth() {
        return sceneWidth;
    }

    public int getSceneHeight() {
        return sceneHeight;
    }

    public void setColumns(int columns){
        this.columns = columns;
    }

    public void setRows(int rows){
        this.rows = rows;
    }

    public void setProbability(double probability){
        this.probability = probability;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getColumns(){
        return this.columns;
    }

    public int getRows(){
        return this.rows;
    }

    public double getProbability(){
        return this.probability;
    }

    public int getSpeed(){
        return this.speed;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

}
