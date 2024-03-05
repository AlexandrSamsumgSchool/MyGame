package com.example.mygame;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;

public class Player extends Circle  {
    public float EatenFood = 0;
    public static  double MAX_SPEED = 10;
    public double mass = 2;
    private Joystick joystick;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super( ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
    }

    public void update() {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        if(EatenFood>75 && radius>100 ){radius=radius-0.0625;EatenFood-=0.0225;}
        MAX_SPEED = 120/(Math.sqrt(radius));
        }
        public boolean CalculateScale(int widthPixels,int heightPixels){

            return (widthPixels/4<=radius || heightPixels/4<=radius);

        }

    }
