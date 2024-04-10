package com.example.mygame;
import android.graphics.Canvas;
import android.graphics.Color;
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
    public int getDirectionX(){

        return 0;
    }

    public String findIntersection(Canvas canvas) {
        double x0 = outerCircleCenterPositionX;
        double y0 = outerCircleCenterPositionY;
        double x1 = outerCircleCenterPositionX,y1 = outerCircleCenterPositionY,x2 = innerCircleCenterPositionX,y2 = innerCircleCenterPositionY;
        double r = outerCircleRadius;
        double a = y2-y1;
        double b = x1-x2;
        double c = (-1) * (a * x1 + b * y1);
        double m = -a/b;
        double n = -c/b;
        double A = 1 + m*m;
        double B = 2*m*(n - y0) - 2*x0;
        double C = x0*x0 + (n - y0)*(n - y0) - r*r;

        double D = B*B - 4*A*C;

            double xm = (-B + Math.sqrt(D))/(2*A);
            double ym = m*x1 + n;

            double x2m = (-B - Math.sqrt(D))/(2*A);
            double y2m = m*x2 + n;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            if(((xm-innerCircleCenterPositionX)*(xm-innerCircleCenterPositionX)+(ym-innerCircleCenterPositionY)*(ym-innerCircleCenterPositionY))<(((x2m-innerCircleCenterPositionX)*(x2m-innerCircleCenterPositionX)+(y2m-innerCircleCenterPositionY)*(y2m-innerCircleCenterPositionY))))
                canvas.drawCircle((float) xm, (float) ym,50,paint);
            else   canvas.drawCircle((float) x2m, (float) y2m,50,paint);
            return ("Точка 1: (" + xm + ", " + ym + ")")+("Точка 2: (" + x2m + ", " + y2m + ")");


    }
    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
