package com.example.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Player extends Circle  {
    private static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private int EatenFood = 0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS;
   private Joystick joystick;
   Points points = new Points();

    public void setEatenFood(int eatenFood) {
        EatenFood = eatenFood;
    }

    public int getEatenFood() {
        return EatenFood;
    }


    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super( ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
    }

    public void update() {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;

    }}
