package com.example.mygame;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Joystick {
    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private final int innerCircleRadius;
    private final int outerCircleRadius;
    public int innerCircleCenterPositionY;
    public int innerCircleCenterPositionX;
    public final int outerCircleCenterPositionX;
    public final int outerCircleCenterPositionY;
    public double joystickCenterToTouchDistance ;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;
    public Joystick(int CenterPositionX, int CenterPositionY, int outerCircleRadius, int innerCircleRadius,int innerCircleColor,int outerCircleColor) {
        outerCircleCenterPositionX = CenterPositionX;
        outerCircleCenterPositionY = CenterPositionY;
        innerCircleCenterPositionX = CenterPositionX;
        innerCircleCenterPositionY = CenterPositionY;


        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(outerCircleColor);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint =new Paint();
        innerCirclePaint.setColor(innerCircleColor);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCircleCenterPositionX,outerCircleCenterPositionY,outerCircleRadius,outerCirclePaint);
        canvas.drawCircle(innerCircleCenterPositionX,innerCircleCenterPositionY,innerCircleRadius,innerCirclePaint);
    }

    public void update() {updateInnerCirclePosition();}

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX =(int)(outerCircleCenterPositionX+actuatorX*outerCircleRadius);
        innerCircleCenterPositionY = (int)(outerCircleCenterPositionY+actuatorY*outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance = Math.sqrt(Math.pow(outerCircleCenterPositionX - touchPositionX,2)+Math.pow(outerCircleCenterPositionY-touchPositionY,2))-1000;
        return joystickCenterToTouchDistance < outerCircleRadius ;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;

    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double dX = touchPositionX - outerCircleCenterPositionX;
        double dY = touchPositionY- outerCircleCenterPositionY;
        double deltadistance = Math.sqrt(Math.pow(dX,2)+Math.pow(dY,2));
        if(deltadistance<outerCircleRadius){
            actuatorX = dX/outerCircleRadius;
            actuatorY = dY/outerCircleRadius;
        }
        else
        {
            actuatorX =dX/deltadistance;
            actuatorY = dY/deltadistance;
        }
    }

    public void resetActuator() {
        actuatorY = 0.0;
        actuatorX = 0.0;
    }
    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
