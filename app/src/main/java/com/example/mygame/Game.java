package com.example.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final MediaPlayer mediaPlayer1,mediaPlayer2 ;
    private final Points points;
    private final Map map;
    private  Bot[] bots;
    private final DisplayMetrics displayMetrics;
    private final Joystick joystick;
    private  GameLoop gameLoop;
    private final GameDisplay camera ;
    private final Bot bot;
    private int innerCircleColor,outerCircleColor;
    private final Resources resources = getResources();

    public Game(Context context) {
            super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        // создаем главный игровой цикл и получаем размеры экрана
         gameLoop = new GameLoop(this,surfaceHolder);
        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // создаем джойстик

        innerCircleColor = resources.getColor(R.color.magenta);
        outerCircleColor = resources.getColor(R.color.outer);
        joystick = new Joystick(275,displayMetrics.heightPixels-300/*800*/,displayMetrics.heightPixels/5,displayMetrics.heightPixels/12,innerCircleColor,outerCircleColor);

// создаем игрока и камеру
        player = new Player(getContext(),joystick,Math.random()*8000,Math.random()*8000,100);
        camera = new GameDisplay(displayMetrics.widthPixels,displayMetrics.heightPixels,player);
// создаем еду
        points = new Points();
        points.SpawnPointsX();
        points.SpawnPointsY();
// карта
        map = new Map(camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
// создаем ботов
        bot = new Bot();
        bots = new Bot[Bot.Bots];
        for(int i=0;i<Bot.Bots;i++) {
            bots[i] = new Bot(player.getColor(), bot.SpawnBotX(), bot.SpawnBotY(), 100);
        }
        // обработка звуков
        mediaPlayer1 = MediaPlayer.create(this.getContext(),R.raw.eat_dot);
        mediaPlayer2 = MediaPlayer.create(this.getContext(),R.raw.eat_blob);
    }
// узнаем где произошло нажатие и считаем скорость джойстика
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed(event.getX(), event.getY())){
                    joystick.setIsPressed(true);
                    joystick.setActuator(event.getX(), event.getY());
                }
               // if((event.getX()-575)*(event.getX()-575)+(event.getY()-800)*(event.getY()-800)<=100*100 ) player.radius/=2;
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
// рисуем все что обновили
    @Override
    public void draw(Canvas canvas) {
         super.draw(canvas);
         canvas.drawColor(Color.WHITE);
         map.drawMap(canvas);
         drawFPS_SIZE(canvas);
         joystick.draw(canvas);
         drawFood(canvas,camera,bots,mediaPlayer1,mediaPlayer2);
         for(Bot k:bots){
             if(player.Can_SEE_FOOD((int) k.positionX, (int) k.positionY,displayMetrics.widthPixels,displayMetrics.heightPixels));
             k.draw(canvas,camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
         }
        player.draw(canvas,camera);
    }
    public void drawFood(Canvas canvas, GameDisplay camera,Bot[] bots, MediaPlayer mediaPlayer,MediaPlayer mediaPlayer1) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        for (int i = 0; i < points.MAX_POINTS_FOOD; i++) {
            // player food
            if (points.Can_Eat((int) player.getPositionX(), (int) player.getPositionY(), (int) points.FoodX[i], (int) points.FoodY[i], (int) player.radius)) {
                mediaPlayer.start();
                points.FoodX[i] = (int) (Math.random() * 10000);
                points.FoodY[i] = (int) (Math.random() * 10000);
                player.radius += player.mass;
                player.EatenFood += 1;
            }
//            bot food определяем ее координаты если сьели (телепортируем на новое место)
            for (int j=0;j<bots.length;j++) {
                if (points.Can_Eat((int) bots[j].getPositionX(), (int) bots[j].getPositionY(), (int) points.FoodX[i], (int) points.FoodY[i], (int) bots[j].radius)) {
                    points.FoodX[i] = (int) (Math.random() * 10000);
                    points.FoodY[i] = (int) (Math.random() * 10000);
                    bots[j].radius += 3;
                    bots[j].EatenFood++;
                }
                if(player.Can_EAT_PL((int) bots[j].positionX, (int)bots[j].positionY, (int) bots[j].radius)){
                    mediaPlayer1.start();
                    bots[j].velocityY = 0;
                    bots[j].velocityX = 0;
                    player.radius = Math.sqrt(player.radius*player.radius+ bots[j].radius*bots[j].radius);
                    bots[j].radius = 100;
                    player.EatenFood+=bots[j].EatenFood;
                    bots[j].EatenFood = 0;
                    bots[j].positionX = Math.random()*10000;
                    bots[j].positionX = Math.random()*10000;
                  //  bot.remove(bots,j);
                }
//                if(bots[j].Can_EAT_PL((int) player.positionX, (int) player.positionY, (int) player.radius)){
//                    player.velocityY = 0;
//                    player.velocityX = 0;
//                   // bots[j].radius = Math.sqrt(player.radius*player.radius+ bots[j].radius*bots[j].radius);
//                    Intent intent = new Intent(Game.this.getContext(),Menu.class);
//                    player.setColor();
//                }

            }
//             масштаб
            if(player.CalculateScale(displayMetrics.widthPixels,displayMetrics.heightPixels) && map.Cagesize > 25)
            {player.radius = player.radius/1.25;points.radiusFood/=1.25;map.Cagesize/=1.5;Player.MAX_SPEED/=1.25;
                for(Bot k:bots){
                    k.radius/=1.5;
                }
            }
            // рисуем еду
            paint.setColor(points.Food_Color[i]);
            if (player.Can_SEE_FOOD((int) points.FoodX[i], (int) points.FoodY[i],displayMetrics.widthPixels,displayMetrics.heightPixels)){
                canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(points.FoodX[i]), (float) camera.gameTOdisplaycoordinateY(points.FoodY[i]), points.getRadiusFood(), paint);}
        }

    }
    public void drawButton(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(575,800,100,paint);
    }


//рисуем  ФПС и размер еды
    public void drawFPS_SIZE (Canvas canvas){

                    String averageFPS = Integer.toString((int) gameLoop.getAverageFPS());
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(50);
                    canvas.drawText("FPS = " + averageFPS, 100, 200, paint);
                    canvas.drawText("Size = " + (int) player.EatenFood, 100, 100, paint);

    }
    //обновляем игроком карту и расположение камеры
    public void update() {
    camera.update();
    joystick.update();
        for(Bot k:bots){
            k.update();
        }
    player.update();
    }
// при закрытии игры останавливаем цикл игры
    public void pause() {
        gameLoop.stopLoop();
    }
}
