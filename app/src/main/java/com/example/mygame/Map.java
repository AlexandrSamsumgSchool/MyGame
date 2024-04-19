package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Map {
    Camera camera;
    int widthPixels,heightPixels;
    DisplayMetrics displayMetrics;
    private int CageX = 0 , CageY = 0;
    public float Cagesize = 600;
    Player player;
    public Map(Camera camera, Player player, int widthPixels, int heightPixels) {
        this.camera = camera;
        this.player = player;
        displayMetrics = new DisplayMetrics();
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public void drawMap(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
            int w = 0;
            for (int i = 0; i <= 10000; i+=Cagesize) {
                w += Cagesize;
                canvas.drawLine((float) camera.gameTOdisplaycoordinateX(w),
                        (float) camera.gameTOdisplaycoordinateY(CageY), (float) camera.gameTOdisplaycoordinateX(w),
                        (float) camera.gameTOdisplaycoordinateY(10000), paint);

                canvas.drawLine((float) camera.gameTOdisplaycoordinateX(CageX), (float) camera.gameTOdisplaycoordinateY(w),
                        (float) camera.gameTOdisplaycoordinateX(10000),
                        (float) camera.gameTOdisplaycoordinateY(w), paint);
            }
//        for(int i=0;i<widthPixels/Cagesize+2;i++){
//            w+=Cagesize;
//            canvas.drawLine(w, CageY, w, heightPixels, paint);
//        }
//        w=0;
//        for(int i=0;i<heightPixels/Cagesize+2;i++){
//            w+=Cagesize;
//            canvas.drawLine(CageX,w, widthPixels,w, paint);
//        }

    }
    public  void update(){
        if((widthPixels/4<=player.radius || heightPixels/4<=player.radius ))Cagesize/=2;
    }
}
