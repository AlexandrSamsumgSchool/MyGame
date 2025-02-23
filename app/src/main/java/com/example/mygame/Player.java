package com.example.mygame;

import android.content.Context;
import androidx.core.content.ContextCompat;

public class Player extends Circle  {
    public float EatenFood = 0;
    public  double MAX_SPEED = 10;
    public double mass = 1 ;
    public int Scale = 1;// Масштаб, чтобы передать серверу в реальном размере
    public boolean isEaten = false;
    private final Joystick joystick;
    public String name;
    public int vision_width;
    public int vision_heigth;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius,String name) {
        super(ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.name = name;
        vision_width = Game.displayMetrics.widthPixels;
        vision_heigth = Game.displayMetrics.heightPixels;
    }
    public void update() {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        if(EatenFood>75 && radius>100 ){radius=radius-0.0225;EatenFood-=0.0225;}
        Collision();
    }

    public  boolean Can_SEE_FOOD(float pX,float pY,int wp,int hp){
        return (positionX+wp>pX && positionX-wp<pX && positionY+hp*2>pY && positionY-hp*2<pY);
    }
    public  boolean Can_SEE(float dist_x,float dist_y,double r){
        return ((Math.abs(dist_x)<=(vision_width)/2+r) &&(Math.abs(dist_y)<=(vision_heigth)/2+r));
    }
}
