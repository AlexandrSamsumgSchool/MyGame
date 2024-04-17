package com.example.mygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;

import java.util.Map;
import java.util.Set;


public class Options extends AppCompatActivity {
    AudioManager audioManager;
    int volume ;
    CheckBox replace,FPS;
    boolean FPs,Replace;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        mSettings = getSharedPreferences("setting", Context.MODE_PRIVATE);

        SeekBar seekBar = findViewById(R.id.seekbar);
        replace = findViewById(R.id.sw);
        FPS = findViewById(R.id.fps);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = mSettings.getInt("Volume", 50);


        FPS.setChecked( mSettings.getBoolean("FPS",false));
        replace.setChecked(mSettings.getBoolean("Replace",false));
        seekBar.setProgress(volume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress , boolean fromUser) {
                volume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }
        });
        Button back;
        back = findViewById(R.id.bt1);
        back.setOnClickListener(v -> {
            Intent i = new Intent(Options.this,Menu.class);
            i.putExtra("KEY",audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            if(replace.isChecked())i.putExtra("Replace",true);
            else i.putExtra("Replace",Replace);
            if(FPS.isChecked())i.putExtra("FPS",true);
            else i.putExtra("FPS",FPs);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt("Volume", volume);
            editor.putBoolean("FPS", FPs);
            editor.putBoolean("Replace", Replace);
            editor.apply();
            startActivity(i);
            finish();
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        FPs = FPS.isChecked();
        Replace = replace.isChecked();
        if (mSettings.contains("Volume")) volume = mSettings.getInt("Volume", 50);
        if (mSettings.contains("FPS"))    FPs = mSettings.getBoolean("FPS",false);
        if (mSettings.contains("Replace"))Replace = mSettings.getBoolean("Replace",false);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        FPs = FPS.isChecked();
        Replace = replace.isChecked();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("Volume", volume);
        editor.putBoolean("FPS", FPs);
        editor.putBoolean("Replace", Replace);
        editor.apply();
        audioManager = null;
    }

}