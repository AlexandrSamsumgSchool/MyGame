package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bot extends Circle{
    private int dX,dY;//направление рандомное
    private int tick = 0;
    public static  double MAX_SPEED = 0.07;
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
        dX = (int)(Math.random()*200-100);
        dY = (int)(Math.random()*200-100);
    }

    public Bot() {
        super();
    }

    @Override
    public void update() {
        setDirection();
        if(tick == 50) {
            tick-=50;
            velocityX = MAX_SPEED * dX;
            velocityY = MAX_SPEED * dY;
        }
        positionX += velocityX;
        positionY += velocityY;
        tick++;
        Collision();
        MAX_SPEED = 120/(Math.sqrt(radius));
        if(EatenFood>75 && radius>100 ){radius=radius-0.0625;EatenFood-=0.0225;}
        MAX_SPEED = 0.75/(Math.sqrt(radius));

    }
    public Bot[] remove(Bot[] values, int index) {
        // Создаем пустой массив размером на один меньше чем исходный
        // так как мы удаляем один элемент
        Bot[] result = new Bot[values.length - 1];

        for (int i = 0; i < values.length; i++) {
            if (i != index) { // Копируем все кроме index
                // Элементы стоящие дальше index смещаются влево
                int newIndex = i < index ? i : i - 1;
                result[newIndex] = values[i];
            }
        }

        return result;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay, Player player, int wp, int hp) {
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(player.Can_SEE_FOOD((int) positionX, (int) positionY,wp,hp))
            canvas.drawCircle((float) gameDisplay.gameTOdisplaycoordinateX(positionX), (float) gameDisplay.gameTOdisplaycoordinateY(positionY), (float) radius,paint);
    }

}
