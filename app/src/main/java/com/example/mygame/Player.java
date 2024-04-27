package com.example.mygame;

import android.content.Context;
import androidx.core.content.ContextCompat;

public class Player extends Circle  {
    public float EatenFood = 0;
    public static double MAX_SPEED = 10;
    public double mass;
    private final Joystick joystick;


    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super( ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
    }

    public void update() {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        if(EatenFood>75 && radius>100 ){radius=radius-0.0325;EatenFood-=0.0325;}
        MAX_SPEED = 120/(Math.sqrt(radius));
        mass = 1/(Math.sqrt(radius))+1;
        Collision();
        }

    public  boolean Can_SEE_FOOD(float pX,float pY,int wp,int hp){
        return (positionX+wp>pX && positionX-wp<pX && positionY+hp>pY && positionY-hp<pY);
    }
    }
