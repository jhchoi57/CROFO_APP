package com.example.crofo_app;

import android.media.Image;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class CrossAlert extends AppCompatActivity {
    private Image alertImage;
    private String alertSoundFront = "전방 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private String alertSoundRight = "우측 횡단보도에 보행자 및 차량이 있습니다. 주의해주세요.";
    private TextToSpeech tts;

    public CrossAlert(Image aI){
        this.alertImage = aI;
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
}
