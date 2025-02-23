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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

import java.net.Socket;
import java.util.HashMap;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private static  Player player = null;
    private final MediaPlayer mediaPlayer1,mediaPlayer2 ;
    private final Points points;
    private final Map map;
    private final Bot[] bots;
    public static HashMap<String,Bot> Players = new HashMap<>();
    public static  DisplayMetrics displayMetrics;
    private final Joystick joystick;
    private  GameLoop gameLoop;
    private final CamerA camera ;
    String name;
    private int Textsize = 60,textX = 14,textY = 5;
    boolean fps  ;
    public static Client client ;
    public Game(Context context,String name,boolean rp,boolean fps) {
            super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        // создаем главный игровой цикл и получаем размеры экрана
         gameLoop = new GameLoop(this,surfaceHolder);
        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.name = name;
        // создаем джойстик,учитывая расположение (справа или слева)

         int innerCircleColor = R.color.outer;
         int outerCircleColor = R.color.inner;

        this.fps = fps;
        if(rp)joystick = new Joystick(displayMetrics.widthPixels-300,displayMetrics.heightPixels-300/*800*/,displayMetrics.heightPixels/5,displayMetrics.heightPixels/12,innerCircleColor,outerCircleColor);
        else joystick = new Joystick(275,displayMetrics.heightPixels-300/*800*/,displayMetrics.heightPixels/5,displayMetrics.heightPixels/12,innerCircleColor,outerCircleColor);
        // создаем еду и способности
        points = new Points();
        // создаем игрока и камеру
        player = new Player(getContext(),joystick,Math.random()*5000 + 1000,Math.random()*5000 + 1000,150,name);
        player.color = player.getColor();
        camera = new CamerA(displayMetrics.widthPixels,displayMetrics.heightPixels,player);

        // карта
        map = new Map(camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
        // создаем ботов
        Bot bot = new Bot();
        bots = new Bot[Bot.Bots];
        for(int i=0;i<Bot.Bots;i++) {
            float bX = Bot.SpawnBotX(), bY = Bot.SpawnBotY(),bR = (float) (Math.random()*200+100);
            Bot tmp = new Bot(player.getColor(),bX,bY,bR,null);
            if(tmp.Can_EAT_PL((int) player.positionX, (int) player.positionY, (int) player.radius)){
                bX = bot.SpawnBotX();
                bY = bot.SpawnBotY();
            }
            tmp.positionX = bX;
            tmp.positionY = bY;
            bots[i] = tmp;

        }
        // обработка звуков
        mediaPlayer1 = MediaPlayer.create(this.getContext(),R.raw.eat_dot);
        mediaPlayer2 = MediaPlayer.create(this.getContext(),R.raw.eat_blob);
        client = new Client();
        client.execute();
    }
    public static DisplayMetrics getDisplayMetrics(){
        return displayMetrics;
    }
    public static Player getPlayer(){
        return player;
    }
    public static void setPlayers(String socket, Bot pl) {
        if (socket != null && pl != null) {
            Players.put(socket, pl); // Добавляем или обновляем игрока
        }
    }
    public static boolean findPl(String id){
        return Players.containsValue(id);
    }

    public static void updatePlayer(String socketId, double positionX, double positionY) {
        Players.get(socketId).positionX = positionX;
        Players.get(socketId).positionY = positionY;
    }

    // узнаем где произошло нажатие и рассчитываем скорость игрока
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
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}
// рисуем все что обновили
    @Override
    public void draw(Canvas canvas) {
         super.draw(canvas);
         canvas.drawColor(Color.WHITE);
         map.drawMap(canvas);
         points.Draw(canvas,camera,bots,mediaPlayer1,mediaPlayer2,displayMetrics,player);// самый важный костыль кода)
         for(Bot k:bots){
             k.draw(canvas,camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
         }
        player.draw(canvas,camera);
        drawName(canvas);
        drawFPS_SIZE(canvas);
        joystick.draw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (!Players.isEmpty()) {
               for (HashMap.Entry<String, Bot> entry : Players.entrySet()) {
                   Bot players = entry.getValue();  // сам обьект - игрок
                    if(!players.name.equals(name) && player.Can_SEE_FOOD((float)players.positionX,(float)players.positionY,displayMetrics.widthPixels,displayMetrics.heightPixels)){
                        players.draw(canvas,camera,player,displayMetrics.widthPixels,displayMetrics.heightPixels);
                    }
               }
           }
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
    public void drawName(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(Textsize);

        float textWidth = paint.measureText(name);

        float x = displayMetrics.widthPixels / 2 - textWidth / 2; // Центрируем по X
        float y = displayMetrics.heightPixels / 2 + textY; // Центрируем по Y

        canvas.drawText(name, x, y, paint);
    }

    //обновляем игроком карту и расположение камеры
    public void update() {
        if (player.isEaten) {
            Textsize = 0;
            return;
        }
        camera.update();
        joystick.update();
        for (Bot k : bots) {
            k.update();
        }

        // Обновляем данные игрока
        player.update();
        client.sendPlayerData(Game.getPlayer());
        // Масштаб
        if (player.CalculateScale(displayMetrics.widthPixels, displayMetrics.heightPixels) && map.Cagesize > 100) {
            player.Scale++;
            Textsize *= 1.03;
            textX *= 1.072222222111;
            textY *= 1.06;
            player.radius /= 1.05;
            points.radiusFood /= 1.05;
            map.Cagesize /= 1.1;
            player.MAX_SPEED /= 1.09;
            for (Bot k : bots) {
                k.radius /= 1.05;
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
