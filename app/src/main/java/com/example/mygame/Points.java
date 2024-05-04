package com.example.mygame;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import java.util.Random;

public class Points extends Circle{
    public final int MAX_POINTS_FOOD = 1000;
    public float radiusFood = 15;
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
    public void Draw(Canvas canvas, CamerA camera, Bot[] bots, MediaPlayer mediaPlayer, MediaPlayer mediaPlayer1, DisplayMetrics displayMetrics,Player player) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < MAX_POINTS_FOOD; i++) {
            // player food
            if (Can_Eat((int) player.positionX, (int) player.positionY, (int) FoodX[i], (int) FoodY[i], (int)player.radius)) {
                mediaPlayer.start();
                FoodX[i] = (int) (Math.random() * 10000);
                FoodY[i] = (int) (Math.random() * 10000);
                player.radius += player.mass;
                player.EatenFood += 1;
            }
//            bot food определяем ее координаты если сьели (телепортируем на новое место)
            for (Bot value : bots) {
                if (Can_Eat((int) value.getPositionX(), (int) value.getPositionY(), (int) FoodX[i], (int) FoodY[i], (int) value.radius)) {
                    FoodX[i] = (int) (Math.random() * 10000);
                    FoodY[i] = (int) (Math.random() * 10000);
                    value.radius += 3;
                    value.EatenFood++;
                }
                if (player.Can_EAT_PL((int) value.positionX, (int) value.positionY, (int) value.radius)) {
                    mediaPlayer1.start();
                    value.velocityY = 0;
                    value.velocityX = 0;
                    player.radius = Math.sqrt(player.radius * player.radius + value.radius * value.radius);
                    value.radius = 100;
                    player.EatenFood += value.EatenFood;
                    value.EatenFood = 0;
                    value.positionX = Math.random() * 10000;
                    value.positionX = Math.random() * 10000;
                }
                if(value.Can_EAT_PL((int) player.positionX, (int) player.positionY, (int) player.radius)){
                    player.velocityY = 0;
                    player.velocityX = 0;
                    value.radius = Math.sqrt(player.radius*player.radius+value.radius*value.radius);
                    player.positionX = 0;
                    player.positionY = 0;
                    player.radius = 100;
                }
            }
            // рисуем еду
            paint.setColor(Food_Color[i]);
            if (player.Can_SEE_FOOD(FoodX[i], FoodY[i],displayMetrics.widthPixels,displayMetrics.heightPixels)){
                canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(FoodX[i]), (float) camera.gameTOdisplaycoordinateY(FoodY[i]), getRadiusFood(), paint);}
        }
    }

}


