package org.example.gameoflife;

public class Settings {
    private static Settings instance;
    private int width;
    private int height;
    private double probability;
    private int speed;

    private Settings(){
        this.width = 0;
        this.height = 0;
        this.probability = 0;
        this.speed = 0;
    }

    public static Settings getSettings(){
        if(instance == null){
            instance = new Settings();
        }
        return instance;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setProbability(double probability){
        this.probability = probability;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public double getProbability(){
        return this.probability;
    }

    public int getSpeed(){
        return this.speed;
    }

}
