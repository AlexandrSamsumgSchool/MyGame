package com.example.mygame;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Joystick {
    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private final int innerCircleRadius;
    private final int outerCircleRadius;
    private int innerCircleCenterPositionY;
    private int innerCircleCenterPositionX;
    private final int outerCircleCenterPositionX;
    private final int outerCircleCenterPositionY;
    private double joystickCenterToTouchDistance ;
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
        joystickCenterToTouchDistance = Math.sqrt(Math.pow(outerCircleCenterPositionX - touchPositionX,2)+Math.pow(outerCircleCenterPositionY-touchPositionY,2));
        return joystickCenterToTouchDistance < outerCircleRadius ;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;

    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerCircleCenterPositionX;
        double deltaY = touchPositionY- outerCircleCenterPositionY;
        double deltadistance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
        if(deltadistance<outerCircleRadius){
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        }
        else
        {
            actuatorX =deltaX/deltadistance;
            actuatorY = deltaY/deltadistance;
        }
    }

    public void resetActuator() {
        actuatorY = 0.0;
        actuatorX = 0.0;
    }
//    public int getDirectionX(){
//
//        return 0;
//    }
//    public int getLine(int x1,int y1,int x2,int y2){
//        int a,b,c;
//        a = y1-y2;
//        b= x2-x1;
//        c = x1*y2-x2*y1;
//    }
    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
