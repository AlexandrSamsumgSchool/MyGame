package com.example.mygame;

import android.graphics.Canvas;

public abstract class GameObject {
    protected double positionX, positionY;
    protected double velocityX, velocityY = 0.0;

    public GameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public GameObject() {

    }

    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }


    public abstract void draw(Canvas canvas, CamerA camera);
    public abstract void update();
}
