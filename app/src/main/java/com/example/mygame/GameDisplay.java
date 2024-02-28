package com.example.mygame;

import android.graphics.Rect;

public class GameDisplay {
    public final Rect DISPLAY_RECT ;
    private double gameToDisplayCoordinateOFFsetX;
    private double gameToDisplayCoordinateOFFsetY;
    private final int widthPixels;
    private final int heightPixels;
    private double gameCenterY;
    private double gameCenterX;
    private double displayCenterX;
    private double displayCenterY;
    private GameObject centerObject;

    public GameDisplay(int widthPixels, int heightPixels, GameObject centerObject) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        DISPLAY_RECT = new Rect(0, 0, widthPixels, heightPixels);
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
