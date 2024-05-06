package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Map {
    CamerA camera;
    int widthPixels,heightPixels;
    DisplayMetrics displayMetrics;

    public float Cagesize = 600;
    private float   Wx = 0, Wy = 0;
    Player player;
    public Map(CamerA camera, Player player, int widthPixels, int heightPixels) {
        this.camera = camera;
        this.player = player;
        displayMetrics = new DisplayMetrics();
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public void drawMap(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        for(int i=0;i<widthPixels/Cagesize+2;i++){
            Wx+=Cagesize;
            canvas.drawLine(Wx, 0,Wx, heightPixels+200, paint);
        }

        for(int i=0;i<heightPixels/Cagesize+2;i++){
            Wy+=Cagesize;
            canvas.drawLine(0,Wy, widthPixels+200,Wy, paint);
        }
       Wx = 0;
       Wy = 0;
    }
    public void update(){
        Wx = (float) (-Cagesize+(-player.positionX)%Cagesize);
        Wy= (float) (-Cagesize+(-player.positionY)%Cagesize);
    }
}
