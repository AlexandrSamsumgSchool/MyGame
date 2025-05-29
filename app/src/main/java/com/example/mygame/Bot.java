package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Bot extends Circle{
    String name;
    public double mass = 2;
    private int dX,dY;//направление, рандомное
    private int tick = 0;
    public static  double MAX_SPEED = 7;
    public int EatenFood = 0;
    public static final int Bots = 0;
    private int Textsize = 60,textX = 14,textY = 5;
    public static final int MAX_TEXT_SIZE = 100; // Максимальный размер текста
    public static final int MIN_TEXT_SIZE = 5; // Минимальный размер текста
    public Bot(int color, double positionX, double positionY, double radius,String name) {
        super(color, positionX, positionY, radius);
        if(name!= null)this.name = name;
        else this.name = "Bot";

    }
    public static int SpawnBotX(){
       return (int)(Math.random()*6000);
   }
    public static int SpawnBotY(){
            return (int)(Math.random()*6000);
    }
    public void setDirection(){
        dX = (int)(Math.random()*4-2);
        dY = (int)(Math.random()*4-2);
    }

    public Bot() {
        super();
    }

    @Override
    public void update() {
        setDirection();
        if (tick == 50) {
            tick -= 50;
            velocityX = MAX_SPEED * dX;
            velocityY = MAX_SPEED * dY;
        }

        positionX += velocityX;
        positionY += velocityY;
        tick++;
        Collision();
        if (EatenFood > 75 && radius > 100) {
            radius = radius - 0.0625;
            EatenFood -= 0.0225;
            Textsize = Math.max(Textsize - 1, MIN_TEXT_SIZE); // Уменьшение размера текста
        }
        if(positionX > 6000 || positionY > 6000){
            positionX = rand.nextFloat()*6000;
            positionY = rand.nextFloat()*6000;

        }
        if (Game.getDisplayMetrics().widthPixels/4 <= radius || Game.getDisplayMetrics().heightPixels/4<=radius) {
            if (Textsize < MAX_TEXT_SIZE) {
                Textsize *= 1.05;
                mass/=1.05;
            }
            textX *= 1.072222222111;
            textY *= 1.06;
        }
    }
    public void draw(Canvas canvas, CamerA camera, Player player, int wp, int hp) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(player.Can_SEE_FOOD((int) positionX, (int) positionY,wp,hp)) {
            canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(positionX), (float) camera.gameTOdisplaycoordinateY(positionY), (float) radius, paint);
//            canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(player.positionX + distanceToPl_x), (float) camera.gameTOdisplaycoordinateY(player.positionY + distanceToPl_y), (float) radius, paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(Textsize);

            float textWidth = paint.measureText(name);

            float x = (float) (positionX - textWidth / 2); // Центрируем по X
            float y = (float) (positionY + textY); // Центрируем по Y

            canvas.drawText(name, (float) camera.gameTOdisplaycoordinateX(x), (float) camera.gameTOdisplaycoordinateY(y), paint);
        }
    }
}
