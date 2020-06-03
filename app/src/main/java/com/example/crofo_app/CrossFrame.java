package com.example.crofo_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
    private ArrayList<ImageView> viewFrontList;
    private ArrayList<ImageView> viewRightList;
    private ArrayList<ImageView> viewBackList;
    private ArrayList<ImageView> viewLeftList;
    private boolean stop = false;
    private CrossSocket[] sock;

    private boolean isInROI = false;

    public CrossInfo getRoi() {
        return roi;
    }

    public void setRoi(CrossInfo roi) {
        this.roi = roi;
    }

    public CrossFrame(Context context) {
        this.context = context;
        roi = null;
        viewFrontList = new ArrayList<ImageView>();
        viewRightList = new ArrayList<ImageView>();
        viewBackList = new ArrayList<ImageView>();
        viewLeftList = new ArrayList<ImageView>();
        stop = false;
        isInROI = false;
    }

    public void stop(){
        isInROI = false;
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
        params.x = 225;
        params.y = 350;
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
        params.x = 225;
        params.y = 900 + 350;
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
        params.y = 350 + 350;
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
        params.x = 900;
        params.y = 350 + 350;
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

        frontdlg.show();
        backdlg.show();
        rightdlg.show();
        leftdlg.show();
    }

    public void refreshFrontFrame(Crosswalk roi){
        if(roi == null) return;
        for(int i = 0;i<viewFrontList.size();i++){
            ((ViewManager)viewFrontList.get(i).getParent()).removeView(viewFrontList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewFrontList.clear();

        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getPedestrianList();
        carList = roi.getCarList();
//        for(int i=0;i<pedestrianList.size();i++){
//            System.out.println(" front 에 찍히는 거 " + pedestrianList.get(i).getPedestrianLocation()[0]+ "  " + pedestrianList.get(i).getPedestrianLocation()[1]);
//            addObjFront(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
//                    pedestrianList.get(i).getPedestrianDirection());
//        }
//        for(int i=0;i<carList.size();i++){
//            System.out.println(" front 에 찍히는 거 " + pedestrianList.get(i).getPedestrianLocation()[0]+ "  " + pedestrianList.get(i).getPedestrianLocation()[1]);
//            addObjFront(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
//        }

        addObjFront(0, 0, 0, 0);
        addObjFront(500, 300, 0, 1);
        frontdlg.show();
    }

    public void refreshRightFrame(Crosswalk roi){
        if(roi == null) return;
        for(int i = 0;i<viewRightList.size();i++){
            ((ViewManager)viewRightList.get(i).getParent()).removeView(viewRightList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewRightList.clear();



        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getPedestrianList();
        carList = roi.getCarList();
//        for(int i=0;i<pedestrianList.size();i++){
//            addObjRight(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
//                    pedestrianList.get(i).getPedestrianDirection());
//        }
//        for(int i=0;i<carList.size();i++){
//            addObjRight(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
//        }
        addObjRight(0, 0, 0, 0);
        addObjRight(300, 500, 0, 1);
        rightdlg.show();
    }

    public void refreshBackFrame(Crosswalk roi){
        if(roi == null) return;
        for(int i = 0;i<viewBackList.size();i++){
            ((ViewManager)viewBackList.get(i).getParent()).removeView(viewBackList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewBackList.clear();



        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getPedestrianList();
        carList = roi.getCarList();
//        for(int i=0;i<pedestrianList.size();i++){
//            System.out.println(" back 에 찍히는 거 " + pedestrianList.get(i).getPedestrianLocation()[0]+ "  " + pedestrianList.get(i).getPedestrianLocation()[1]);
//            addObjBack(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
//                    pedestrianList.get(i).getPedestrianDirection());
//        }
//        for(int i=0;i<carList.size();i++){
//            addObjBack(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
//        }
        addObjBack(0, 0, 0, 0);
        addObjBack(500, 300, 0, 1);
        backdlg.show();
    }

    public void refreshLeftFrame(Crosswalk roi){
        if(roi == null) return;
        System.out.println(" 리프레쉬에 객체 있나 봅시다 " + "  " + viewLeftList.size());
        for(int i = 0;i<viewLeftList.size();i++){
            ((ViewManager)viewLeftList.get(i).getParent()).removeView(viewLeftList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewLeftList.clear();
        System.out.println(" 리프레쉬 초기화 됐나 봅시다 " + "  " + viewLeftList.size());


        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getPedestrianList();
        carList = roi.getCarList();
//        for(int i=0;i<pedestrianList.size();i++){
//            addObjLeft(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
//                    pedestrianList.get(i).getPedestrianDirection());
//        }
//        for(int i=0;i<carList.size();i++){
//            addObjLeft(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
//        }
        addObjLeft(0, 0, 0, 0);
        addObjLeft(300, 500, 0, 1);
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

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 60;
        param.height = 60;

        if(obj == 0){
            if(direction == 0){
                iv.setImageResource(R.drawable.person);
            }
            else{
                iv.setImageResource(R.drawable.person_direction);
                param.width = 100;
                param.height = 60;
                if(direction == 1){
                    iv.setRotation(-180.0f);
                }
            }
        }

        else{
            iv.setImageResource(R.drawable.car);
        }
        // 오브젝트 받아서 if로 나누면 댐


        param.setMargins(convertMargin500(left, param.width),convertMargin300(top, param.height),0,0);

        frontdlg.addContentView(iv, param);
        viewFrontList.add(iv);
    }

    public void addObjRight(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 60;
        param.height = 60;

        if(obj == 0){
            if(direction == 0){
                iv.setImageResource(R.drawable.person);
            }
            else{
                iv.setImageResource(R.drawable.person_direction);
                param.width = 60;
                param.height = 100;
                if(direction == 1){
                    iv.setRotation(-90.0f);
                }
                if(direction == -1){
                    iv.setRotation(90.0f);
                }
            }
        }

        else{
            iv.setImageResource(R.drawable.car);
        }

        // 오브젝트 받아서 if로 나누면 댐
        System.out.println(" 컨버트 " + convertMargin300(left, param.height) + " " + convertMargin500(top, param.width));
        param.setMargins(convertMargin300(left, param.width),convertMargin500(top, param.width),0,0);
        rightdlg.addContentView(iv, param);
        viewRightList.add(iv);
    }

    public void addObjBack(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 60;
        param.height = 60;

        // 오브젝트 받아서 if로 나누면 댐
        if(obj == 0){
            if(direction == 0){
                iv.setImageResource(R.drawable.person);
            }
            else{
                iv.setImageResource(R.drawable.person_direction);
                param.width = 100;
                param.height = 60;
                if(direction == -1){
                    iv.setRotation(-180.0f);
                }
            }
        }

        else{
            iv.setImageResource(R.drawable.car);
        }

        param.setMargins(convertMargin500(left, param.width),convertMargin300(top, param.height),0,0);
        backdlg.addContentView(iv, param);
        viewBackList.add(iv);
    }

    public void addObjLeft(int left, int top, int obj, int direction){
        ImageView iv = new ImageView(context);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width = 60;
        param.height = 60;

        // 오브젝트 받아서 if로 나누면 댐
        if(obj == 0){
            if(direction == 0){
                iv.setImageResource(R.drawable.person);
            }
            else{
                iv.setImageResource(R.drawable.person_direction);
                param.width = 100;
                param.height = 60;
                if(direction == 1){
                    iv.setRotation(90.0f);
                }
                if(direction == -1){
                    iv.setRotation(-90.0f);
                }
            }
        }

        else{
            iv.setImageResource(R.drawable.car);
        }
        param.setMargins(convertMargin300(left, param.height),convertMargin500(top, param.width),0,0);
        leftdlg.addContentView(iv, param);
        viewLeftList.add(iv);
    }

    public boolean getIsInROI() {
        return isInROI;
    }

    public void setInROI(boolean inROI) {
        isInROI = inROI;
    }

    public int convertMargin500(int margin, int param){
        // 500 : 500 - param = margin : convert
        int convert = 0;
        convert = (margin * (500 - param)) / 500;
        return convert;
    }

    public int convertMargin300(int margin, int param){
        // 300 : 300 - param = margin : convert
        int convert = 0;
        convert = (margin * (300 - param)) / 300;
        return convert;
    }

}
