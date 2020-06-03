package com.example.crofo_app;

import android.content.Context;
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

public class CrossAlert{
    private Image alertImage;
    private String alertSoundFront = "전방 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private String alertSoundRight = "우측 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private String alertSound = "전방 교차로에 보행자 및 차량이 있습니다. 주의해주세요.";
    private TextToSpeech tts;
    private Timer timer;
    private TimerTask timerTask;
    private Context context;
    private boolean isAlert = false;

    public CrossAlert(Context ct){
        context = ct;
        isAlert = false;
    }

    public boolean getIsAlert(){
        return isAlert;
    }

    public void setIsAlertTrue(){
        isAlert = true;
    }
    public void setIsAlertFalse(){
        isAlert = false;
    }

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

    public void alertSoundFront(){
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(alertSoundFront,TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    public void alertSoundRight(){
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(alertSoundRight,TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    public void alertSound(){
        if(isAlert) return;

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(alertSound,TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

}
