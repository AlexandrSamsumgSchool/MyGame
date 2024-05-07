package com.example.mygame;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final MediaPlayer mediaPlayer1,mediaPlayer2 ;
    private final Points points;
    private final Map map;
    private final Bot[] bots;
    private final DisplayMetrics displayMetrics;
    private final Joystick joystick;
    private  GameLoop gameLoop;
    private final CamerA camera ;

    String name;
    private int Textsize = 60,textX = 14,textY = 5;
    boolean fps  ;
    //сделать нормальное расположение имени

    public Game(Context context,String name,boolean rp,boolean fps) {
            super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        // создаем главный игровой цикл и получаем размеры экрана
         gameLoop = new GameLoop(this,surfaceHolder);
        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if(name.isEmpty())this.name = "Player";
        else this.name = name;
        // создаем джойстик,учитывая расположение (справа или слева)

         int innerCircleColor = R.color.outer;
         int outerCircleColor = R.color.inner;

        this.fps = fps;
        if(rp)joystick = new Joystick(displayMetrics.widthPixels-300,displayMetrics.heightPixels-300/*800*/,displayMetrics.heightPixels/5,displayMetrics.heightPixels/12,innerCircleColor,outerCircleColor);
        else joystick = new Joystick(275,displayMetrics.heightPixels-300/*800*/,displayMetrics.heightPixels/5,displayMetrics.heightPixels/12,innerCircleColor,outerCircleColor);

        // создаем игрока и камеру
        player = new Player(getContext(),joystick,Math.random()*8000,Math.random()*8000,150);
        camera = new CamerA(displayMetrics.widthPixels,displayMetrics.heightPixels,player);
        // создаем еду и способности
        points = new Points();
        points.SpawnPointsX();
        points.SpawnPointsY();
        // карта
        map = new Map(camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
        // создаем ботов
        Bot bot = new Bot();
        bots = new Bot[Bot.Bots];
        for(int i=0;i<Bot.Bots;i++) {
            bots[i] = new Bot(player.getColor(), bot.SpawnBotX(), bot.SpawnBotY(), Math.random()*200+100);
            if(bots[i].Can_EAT_PL((int) player.positionX, (int) player.positionY, (int) player.radius)){
                bots[i].positionX = bot.SpawnBotX();
                bots[i].positionY = bot.SpawnBotY();
            }
        }
        // обработка звуков
        mediaPlayer1 = MediaPlayer.create(this.getContext(),R.raw.eat_dot);
        mediaPlayer2 = MediaPlayer.create(this.getContext(),R.raw.eat_blob);
    }
// узнаем где произошло нажатие и рассчитываем скорость джойстика
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed(event.getX(), event.getY())){
                    joystick.setIsPressed(true);
                    joystick.setActuator(event.getX(), event.getY());
                }
                // выход в меню
                if(player.isEaten) {
                    Intent intent = new Intent(getContext(),Menu.class);
                    startActivity(getContext(),intent, new Bundle());

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
// рисуем все что обновили
    @Override
    public void draw(Canvas canvas) {
         super.draw(canvas);
         canvas.drawColor(Color.WHITE);
         map.drawMap(canvas);
         points.Draw(canvas,camera,bots,mediaPlayer1,mediaPlayer2,displayMetrics,player);
         for(Bot k:bots){
                     k.draw(canvas,camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
         }
        player.draw(canvas,camera);
        drawName(canvas);
        drawFPS_SIZE(canvas);
        joystick.draw(canvas);
       if(player.isEaten){
           Draw_Game_Over(canvas);
       }
    }

//рисуем  ФПС и размер еды
    public void drawFPS_SIZE (Canvas canvas){
        String averageFPS = Integer.toString((int)gameLoop.getAverageFPS());
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(50);
                    if(fps)canvas.drawText("FPS  "+averageFPS,100,200,paint);
                    canvas.drawText("Size = " + (int) player.EatenFood, 100, 100, paint);
    }
    public void drawName(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(Textsize);
        canvas.drawText(name, displayMetrics.widthPixels/2-textX*name.length(), displayMetrics.heightPixels/2+textY, paint);
    }
    //обновляем игроком карту и расположение камеры
    public void update() {
        if(player.isEaten) {
            Textsize=0;
            return;
        }
    camera.update();
    joystick.update();
        for(Bot k:bots){
            k.update();
        }
    player.update();
        // Масштаб
        if(player.CalculateScale(displayMetrics.widthPixels,displayMetrics.heightPixels) && map.Cagesize > 25)
        {   Textsize*=1.05;textX*=1.072222222111;textY*= 1.06;
            player.radius = player.radius/1.1;points.radiusFood/=1.1;map.Cagesize/=1.2;player.MAX_SPEED/=1.09;
            for(Bot k:bots){
                k.radius/=1.25;
            }
        }
        map.update();
    }
    public void Draw_Game_Over(Canvas canvas){
        String text = "Game Over";
        String text1 = "Size = "+ (int)player.EatenFood;
        String text2 = "Radius = "+(int)player.radius;
        String text3 = "В меню  ";
        int x = displayMetrics.widthPixels/2-325;
        int y = displayMetrics.heightPixels/2-300;

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        float textSize = 200;
        paint.setTextSize(textSize);

        canvas.drawText(text,  x-150, y, paint);
        paint.setColor(Color.BLACK);
        textSize = 150;
        paint.setTextSize(textSize);

        canvas.drawText(text1, x, y+200, paint);
        canvas.drawText(text2, x, y+400, paint);
        canvas.drawText(text3, x, y+600, paint);
    }

// при закрытии игры останавливаем цикл игры
    public void pause() {gameLoop.stopLoop();}
}
