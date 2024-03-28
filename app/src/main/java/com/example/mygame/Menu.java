package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button play,quit,options;
    MediaPlayer mediaPlayer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        int h = getIntent().getIntExtra("KEY",0);
        if(h==0) {
            mediaPlayer = MediaPlayer.create(this, R.raw.menu);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

        }

        play = findViewById(R.id.play);
        quit = findViewById(R.id.quit);
        options = findViewById(R.id.options);
        play.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this,MainActivity.class);
            startActivity(i);
            mediaPlayer.stop();
        });
        quit.setOnClickListener(v -> {
            finishAndRemoveTask(); mediaPlayer.stop();
        });
        options.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this,Options.class);
            startActivity(i);
        });


    }
}