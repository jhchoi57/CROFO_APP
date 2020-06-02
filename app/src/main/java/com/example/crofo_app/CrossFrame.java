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

    public void run(CrossSocket[] socket){
//        sock = socket;
//        new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while(isInROI){
//                        try {
//                            sock[0].setCrossFrameROI(CrossFrame.this);
//                            System.out.println("교차로 그리기" + getRoi().getFrontCrosswalk().getCrosswalkLocation()[0]);
//                            Handler mHandler = new Handler(Looper.getMainLooper());
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    deleteAllCrossFrame();
//                                    System.out.println("교차로 지우기");
//                                    initAllCrossFrame();
//                                    System.out.println("교차로 초기화");
//                                    showAllCrossFrame();
//                                    System.out.println("교차로 그리기" + getRoi().getFrontCrosswalk().getCrosswalkLocation()[0]);
//                                }
//                            }, 0);
//                            Thread.sleep(500);
//                        } catch (InterruptedException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
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
//        for(int i = 0;i<viewList.size();i++){
//            ((ViewManager)viewList.get(i).getParent()).removeView(viewList.get(i));
//        }
//        //((ViewManager)iv.getParent()).removeView(iv);
//        viewList.clear();
//
//        if(roi == null) return;
//
//        ArrayList<Pedestrian> pedestrianList;
//        ArrayList<Car> carList;
//        pedestrianList = roi.getFrontCrosswalk().getPedestrianList();
//        carList = roi.getFrontCrosswalk().getCarList();
//        System.out.println(" 보행자 차량 리스트 ");
//        for(int i=0;i<pedestrianList.size();i++){
//            System.out.println(" 보행자 리스트 " + pedestrianList.get(i).getPedestrianLocation()[0] + pedestrianList.get(i).getPedestrianLocation()[1]);
//            addObjFront(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
//                    pedestrianList.get(i).getPedestrianDirection());
//        }
//        for(int i=0;i<carList.size();i++){
//            System.out.println("차량 리스트" + carList.get(i).getCarLocation()[0] + carList.get(i).getCarLocation()[1]);
//            addObjFront(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
//        }
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
        for(int i=0;i<pedestrianList.size();i++){
            addObjFront(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
                    pedestrianList.get(i).getPedestrianDirection());
        }
        for(int i=0;i<carList.size();i++){
            addObjFront(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
        }
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
        for(int i=0;i<pedestrianList.size();i++){
            addObjRight(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
                    pedestrianList.get(i).getPedestrianDirection());
        }
        for(int i=0;i<carList.size();i++){
            addObjRight(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
        }
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
        for(int i=0;i<pedestrianList.size();i++){
            addObjBack(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
                    pedestrianList.get(i).getPedestrianDirection());
        }
        for(int i=0;i<carList.size();i++){
            addObjBack(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
        }
        backdlg.show();
    }

    public void refreshLeftFrame(Crosswalk roi){
        if(roi == null) return;
        for(int i = 0;i<viewLeftList.size();i++){
            ((ViewManager)viewLeftList.get(i).getParent()).removeView(viewLeftList.get(i));
        }
        //((ViewManager)iv.getParent()).removeView(iv);
        viewLeftList.clear();



        ArrayList<Pedestrian> pedestrianList;
        ArrayList<Car> carList;
        pedestrianList = roi.getPedestrianList();
        carList = roi.getCarList();
        for(int i=0;i<pedestrianList.size();i++){
            addObjLeft(pedestrianList.get(i).getPedestrianLocation()[0], pedestrianList.get(i).getPedestrianLocation()[1], 0,
                    pedestrianList.get(i).getPedestrianDirection());
        }
        for(int i=0;i<carList.size();i++){
            addObjLeft(carList.get(i).getCarLocation()[0], carList.get(i).getCarLocation()[1], 1, -1);
        }
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
        viewFrontList.add(iv);
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
        viewRightList.add(iv);
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
        viewBackList.add(iv);
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
        viewLeftList.add(iv);
    }

    public boolean getIsInROI() {
        return isInROI;
    }

    public void setInROI(boolean inROI) {
        isInROI = inROI;
    }

}
