package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Circle extends GameObject {
    protected double radius;
    protected Paint paint;
    Random rand = new Random();
    protected int color;

    public Circle(int color, double positionX, double positionY, double radius) {
            super(positionX, positionY);
            this.radius = radius;
            paint = new Paint();
            paint.setColor(color);
            this.color = color;
        }

    public Circle() {
        super();
    }

    public int getColor() {
        return (android.graphics.Color.argb(255,rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));
    }

    @Override
    public void draw(Canvas canvas, CamerA gameDisplay) {
        paint.setColor(color);
        canvas.drawCircle((float)gameDisplay.gameTOdisplaycoordinateX(positionX),(float)gameDisplay.gameTOdisplaycoordinateY(positionY), (float) radius,paint);
    }
    public boolean CalculateScale(int widthPixels,int heightPixels){

        return (widthPixels/4 <= radius || heightPixels/4<=radius);

    }
    public boolean CalculateScale2(int widthPixels,int heightPixels){
        return (widthPixels/8>radius || heightPixels/8>radius);
    }
    public void Collision(){
        if(positionX-radius <= 0)positionX =radius;
        if(positionX+radius >= 6000)positionX = 6000-radius;
        if(positionY-radius <= 0)positionY=radius;
        if(positionY+radius >= 6000)positionY = 6000-radius;
    }
    public boolean Can_EAT_PL(double pX,double pY,double rad){
        return ((pX-positionX)*(pX-positionX)+(pY-positionY)*(pY-positionY)<radius*radius && rad<radius);
    }
    @Override
    public void update(){}
}
