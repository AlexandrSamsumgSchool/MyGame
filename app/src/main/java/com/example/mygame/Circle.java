package com.example.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends GameObject {
    protected double radius;
    protected Paint paint;

    public Circle(int color, double positionX, double positionY, double radius) {
            super(positionX, positionY);
            this.radius = radius;
            paint = new Paint();
            paint.setColor(color);
        }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawCircle((float)gameDisplay.gameTOdisplaycoordinateX(positionX),(float)gameDisplay.gameTOdisplaycoordinateY(positionY), (float) radius,paint);
    }

    @Override
    public void update() {

    }
}
