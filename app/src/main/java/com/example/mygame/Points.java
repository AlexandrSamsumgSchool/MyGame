package com.example.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Points extends Circle {
    public final int MAX_POINTS_FOOD = 750;
    public float radiusFood = 15;
    public float[] FoodX;
    public float[] FoodY;
    public int[] Food_Color;
    private static final int MAX_X = 6000;
    private static final int MAX_Y = 6000;
    Random rand = new Random();

    public Points() {
        FoodX = new float[MAX_POINTS_FOOD];
        FoodY = new float[MAX_POINTS_FOOD];
        Food_Color = new int[MAX_POINTS_FOOD];
        SpawnPoints();
    }
    public void SpawnPoints() {
        for (int i = 0; i < MAX_POINTS_FOOD; i++) {
            FoodX[i] = rand.nextFloat() * MAX_X;
            FoodY[i] = rand.nextFloat() * MAX_Y;
            Food_Color[i] = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }

    public boolean Can_Eat(int PlayerX, int PlayerY, int circleX, int circleY, int R) {
        return (PlayerX - circleX) * (PlayerX - circleX) + (PlayerY - circleY) * (PlayerY - circleY) <= R * R;
    }

    public void Draw(Canvas canvas, CamerA camera, Bot[] bots, MediaPlayer mediaPlayer, MediaPlayer mediaPlayer1, DisplayMetrics displayMetrics, Player player) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        HashMap<String, Bot> playersMap = Game.Players;

        for (int i = 0; i < MAX_POINTS_FOOD; i++) {
            // Player food
            if (Can_Eat((int) player.positionX, (int) player.positionY, (int) FoodX[i], (int) FoodY[i], (int) player.radius)) {
                mediaPlayer.start();
                FoodX[i] = rand.nextFloat() * MAX_X;
                FoodY[i] = rand.nextFloat() * MAX_Y;
                player.radius += player.mass;
                player.EatenFood++;
            }

            // Bot food
            for (Bot bot : bots) {
                if (Can_Eat((int) bot.getPositionX(), (int) bot.getPositionY(), (int) FoodX[i], (int) FoodY[i], (int) bot.radius)) {
                    FoodX[i] = rand.nextFloat() * MAX_X;
                    FoodY[i] = rand.nextFloat() * MAX_Y;
                    bot.radius += bot.mass;
                    bot.EatenFood++;
                }

                if (player.Can_EAT_PL((int) bot.positionX, (int) bot.positionY, (int) bot.radius)) {
                    mediaPlayer1.start();
                    bot.velocityY = 0;
                    bot.velocityX = 0;
                    player.radius = Math.sqrt(player.radius * player.radius + bot.radius * bot.radius);
                    bot.radius = 160;
                    player.EatenFood += bot.EatenFood;
                    bot.EatenFood = 0;
                    bot.positionX = FoodX[(int)Math.random()*900];
                    bot.positionY = FoodY[(int)Math.random()*900];
                }
                if (bot.Can_EAT_PL((int) player.positionX, (int) player.positionY, (int) player.radius)) {
                    player.isEaten = true;
                }
            }

            // Draw food
            paint.setColor(Food_Color[i]);
            if (player.Can_SEE_FOOD(FoodX[i], FoodY[i], displayMetrics.widthPixels, displayMetrics.heightPixels)) {
                canvas.drawCircle((float) camera.gameTOdisplaycoordinateX(FoodX[i]), (float) camera.gameTOdisplaycoordinateY(FoodY[i]), radiusFood, paint);
            }
        }

        if (!playersMap.isEmpty()) {
            for (HashMap.Entry<String, Bot> entry : playersMap.entrySet()) {
                Bot from_server = entry.getValue();
                if (!player.name.equals(from_server.name) && from_server.Can_EAT_PL(player.positionX, player.positionY, player.radius)) {
                    player.isEaten = true;
                    player.radius = 0;
                    Game.client.sendPlayerData(player);
                }
                else if (!from_server.name.equals(player.name) && player.Can_EAT_PL(from_server.positionX, from_server.positionY, from_server.radius)) {
                    mediaPlayer1.start();
                    player.radius += Math.sqrt(player.radius + from_server.radius * from_server.radius);
                    player.EatenFood += from_server.EatenFood;
                }
            }
        }
    }
}



