package com.example.mygame;

    public class Bot extends Circle{
    private int dX,dY;
    private int tick = 0;
    public static  double MAX_SPEED = 0.07;

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
    }
}
