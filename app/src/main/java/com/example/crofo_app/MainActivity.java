package com.example.crofo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    TMapView tMapView;
    TMapMarkerItem markerItem1 = new TMapMarkerItem();
    TMapPoint tMapMarkerPoint = new TMapPoint(37.570841, 126.985302); // 임의로 찍어둠 : SKT타워
    TMapPoint startPoint = new TMapPoint(0,0);
    TMapPoint endPoint = new TMapPoint(0,0);
    TMapGpsManager gps = null;
    Button btnStarting;
    Button btnDestination;
    Button btnFinish;
    Button btnCurrentLocationToStarting;
    TextToSpeech tts;
    TimerTask timerTask;

    @Override
    public void onLocationChange(Location location){
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
    }

    @SuppressLint("SourceLockedOrientationActivity")        //빨간줄무시
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 로딩창 띄우기
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);

        // 세로모드고정, activity_main View 띄우기
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // 위치권한 탐색 허용 관련, 현재 위치 받아오기
        acceptLocationPermission();
        setGps();
        receiveLocation();

        //tts 한국어로 초기화, TMAP 띄우기
        initTTS();
        setTMAP();

        // 시작 중심 좌표, 시작 현위치로
        tMapView.setTrackingMode(true);
        Toast.makeText(getApplicationContext(), "현재 위치 : " + tMapView.getLocationPoint(), Toast.LENGTH_LONG).show();

        // 마커 아이콘 지정, 버튼 설정, tMapView 클릭 이벤트
        setMarkerIcon();
        setButton();
        tMapViewClickEvent();
        tMapViewLongClickEvent();

        // 커스텀 다이얼로그를 호출한다.
        CrossFrame crossFrame = new CrossFrame(MainActivity.this);
        crossFrame.callFunction();

        //startTimerTask();
    }

    // onCreate 끝

    //함수들 시작

    // 버튼 이벤트
    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){
                // 출발지 설정 버튼
                case R.id.btnStart :
                    startPoint = tMapMarkerPoint;
                    Toast.makeText(getApplicationContext(), "위도 : " + startPoint.getLatitude() + "\n경도 : " + startPoint.getLongitude(), Toast.LENGTH_LONG).show();
                    break;

                // 도착지 설정 버튼
                case R.id.btnEnd :
                    endPoint = tMapMarkerPoint;
                    Toast.makeText(getApplicationContext(), "위도 : " + endPoint.getLatitude() + "\n경도 : " + endPoint.getLongitude(), Toast.LENGTH_LONG).show();

                    // 출발지가 0 0 이 아니면 네비게이션 시작
                    if(startPoint.getLongitude() != 0 && startPoint.getLatitude() != 0){
                        Navigation navigation = new Navigation(startPoint, endPoint, tMapView);
                        navigation.execute(startPoint, endPoint);
                        btnFinish.setVisibility(View.VISIBLE);

                        //tts
                        CrossAlert crossAlert = new CrossAlert(null);
                        tts.speak(crossAlert.getAlertSoundFront(),TextToSpeech.QUEUE_FLUSH, null);
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "출발지가 없어용" , Toast.LENGTH_LONG).show();
                    }
                    break;

                // 길찾기 종료 버튼
                case R.id.btnFinish :
                    btnStarting.setVisibility(View.INVISIBLE);
                    btnDestination.setVisibility(View.INVISIBLE);
                    btnFinish.setVisibility(View.INVISIBLE);

                    // 모든 마커 삭제
                    tMapView.removeAllMarkerItem();

                    // 모든 경로 선 삭제
                    tMapView.removeAllTMapPolyLine();

                    // 나침반 모드 없애주고
                    tMapView.setCompassMode(false);

                    // 화면 확대되었던거 약간 축소
                    tMapView.setZoomLevel(16);

                    // 출발지, 목적지 초기화
                    startPoint = new TMapPoint(0,0);
                    endPoint = new TMapPoint(0,0);

                    // 출발지, 목적지 초기화 됐는지 출력 해볼게용
                    Toast.makeText(getApplicationContext(), "출발지 " + startPoint.getLatitude() + ", " + startPoint.getLongitude() +
                            "\n도착지 " + endPoint.getLatitude() + ", " + endPoint.getLongitude(), Toast.LENGTH_LONG).show();

                    break;

                // 현재 위치를 출발지로 설정해주는 버튼
                case R.id.btnCurrentLocationToStarting:

                    // 출발지를 현재 위치로
                    startPoint = gps.getLocation();
                    tMapView.setCenterPoint(startPoint.getLongitude(), startPoint.getLatitude());
                    Toast.makeText(getApplicationContext(), "위도 : " + startPoint.getLatitude() + "\n경도 : " + startPoint.getLongitude(), Toast.LENGTH_LONG).show();

                    break;

            }
        }
    };

    public void setMarkerIcon(){
        Context context = this;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poi_dot);
        markerItem1.setIcon(bitmap);
    }

    public void tMapViewClickEvent(){
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback(){
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });
    }

    public void tMapViewLongClickEvent(){
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                tMapMarkerPoint = tMapPoint;
                markerItem1.setTMapPoint( tMapMarkerPoint ); // 마커의 좌표 지정
                markerItem1.setName(""); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                btnStarting.setVisibility(View.VISIBLE);
                btnDestination.setVisibility(View.VISIBLE);
                //btnFinish.setVisibility(View.VISIBLE);
            }
        });
    }

    public void acceptLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return;
        }
    }

    public void receiveLocation(){
        // 현재 위치 받아오기
        gps = new TMapGpsManager(MainActivity.this);
        gps.setMinTime(1000);
        gps.setMinDistance(2);
        // 현재 위치 인터넷으로 받기 gps 면 GPS_PROVIDER
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();
    }


    public void startTimerTask(){
        timerTask = new TimerTask(){
            @Override
            public void run(){
                Toast.makeText(getApplicationContext(),"런런런", Toast.LENGTH_LONG).show();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,5000);
    }

    public void setButton(){
        // 버튼 ID 찾기
        this.btnStarting = (Button)findViewById(R.id.btnStart);
        this.btnDestination = (Button)findViewById(R.id.btnEnd);
        this.btnFinish = (Button)findViewById(R.id.btnFinish);
        this.btnCurrentLocationToStarting = (Button)findViewById(R.id.btnCurrentLocationToStarting);

        // 버튼 Listener
        this.btnStarting.setOnClickListener(mClickListener);
        this.btnDestination.setOnClickListener(mClickListener);
        this.btnFinish.setOnClickListener(mClickListener);
        this.btnCurrentLocationToStarting.setOnClickListener(mClickListener);
    }

    public void initTTS(){
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    public void setTMAP(){
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( "l7xx57390f539bd74175a4783fe65224453e" );
        linearLayoutTmap.addView( tMapView );
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                System.out.println("TmapTest" +  Double.toString(longitude));
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,mLocationListener);
    }

}