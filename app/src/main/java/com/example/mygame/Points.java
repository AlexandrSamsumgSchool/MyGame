package com.example.mygame;

public class Points {

    public final int MAX_POINTS_FOOD = 1000;
    public float radiusFood = 10;

    public float getRadiusFood() {
        return radiusFood;
    }
    public float[] SpawnPointsX(){
        float[] PointsX = new float[MAX_POINTS_FOOD] ;
                for(int i=0;i<MAX_POINTS_FOOD;i++) {
            PointsX [i]= (float) (Math.random() * 10000);

        }
        return PointsX;
    }
    public float[] SpawnPointsY(){
        float[] PointsY = new float[MAX_POINTS_FOOD];
        for(int i=0;i<MAX_POINTS_FOOD;i++){
            PointsY[i] = (int)(Math.random()*10000);
        }
        return PointsY;
    }
    public boolean CalculateFood(int PlayerX, int PlayerY, int circleX, int  circleY, int R){
        return (PlayerX - circleX) * (PlayerX - circleX) + (PlayerY - circleY) * (PlayerY - circleY) <= R * R;
    }

}
