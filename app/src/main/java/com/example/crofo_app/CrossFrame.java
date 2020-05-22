package com.example.crofo_app;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class CrossFrame {
    private Context context;

    public CrossFrame(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callCrossFront() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.crossframefront);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = dlg.getWindow().getAttributes();
        // params.x = 100;
        // params.y = 500;
        // params.width = 300;
        // params.height = 300;
        dlg.getWindow().setAttributes(params);
        dlg.getWindow().setGravity(Gravity.CENTER);

        // 배경 어두워지는거 없애기
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        dlg.show();
    }

    public void callCrossRight() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.crossframeright);

        // 커스텀 다이얼로그 위치 조정
        LayoutParams params = dlg.getWindow().getAttributes();
        // params.x = 500;
        // params.y = 100;
        // params.width = 300;
        // params.height = 300;
        dlg.getWindow().setAttributes(params);
        dlg.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);

        // 배경 어두워지는거 없애기
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 커스텀 다이얼로그를 노출한다
        dlg.show();
    }
}
