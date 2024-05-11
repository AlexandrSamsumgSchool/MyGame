package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bot extends Circle{
    private int dX,dY;//направление рандомное
    private int tick = 0;
    public static  double MAX_SPEED = 7;
    public int EatenFood = 0;
    public static final int Bots = 25;
    public Bot(int color, double positionX, double positionY, double radius) {
        super(color, positionX, positionY, radius);
    }
   public int SpawnBotX(){
       return (int)(Math.random()*10000);

   }
    public int SpawnBotY(){
            return (int)(Math.random()*10000);
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
        }


    }
    public void draw(Canvas canvas, CamerA camera, Player player, int wp, int hp) {
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(player.Can_SEE_FOOD((int) positionX, (int) positionY,wp,hp))
            canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(positionX), (float) camera.gameTOdisplaycoordinateY(positionY), (float) radius,paint);

    }

}
