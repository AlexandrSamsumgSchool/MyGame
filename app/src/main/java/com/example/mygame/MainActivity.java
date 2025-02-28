package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
public class MainActivity extends AppCompatActivity {
    private Game game;
    boolean rp;
    boolean fps;
    boolean isLaunch;
    String name;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mSettings = getSharedPreferences("setting", Context.MODE_PRIVATE);
        name = getIntent().getStringExtra("Name");
        if(name.equals("Player") && !mSettings.getString("Name", "Player").equals("Player"))name = mSettings.getString("Name", "Player");
        isLaunch = getIntent().getBooleanExtra("IsL",false);
        if(isLaunch) {
            rp = getIntent().getBooleanExtra("Replace", mSettings.getBoolean("Replace", false));
            fps = getIntent().getBooleanExtra("FPS", mSettings.getBoolean("FPS", false));
        }
        else {
            rp = mSettings.getBoolean("Replace", false);
            fps =  mSettings.getBoolean("FPS", false);
        }
        game = new Game(this,name,rp,fps);
        setContentView(game);
    }

    @Override 
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("FPS", fps);
        editor.putBoolean("Replace", rp);
        editor.putString("Name", name);
        editor.apply();
    }

    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}