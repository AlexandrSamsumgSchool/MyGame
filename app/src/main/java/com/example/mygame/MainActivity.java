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

SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mSettings = getSharedPreferences("setting", Context.MODE_PRIVATE);
        String name= getIntent().getStringExtra("Name");
        rp = getIntent().getBooleanExtra("Replace",mSettings.getBoolean("Replace",false));
        fps = getIntent().getBooleanExtra("FPS", mSettings.getBoolean("Replace",false));
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
        //
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("FPS", fps);
        editor.putBoolean("Replace", rp);
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