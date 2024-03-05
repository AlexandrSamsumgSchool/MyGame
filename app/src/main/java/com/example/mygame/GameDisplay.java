package com.example.mygame;

import android.graphics.Rect;

public class GameDisplay {
    private double gameToDisplayCoordinateOFFsetX;
    private double gameToDisplayCoordinateOFFsetY;
    private double gameCenterY;
    private double gameCenterX;
    private double displayCenterX;
    private double displayCenterY;
    private GameObject centerObject;

    public GameDisplay(int widthPixels, int heightPixels, GameObject centerObject) {
        this.centerObject = centerObject;
        displayCenterX = widthPixels/2.0;
        displayCenterY = heightPixels/2.0;
    }

    public void update(){
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();
        gameToDisplayCoordinateOFFsetX  = displayCenterX - gameCenterX;
        gameToDisplayCoordinateOFFsetY  = displayCenterY - gameCenterY;

    }
    public double gameTOdisplaycoordinateY(double y) {
        return y + gameToDisplayCoordinateOFFsetY;
    }

    public double gameTOdisplaycoordinateX(double x) {
        return x + gameToDisplayCoordinateOFFsetX;
    }

}
