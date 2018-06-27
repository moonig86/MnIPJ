package mn.com.tpj_mnlibtools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import mn.com.ipj_mnctools.check_exception.MnApp_CheckException;
import mn.com.ipj_mnctools.limitApp.MnLimitApp;
import mn.com.ipj_mnctools.log.MnLog;
import mn.com.ipj_mnctools.permission.MnPermission;
import mn.com.tpj_mnlibtools.gps.MnGPS;

/**
 * 18.05.15 미완성
 * */

public class MnAct_Main extends AppCompatActivity {
    public static final String TAG = "act_main";

    Activity mActivity;
    Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    int mMainMaxHeightDP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        mContext = mActivity.getApplicationContext();

        // lib MnLog
        MnLog.setLog(mContext);
        // lib MnCheckException
        MnApp_CheckException.setCheckException(mContext);


        setContentView(R.layout.mn_act_main);

        //GPS
        //setGPSInfo();
        //LatLng test = MnGPS.getThisLocation(mActivity);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case MnPermission.CHECK_PERMISSION:
                MnPermission.requestPermissionsResult(mActivity, permissions, grantResults);
                break;
        }
    }

    private void setLimitApp(){
        MnLimitApp limitApp = new MnLimitApp(mActivity);
        limitApp.setLimitApp_day("20180523", 30);
    }


    private void setPermission(){
        String[] strArr = new String[]{
                Manifest.permission.CAMERA
        };
        MnPermission.setPermission(strArr);
        MnPermission.checkRequestPermission(mActivity);
        //MnLog.d(TAG, "isCheck : "+isCheck);
    }


    private void setClause(Activity activity, LinearLayout lout_main){
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        MnLog.d(TAG, "width : "+width);
        MnLog.d(TAG, "height : "+height);

        MnClause test = new MnClause(mActivity);
        test.setClausePublic((int)(height*0.5), 10);
        test.setTitle("Main_title", "#000000", 17);
        View.OnClickListener mBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        };
        test.setBtnOk("확인", "#000000", 20, "#f5f5f5", "#ffffff");
        test.startClause();
    }


    private String setTestText(){
        return "content\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\n";
    }


    private void setGPS(){
        MnGPS gps = new MnGPS(mActivity);
        LatLng nowLatLng = gps.getThisLocation();
        gps.setData_apiKey("AIzaSyAbi9NCTxsVy4mHiBZG3785FbtHoid7WOE");
        gps.setData_forPlace_locInfo(nowLatLng, 500);
        gps.setData_forPlace_type("restaurant");
        gps.start_forPlace();
    }
}
