package com.example.mygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Player player;
    Points points;
    private float[] FoodX;
    private float[] FoodY;
    DisplayMetrics displayMetrics;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private GameDisplay camera ;
    public Game(Context context) {
            super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this,surfaceHolder);
            joystick = new Joystick(275,800,150,70);
            player = new Player(getContext(),joystick,1000,500,100);
         displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        camera = new GameDisplay(displayMetrics.widthPixels,displayMetrics.heightPixels,player);
        points = new Points();
        FoodX = points.SpawnPointsX(500);
        FoodY = points.SpawnPointsY(500);;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed(event.getX(), event.getY())){
                    joystick.setIsPressed(true);

                }
                return true;
                case MotionEvent.ACTION_MOVE:
                    if(joystick.getIsPressed()){
                        joystick.setActuator(event.getX(), event.getY());


                    }
                  return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated( SurfaceHolder holder) {
    gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed( SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawARGB(255, 255, 255, 255);
       drawFPS(canvas);
        player.draw(canvas,camera);
        joystick.draw(canvas);
         drawFood(canvas);
    }

    public void drawFood(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < 500; i++) {
            if (points.CalculateFood((int) player.getPositionX(), (int) player.getPositionY(), (int) FoodX[i], (int) FoodY[i], (int) player.radius)) {
                FoodX[i] = (int) (Math.random() * 10000);
                FoodY[i] = (int) (Math.random() * 10000);
                player.setEatenFood(player.getEatenFood()+1);
            }
            canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(FoodX[i]), (float) camera.gameTOdisplaycoordinateY(FoodY[i]), points.getRadiusFood(), paint);
        }

    }
    public void drawFPS (Canvas canvas){

        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(0),(float)camera.gameTOdisplaycoordinateY(0),100,paint);
        canvas.drawText("FPS : "+averageFPS,100,200,paint);
    }
    public void update() {
    camera.update();
    player.update();
    joystick.update();
        for (int i = 0; i < points.getMAX_POINTS_FOOD(); i++) {
            if (points.CalculateFood((int) player.positionX, (int) player.positionY, (int) FoodX[i], (int) FoodY[i], (int)player.radius)) {
                player.setEatenFood(player.getEatenFood() + 1);
                player.radius +=15;
            }
        }
        if(displayMetrics.heightPixels<player.radius*3){player.radius = player.radius/2;points.setRadiusFood(points.getRadiusFood()/2);}

    }



}
