package com.example.crofo_app;

import android.media.Image;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;

public class CrossAlert extends AppCompatActivity {
    private Image alertImage;
    private String alertSoundFront = "전방 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private String alertSoundRight = "우측 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private TextToSpeech tts;
    private Timer timer;
    private TimerTask timerTask;

    public CrossAlert(Image aI){ this.alertImage = aI; }

    public Image getAlertImage(){
        return this.alertImage;
    }

    public void setAlertImage(Image aI){
        this.alertImage = aI;
    }

    public TextToSpeech getTts(){
        return this.tts;
    }

    public String getAlertSoundFront(){
        return this.alertSoundFront;
    }

    public String getAlertSoundRight(){
        return this.alertSoundRight;
    }

    public void startTimerTask(){
        timerTask = new TimerTask(){
            int second = 10;
            @Override
            public void run(){
                if(second%2==0){
                    Toast.makeText(getApplicationContext(), "짝수:이미지 호출" , Toast.LENGTH_LONG).show();
                    second--;
                }
                else if(second%2==1){
                    Toast.makeText(getApplicationContext(), "홀수:이미지 제거" , Toast.LENGTH_LONG).show();
                    second--;
                }
            }
        };
    }
}
