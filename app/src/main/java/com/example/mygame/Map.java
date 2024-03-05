package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Map {
    GameDisplay camera;
    int widthPixels,heightPixels;
    DisplayMetrics displayMetrics;
    public float Cagesize = 200;
    Player player;
    public Map(GameDisplay camera,Player player,int widthPixels,int heightPixels) {
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
            int w = 0;
            for (int i = 0; i <= 10000; i+=Cagesize) {
                w += Cagesize;
                canvas.drawLine((float) camera.gameTOdisplaycoordinateX(w),
                        (float) camera.gameTOdisplaycoordinateY(0), (float) camera.gameTOdisplaycoordinateX(w),
                        (float) camera.gameTOdisplaycoordinateY(10000), paint);

                canvas.drawLine((float) camera.gameTOdisplaycoordinateX(0), (float) camera.gameTOdisplaycoordinateY(w),
                        (float) camera.gameTOdisplaycoordinateX(10000),
                        (float) camera.gameTOdisplaycoordinateY(w), paint);
            }

    }
    public  void update(){
        if((widthPixels/4<=player.radius || heightPixels/4<=player.radius))Cagesize/=2;
    }
}
