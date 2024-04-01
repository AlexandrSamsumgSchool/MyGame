package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;


public class Options extends AppCompatActivity {
    AudioManager audioManager;
    CheckBox replace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SeekBar seekBar = findViewById(R.id.seekbar);
        replace = findViewById(R.id.sw);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        Button back;
        back = findViewById(R.id.bt1);
        back.setOnClickListener(v -> {
            Intent i = new Intent(Options.this,Menu.class);
            i.putExtra("KEY",audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            if(replace.isChecked())i.putExtra("Replace",true);
            startActivity(i);
            finish();
        });



    }

}