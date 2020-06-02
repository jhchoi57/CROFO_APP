package com.example.crofo_app;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CrossFrame {
    private Context context;
    public Dialog frontdlg = null;
    public Dialog backdlg = null;
    public Dialog leftdlg = null;
    public Dialog rightdlg = null;
    private CrossInfo roi;
    private ArrayList<ImageView> viewList;

    public CrossInfo getRoi() {
        return roi;
    }

    public void setRoi(CrossInfo roi) {
        this.roi = roi;
    }

    public CrossFrame(Context context) {
        this.context = context;
        roi = null;
        viewList = new ArrayList<ImageView>();
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callCrossFront() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        frontdlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        frontdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        frontdlg.setContentView(R.layout.crossframefront);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = frontdlg.getWindow().getAttributes();
        params.x = 250;
        params.y = 800 + 300;
        // params.width = 300;
        // params.height = 300;
        frontdlg.getWindow().setAttributes(params);
        frontdlg.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);

        // 배경 어두워지는거 없애기
        frontdlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        //frontdlg.show();
    }

    public void callCrossBack() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        backdlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        backdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        backdlg.setContentView(R.layout.crossframeback);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = backdlg.getWindow().getAttributes();
        params.x = 250;
        params.y = 300;
        // params.width = 300;
        // params.height = 300;
        backdlg.getWindow().setAttributes(params);
        backdlg.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);

        // 배경 어두워지는거 없애기
        backdlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        //backdlg.show();
    }

    public void callCrossLeft() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        leftdlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        leftdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        leftdlg.setContentView(R.layout.crossframeleft);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = leftdlg.getWindow().getAttributes();
        // params.x = 500;
        params.y = 300 + 300;
        // params.width = 300;
        // params.height = 300;
        leftdlg.getWindow().setAttributes(params);
        leftdlg.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);

        // 배경 어두워지는거 없애기
        leftdlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        //leftdlg.show();
    }

    public void callCrossRight() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        rightdlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        rightdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        rightdlg.setContentView(R.layout.crossframeright);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = rightdlg.getWindow().getAttributes();
        params.x = 800;
        params.y = 300 + 300;
        // params.width = 300;
        // params.height = 300;
        rightdlg.getWindow().setAttributes(params);
        rightdlg.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);

        // 배경 어두워지는거 없애기
        rightdlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        //rightdlg.show();
    }

    public void initAllCrossFrame(){
        callCrossBack();
        callCrossFront();
        callCrossLeft();
        callCrossRight();
    }

    public void showAllCrossFrame(){
        for(int i = 0;i<viewList.size();i++){
            ((ViewManager)viewList.get(i).getParent()).removeView(viewList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewList.clear();

        if(roi == null) return;

        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getFrontCrosswalk().getPedestrianList();
        carList = roi.getFrontCrosswalk().getCarList();
        System.out.println(" 보행자 차량 리스트 ");
        for(int i=0;i<pedestrianList.size();i++){
            System.out.println(" 보행자 리스트 " + pedestrianList.get(i).getPedestrianLocation()[0] + pedestrianList.get(i).getPedestrianLocation()[1]);
            addObjFront(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
                    pedestrianList.get(i).getPedestrianDirection());
        }
        for(int i=0;i<carList.size();i++){
            System.out.println("차량 리스트" + carList.get(i).getCarLocation()[0] + carList.get(i).getCarLocation()[1]);
            addObjFront(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
        }
        frontdlg.show();
        backdlg.show();
        rightdlg.show();
        leftdlg.show();
    }

    public void showTwoCrossFrame(){
        rightdlg.show();
        backdlg.show();
    }

    public void deleteAllCrossFrame(){
        try {
            frontdlg.dismiss();
            backdlg.dismiss();
            rightdlg.dismiss();
            leftdlg.dismiss();
        } catch (Exception e){
            Log.e("dismiss error", String.valueOf(e));
        }

    }

    public CrossInfo getROIInfo(CrossSocket sock){
        CrossInfo roi = null;
        sock.connect(); // 노드 서버 소켓과 연결
        sock.run(); // 메시지 수신
        //sock.disconnect();  // 소켓 해제
        return roi;
    }

    public void addObjFront(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        // 오브젝트 받아서 if로 나누면 댐
        iv.setImageResource(R.drawable.person);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 50;
        param.height = 50;
        param.setMargins(left,top,0,0);
        frontdlg.addContentView(iv, param);
        viewList.add(iv);
    }

    public void addObjRight(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        // 오브젝트 받아서 if로 나누면 댐
        iv.setImageResource(R.drawable.person);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 50;
        param.height = 50;
        param.setMargins(left,top,0,0);
        rightdlg.addContentView(iv, param);
        viewList.add(iv);
    }

    public void addObjBack(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        // 오브젝트 받아서 if로 나누면 댐
        iv.setImageResource(R.drawable.person);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 50;
        param.height = 50;
        param.setMargins(left,top,0,0);
        backdlg.addContentView(iv, param);
        viewList.add(iv);
    }

    public void addObjLeft(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        // 오브젝트 받아서 if로 나누면 댐
        iv.setImageResource(R.drawable.person);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 50;
        param.height = 50;
        param.setMargins(left,top,0,0);
        leftdlg.addContentView(iv, param);
        viewList.add(iv);
    }

}
