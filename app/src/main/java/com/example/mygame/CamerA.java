package com.example.mygame;

public class CamerA {
    private double gameX;
    private double gameY;
    private double gameCenterY;
    private double gameCenterX;
    private double displayCenterX;
    private double displayCenterY;
    private GameObject centerObject;

    public CamerA(int widthPixels, int heightPixels, GameObject centerObject) {
        this.centerObject = centerObject;
        displayCenterX = widthPixels/2.0;
        displayCenterY = heightPixels/2.0;
    }

    public void update(){
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();
        gameX = displayCenterX - gameCenterX;
        gameY = displayCenterY - gameCenterY;

    }
    public double gameTOdisplaycoordinateY(double y) {
        return y + gameY;
    }

    public double gameTOdisplaycoordinateX(double x) {
        return x + gameX;
    }

}
