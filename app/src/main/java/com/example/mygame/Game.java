package com.example.mygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Player player;
    Points points;
   Map map;
    private final float[] FoodX;
    private final float[] FoodY;
    int Rr = 100;//radius food
    DisplayMetrics displayMetrics;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private final GameDisplay camera ;
    Bot bot;
    public Game(Context context) {
            super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this,surfaceHolder);

        joystick = new Joystick(275,800,200,100);

        player = new Player(getContext(),joystick,1000,500,100);

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        camera = new GameDisplay(displayMetrics.widthPixels,displayMetrics.heightPixels,player);

        points = new Points();
        FoodX = points.SpawnPointsX();
        FoodY = points.SpawnPointsY();

        map = new Map(camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);

        bot = new Bot(Color.RED,500,500,100);

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
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if(gameLoop.getState().equals(Thread.State.TERMINATED)){
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this,surfaceHolder);
        }
    gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
         super.draw(canvas);
         canvas.drawARGB(255, 255, 255, 255);
         map.drawMap(canvas);
         drawFPS_Eaten_FOOD(canvas);
         player.draw(canvas,camera);
         joystick.draw(canvas);
         drawFood(canvas);
         bot.draw(canvas,camera);
    }

    public void drawFood(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < points.MAX_POINTS_FOOD; i++) {
            if (points.CalculateFood((int) player.getPositionX(), (int) player.getPositionY(), (int) FoodX[i], (int) FoodY[i], (int) player.radius)) {
                FoodX[i] = (int) (Math.random() * 10000);
                FoodY[i] = (int) (Math.random() * 10000);
                player.radius +=player.mass;
                player.EatenFood+=1;
            }
            if(player.CalculateScale(displayMetrics.widthPixels, displayMetrics.heightPixels) && map.Cagesize > 25)
            {player.radius = player.radius/1.25;points.radiusFood/=1.5;Rr/=1.5;map.Cagesize/=1.5;Player.MAX_SPEED/=1.25;}
            canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(FoodX[i]), (float) camera.gameTOdisplaycoordinateY(FoodY[i]), points.getRadiusFood(), paint);
        }

    }
    public void drawFPS_Eaten_FOOD (Canvas canvas){

        String averageFPS = Integer.toString((int)gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS = "+averageFPS,100,200,paint);
        canvas.drawText("Size = "+(int)player.EatenFood,100,100,paint);
    }
    public void update() {
    camera.update();
    player.update();
    joystick.update();
    bot.update();
    }


    public void pause() {
        gameLoop.stopLoop();
    }
}
