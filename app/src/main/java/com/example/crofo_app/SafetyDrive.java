package com.example.crofo_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class SafetyDrive extends AsyncTask<TMapPoint, Void, Void> {

    public SafetyDrive(TMapPoint sPoint, TMapPoint ePoint, TMapView tView){
        super();
    }

    protected void onPostExecute() {
        super.onPreExecute();
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(TMapPoint... tMapPoints){                       //실행부분

        return null;
    }

}
