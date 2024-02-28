package com.example.mygame;

import android.util.DisplayMetrics;

public class Points {

    private final int MAX_POINTS_FOOD = 500;

    public void setRadiusFood(int radiusFood) {
        this.radiusFood = radiusFood;
    }

    private  int radiusFood = 10;
    DisplayMetrics displayMetrics = new DisplayMetrics();


    public int getRadiusFood() {
        return radiusFood;
    }
    public int getMAX_POINTS_FOOD() {
        return MAX_POINTS_FOOD;
    }

    public float[] SpawnPointsX(int Eaten_Points){
        float[] PointsX = new float[Eaten_Points] ;
                for(int i=0;i<Eaten_Points;i++) {
            PointsX [i]= (float) (Math.random() * 10000);

        }
        return PointsX;
    }
    public float[] SpawnPointsY(int Eaten_Points){
        float[] PointsY = new float[Eaten_Points];
        for(int i=0;i<PointsY.length;i++){
            PointsY[i] = (int)(Math.random()*10000);
        }
        return PointsY;
    }
    public boolean CalculateFood(int PlayerX, int PlayerY, int circleX, int  circleY, int R){
        if((PlayerX-circleX)*(PlayerX-circleX)+(PlayerY-circleY)*(PlayerY-circleY)<=R*R) return true;
        else return false;
    }
    public boolean CalculateScale(int playerR){
        if(playerR>=displayMetrics.heightPixels)return true;
        else return false;
    }

}
