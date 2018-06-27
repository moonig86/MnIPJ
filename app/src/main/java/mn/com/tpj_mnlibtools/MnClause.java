package mn.com.tpj_mnlibtools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mn.com.ipj_mnctools.log.MnLog;
import mn.com.ipj_mnctools.resource.MnResource;

/**
 * Created by Ingeol Moon on 2018-05-11.
 */

public class MnClause {
    public static final String TAG = "clause";

    private Activity mActivity;
    private Context mContext;

    private LinearLayout loutClause;
    private LinearLayout loutMain;

    // title
    private String mTitle;                                  // title text
    private String mTitleColor;                             // title text color
    private int mTitleSize;                                 // title text size

    // clause box public
    private int mMaxHeight;                                 // box max height
    private int mClauseBoxCnt;                              // box cnt

    // lout btn ok
    private String mOkPressFalseColor;                      // lout ok pressfalse color
    private String mOkPressTrueColor;                       // lout ok presstrue color
    private String mOk;                                     // btn ok text
    private String mOkColor;                                // btn ok text color
    private int mOkSize;                                    // btn ok text size

    // clause dlg
    private AlertDialog mClauseDlg;


    public MnClause(Activity activity){

        this.mActivity = activity;
        this.mContext = mActivity.getApplicationContext();

        /*loutClauseMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Resources resources = mContext.getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                mClauseHeight = metrics.heightPixels;
                MnLog.d("TAG", mClauseHeight+"");
                //mMainMaxHeightDP = (int)(metrics.heightPixels/(metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
            }
        });*/
    }


    public void setClausePublic(int height, int clause_box_cnt){
        this.mMaxHeight = height;
        this.mClauseBoxCnt = clause_box_cnt;

        MnLog.d(TAG, "mMaxHeight : "+mMaxHeight);
    }


    public void setTitle(String txt_title){
        this.mTitle = txt_title;
    }


    public void setTitle(String txt_title, String txt_title_color, int txt_title_size){
        this.mTitle = txt_title;
        this.mTitleColor = txt_title_color;
        this.mTitleSize = txt_title_size;
    }


    public void setBtnOk(String btn_text, String btn_text_color, int btn_text_size, String pressfalse_color, String presstrue_color){
        this.mOk = btn_text;
        this.mOkColor = btn_text_color;
        this.mOkSize = btn_text_size;
        this.mOkPressFalseColor = pressfalse_color;
        this.mOkPressTrueColor = presstrue_color;
    }


    public void startClause(){
        LayoutInflater infoInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = infoInflater.inflate(MnResource.getLayout(mContext, "mn_dlg_clause"), null);

        loutClause = (LinearLayout)view.findViewById(MnResource.getID(mContext, "lout_dlg_clause"));
        LinearLayout.LayoutParams clauseParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loutClause.setLayoutParams(clauseParams);
        loutClause.setOrientation(LinearLayout.VERTICAL);

        // main(weight : 1)
        loutMain = new LinearLayout(mContext);
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        loutMain.setLayoutParams(mainParams);
        loutMain.setWeightSum(mClauseBoxCnt);
        loutMain.setPadding(getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10));
        loutMain.setOrientation(LinearLayout.VERTICAL);

        // main title
        TextView txtMainTitle = new TextView(mContext);
        LinearLayout.LayoutParams txtTitleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtTitleParams.setMargins(0, 0, 0, getPxToDp_LayoutParams(10));
        txtMainTitle.setLayoutParams(txtTitleParams);
        txtMainTitle.setText(mTitle);
        txtMainTitle.setTextColor(Color.parseColor(mTitleColor));
        txtMainTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTitleSize);

        loutMain.addView(txtMainTitle);

        for(int i=0; i<mClauseBoxCnt; i++){
            LinearLayout loutBox = new LinearLayout(mContext);
            //getPxToDp_LayoutParams(mLoutHeight)
            LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            boxParams.setMargins(0, 0, 0, getPxToDp_LayoutParams(10));
            loutBox.setLayoutParams(boxParams);
            loutBox.setPadding(getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10));
            loutBox.setOrientation(LinearLayout.VERTICAL);
            loutBox.setBackgroundColor(Color.parseColor("#f5f5f5"));

            //


            // accview
            loutMain.addView(loutBox);


            /*LinearLayout loutBox = new LinearLayout(mContext);
            //getPxToDp_LayoutParams(mLoutHeight)
            LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            boxParams.setMargins(0, 0, 0, getPxToDp_LayoutParams(10));
            loutBox.setLayoutParams(boxParams);
            loutBox.setPadding(getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10), getPxToDp_LayoutParams(10));
            loutBox.setOrientation(LinearLayout.VERTICAL);
            loutBox.setBackgroundColor(Color.parseColor("#f5f5f5"));

            // box_title
            TextView txtBoxTitle = new TextView(mContext);
            LinearLayout.LayoutParams boxTitleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtBoxTitle.setLayoutParams(boxTitleParams);
            txtBoxTitle.setGravity(Gravity.CENTER_VERTICAL);
            txtBoxTitle.setText("test_box_title");
            txtBoxTitle.setTextColor(Color.parseColor("#000000"));
            txtBoxTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

            // box_title_topline
            LinearLayout loutTopLine = new LinearLayout(mContext);
            LinearLayout.LayoutParams topLineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            topLineParams.setMargins(0, getPxToDp_LayoutParams(5), 0, getPxToDp_LayoutParams(5));
            loutTopLine.setLayoutParams(topLineParams);
            loutTopLine.setBackgroundColor(Color.parseColor("#cccccc"));

            // box_scrollView
            ScrollView scrollBox = new ScrollView(mContext);
            LinearLayout.LayoutParams boxScrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollBox.setLayoutParams(boxScrollParams);
            scrollBox.setPadding(getPxToDp_LayoutParams(5), 0, getPxToDp_LayoutParams(5), 0);
            scrollBox.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            // box_Content
            TextView txtBoxContent = new TextView(mContext);
            LinearLayout.LayoutParams boxContentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            txtBoxContent.setLayoutParams(boxContentParams);
            txtBoxContent.setGravity(Gravity.CENTER_VERTICAL);
            //txtBoxContent.setText("test");
            txtBoxContent.setText("content\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\n");
            txtBoxContent.setTextColor(Color.parseColor("#000000"));
            txtBoxContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

            // lout checkbox
            LinearLayout loutCheckBox = new LinearLayout(mContext);
            LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0);
            loutCheckBox.setLayoutParams(checkboxParams);
            loutCheckBox.setGravity(Gravity.CENTER_VERTICAL);
            loutCheckBox.setOrientation(LinearLayout.VERTICAL);

            // box bottom line
            LinearLayout loutBottomLine = new LinearLayout(mContext);
            LinearLayout.LayoutParams bottomLineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            bottomLineParams.setMargins(0, getPxToDp_LayoutParams(5), 0, getPxToDp_LayoutParams(5));
            loutBottomLine.setLayoutParams(bottomLineParams);
            loutBottomLine.setBackgroundColor(Color.parseColor("#cccccc"));

            // checkbox
            CheckBox checkAgree = new CheckBox(mContext);
            LinearLayout.LayoutParams checkboxParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            checkAgree.setLayoutParams(checkboxParams1);
            checkAgree.setText("test");
            checkAgree.setTextColor(Color.parseColor("#000000"));
            checkAgree.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

            // addview

            scrollBox.addView(txtBoxContent);

            loutBox.addView(txtBoxTitle);
            loutBox.addView(loutTopLine);
            loutBox.addView(scrollBox);
            loutBox.addView(loutBottomLine);
            loutBox.addView(checkAgree);

            loutMain.addView(loutBox);
            loutMain.addView(loutCheckBox);*/
        }


        // lout bottom btnok(weight : 0)
        LinearLayout loutBottom = new LinearLayout(mContext);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActionBarHeight(), 0);
        loutBottom.setLayoutParams(bottomParams);
        loutBottom.setOrientation(LinearLayout.VERTICAL);
        int pressFalse = Color.parseColor(mOkPressFalseColor);
        int pressTrue = Color.parseColor(mOkPressTrueColor);
        loutBottom.setBackground(setPressButtonColor(pressFalse, pressTrue));
        loutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setDismiss(mClauseDlg);
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        });

        // txt bottom btnok
        TextView txtBtnOk = new TextView(mContext);
        LinearLayout.LayoutParams txtOkParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        txtBtnOk.setLayoutParams(txtOkParams);
        txtBtnOk.setGravity(Gravity.CENTER);
        txtBtnOk.setText(mOk);
        txtBtnOk.setTextColor(Color.parseColor(mOkColor));
        txtBtnOk.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mOkSize);

        // addView
        loutBottom.addView(txtBtnOk);

        loutClause.addView(loutMain);
        loutClause.addView(loutBottom);

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        //builder.setCancelable(false);

        if(mClauseDlg == null){
            mClauseDlg = builder.create();
            mClauseDlg.show();
        }

        loutClause.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }


    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int height = loutMain.getHeight();
            MnLog.d(TAG, "height : "+height);

                if(height >= mMaxHeight){
                    LinearLayout.LayoutParams tempParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMaxHeight);
                    loutMain.setLayoutParams(tempParams);
                    loutMain.setOrientation(LinearLayout.VERTICAL);
                }

            removeOnGlobalLayoutListener(loutClause.getViewTreeObserver(), mGlobalLayoutListener);
        }
    };


    private int getPxToDp_LayoutParams(int px){
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mContext.getResources().getDisplayMetrics());
        return dp;
    }


    private int getActionBarHeight(){
        int actionBarHeight = 0;

        TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mActivity.getResources().getDisplayMetrics());
        }
        else {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mActivity.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }


    private StateListDrawable setPressButtonColor(int press_false, int press_true){
        StateListDrawable states= new StateListDrawable();
        states.addState(new int[] {-android.R.attr.state_pressed}, new ColorDrawable(press_false));
        states.addState(new int[] {android.R.attr.state_pressed}, new ColorDrawable(press_true));

        return states;
    }


    private void setDismiss(Dialog dialog){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            mClauseDlg = null;
        }
    }


    private String setTestText(){
        return "content\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\ncontent\n";
    }


    private void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener){
        if(observer == null){
            return;
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            observer.removeGlobalOnLayoutListener(listener);
        }
        else{
            observer.removeOnGlobalLayoutListener(listener);
        }
    }
}
