package com.example.mygame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
public class GameLoop extends Thread{
    private boolean isRunning = false;
    private  SurfaceHolder surfaceHolder;
    private Game game;
    private double averageFPS;
    public  long startTime;
    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }
    @Override
    public void run() {
        Canvas canvas = null;
        super.run();
        int frameCount =0;
        long elapsedTime;
        startTime = System.currentTimeMillis();
        while(isRunning){

            try {
                canvas = surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    game.update();
                    game.draw(canvas);
                }
            }
            catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            finally{
                if(canvas!=null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }
            elapsedTime = System.currentTimeMillis()- startTime;
            if(elapsedTime>=1000){
                    averageFPS =frameCount/(0.001 * elapsedTime);
                    frameCount = 0;
                    startTime =System.currentTimeMillis();

            }
        }
    }
    public void stopLoop() {
        isRunning = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
