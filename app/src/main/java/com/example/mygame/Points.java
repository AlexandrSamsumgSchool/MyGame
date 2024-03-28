package com.example.mygame;


import android.graphics.Color;
import java.util.Random;

public class Points {
    public final int MAX_POINTS_FOOD = 1000;
    public float radiusFood = 10;
    public   float[] FoodX;
    public   float[] FoodY;
    public int[] Food_Color = new int[MAX_POINTS_FOOD];
    Random rand = new Random();


    public float getRadiusFood() {
        return radiusFood;
    }
    public void SpawnPointsX(){
        float[] PointsX = new float[MAX_POINTS_FOOD] ;
                for(int i=0;i<MAX_POINTS_FOOD;i++) {
            PointsX [i]= (float) (Math.random() * 10000);
            Food_Color[i] = Color.argb(255,rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));

                }
        FoodX = PointsX;
    }
    public void SpawnPointsY(){
        float[] PointsY = new float[MAX_POINTS_FOOD];
        for(int i=0;i<MAX_POINTS_FOOD;i++){
            PointsY[i] = (int)(Math.random()*10000);
        }
        FoodY = PointsY;
    }

    public boolean Can_Eat(int PlayerX, int PlayerY, int circleX, int  circleY, int R){
        return (PlayerX - circleX) * (PlayerX - circleX) + (PlayerY - circleY) * (PlayerY - circleY) <= R * R;

    }


    }


