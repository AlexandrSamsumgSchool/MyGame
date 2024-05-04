package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Teach extends AppCompatActivity {
ImageFilterButton teach ;
private final int[] images = {R.drawable.menuteach, R.drawable.joystickteach,R.drawable.growteach,R.drawable.enemyteach,R.drawable.menuteach};
private int imageNum = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        teach = findViewById(R.id.teeach);
        teach.setOnClickListener(v -> nextImage());
    }
    private void nextImage(){
        ++imageNum;
        if(imageNum==4){
            Intent intent = new Intent(Teach.this,Menu.class);
            startActivity(intent);
            finish();
        }
        teach.setBackground(AppCompatResources.getDrawable(getApplicationContext(),images[imageNum]));
    }
}