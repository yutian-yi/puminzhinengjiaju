package pumin.com.pumin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;

import java.util.ArrayList;
import java.util.List;

import pumin.com.pumin.R;
import pumin.com.pumin.adapter.FacilityAdapter;
import pumin.com.pumin.context.UIConfig;
import pumin.com.pumin.untils.MyAnimation;
import pumin.com.pumin.untils.SpUtils;

/*
* 设备管理展示页面
* */
public class FacilityActivity extends Activity implements View.OnClickListener,FacilityAdapter.OnItemclickLisenter{
    public List<String> LouList = new ArrayList<>();
    private FacilityAdapter mRecycleAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mSetUpIMG;
    private Menu menu;
    private LinearLayout mMenuLinear;
    private LinearLayout mTianjia,mSetPassword,mCloseLogin;
    private LinearLayout mFacility;
    private LinearLayout mImageView;
    private ImageView mXiaofangziIMG;
    private FrameLayout mDonghua;
    private MyCountDownTimer myCountDownTimer;
    private ProgressDialog progressDialog;
    private FrameLayout mFacilityFrame;
    private MyCountDownTimer2 myCountDownTimer2;
    private int time = 30;
    private FrameLayout mSetLinear;
    public static int height;
    private long            mExitTime = 2000;
    private ImageView mConsultIMG;
    private String TAG = "FacilityActivity";
    private List<GizWifiDevice> devices = new ArrayList<GizWifiDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        GizWifiSDK.sharedInstance().startWithAppID(this, UIConfig.app_id);

        initView();
        initData();
        initListener();
        GizWifiSDK.sharedInstance().userLogin(SpUtils.getString(this, "etName"), SpUtils.getString(this, "etPsws"));
// 实现回调
        GizWifiSDKListener mListener1 = new GizWifiSDKListener() {
            public Intent intent;

            @Override
            public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 登录成功
                    progressDialog.dismiss();
//                    Log.d(TAG, "token = " + token + "------uid = " + uid + "------------1 = " + result.toString());
//                            spf.edit().putString("Uid", uid).commit();
//                            spf.edit().putString("Token", token).commit();
                    SpUtils.putString(FacilityActivity.this, "Token", token);
                    SpUtils.putString(FacilityActivity.this, "Uid", uid);
//                    intent = new Intent(FacilityActivity.this, FacilityActivity.class);
//                    startActivity(intent);
                    mRecycleAdapter.notifyDataSetChanged();
                } else {
// 登录失败
                    if (result == GizWifiErrorCode.GIZ_SDK_CONNECTION_ERROR) {
                        ToastTools.short_Toast(FacilityActivity.this, "发生了连接错误");
                        progressDialog.dismiss();
                    }else if (result == GizWifiErrorCode.GIZ_SDK_REQUEST_TIMEOUT){
                        ToastTools.short_Toast(FacilityActivity.this, "SDK接口执行超时");
                        progressDialog.dismiss();
                    }
                }
            }
        };
        GizWifiSDK.sharedInstance().setListener(mListener1);


        mRecycleAdapter = new FacilityAdapter(FacilityActivity.this, LouList);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        //设置布局管理器、
        mRecyclerView.setLayoutManager(layoutManager);

        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter、
        mRecyclerView.setAdapter(mRecycleAdapter);
        //设置增加或删除条、目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                     /*设置itemView监听器*/
        mRecycleAdapter.setOnItemclickLisenter(FacilityActivity.this);



        rotateAnim(mImageView);
//        rotateAnim(mXiaofangziIMG);
        MyAnimation animation = new MyAnimation();
        animation.setDuration(2000);
        animation.setRepeatCount(1000000);
        mXiaofangziIMG.startAnimation(animation);

//        if (devices.size()!=0){}
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取数据");
        progressDialog.show();
        myCountDownTimer = new MyCountDownTimer(3000, 1000);
        myCountDownTimer.start();
        mFacilityFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mDonghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDonghua.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("############################", "mFacilityFrame");
        myCountDownTimer2.start();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GizWifiSDKListener mListener = new GizWifiSDKListener() {
            @Override
            public  void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
                // 提示错误原因
                if(result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    Log.d(TAG, "result: " + result.name());
                }
                // 显示变化后的设备列表
                Log.d(TAG, "discovered deviceList: " + deviceList);
//                devices = deviceList;
                mRecycleAdapter.notifyDataSetChanged();

            }
        };
        GizWifiSDK.sharedInstance().setListener(mListener);
        mRecycleAdapter.notifyDataSetChanged();
        myCountDownTimer2.start();
        mDonghua.setVisibility(View.GONE);

    }
    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
//            btnGetCode.setClickable(false);
//            btnGetCode.setText(l / 1000 + "s");
//            btnGetCode.setBackgroundColor(Color.parseColor("#aaaaaa"));
            devices = GizWifiSDK.sharedInstance().getDeviceList();

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
//            btnGetCode.setText("重新发送");
//            //设置可点击
//            btnGetCode.setClickable(true);
//            btnGetCode.setBackgroundColor(Color.parseColor("#93CDDD"));
            devices = GizWifiSDK.sharedInstance().getDeviceList();
            if (devices.size()!=0){
                progressDialog.dismiss();
            }
            mRecycleAdapter.notifyDataSetChanged();
//            progressDialog.dismiss();

//            GizWifiSDKListener mListener = new GizWifiSDKListener() {
//                @Override
//                public  void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
//                    if (deviceList.size()!=0){
//                        progressDialog.dismiss();
//                        mRecycleAdapter.notifyDataSetChanged();
//                    }
//                    // 提示错误原因
//                    if(result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//                        Log.d("FacilityAdapter", "result: " + result.name());
//                    }
//                    // 显示变化后的设备列表
//                    Log.d("FacilityAdapter", "discovered deviceList: " + deviceList);
////                devices = deviceList;
//                    mRecycleAdapter.notifyDataSetChanged();
//
//                }
//            };
//            GizWifiSDK.sharedInstance().setListener(mListener);

        }
    }
    //复写倒计时
    private class MyCountDownTimer2 extends CountDownTimer {

        private LinearLayout mDonghuaLinear;

        public MyCountDownTimer2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            mDonghua.setVisibility(View.VISIBLE);
            Log.d("############################", "计时结束");
        }
    }
    private void initListener() {
        mSetUpIMG.setOnClickListener(this);
        mTianjia.setOnClickListener(this);
        mSetPassword.setOnClickListener(this);
        mCloseLogin.setOnClickListener(this);
        mFacility.setOnClickListener(this);
        mRecyclerView.setOnClickListener(this);

        myCountDownTimer2 = new MyCountDownTimer2(1000*time, 1000);

    }

    private void initData() {
//        for (int i = 1; i < 6; i++) {
//            if (i==5){
//                LouList.add("门卫");
//            }else {
//                LouList.add(i + " F ");
//            }
//        }
        LouList.add("实验");
        LouList.add("四楼");
        LouList.add("三楼");
        LouList.add("二楼");
        LouList.add("一楼");
        LouList.add("门卫");

        height = mSetLinear.getHeight();
        // 使用缓存的设备列表刷新UI
//        GizWifiSDKListener mListener = new GizWifiSDKListener() {
//            @Override
//            public  void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
//                // 提示错误原因
//                if(result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//                    Log.d(TAG, "result: result: = 1" + result.name());
//                }
//                // 显示变化后的设备列表
//                Log.d(TAG, "discovered deviceList: result: = 2" + deviceList);
////                devices = deviceList;
//                if (devices!=deviceList){
//                    Log.d(TAG, "result: result: = 11" + result.name());
//                }
//                mRecycleAdapter.notifyDataSetChanged();
////                mRecyclerView.getItemAnimator().notify();
//            }
//        };
//        GizWifiSDK.sharedInstance().setListener(mListener);
//        devices = GizWifiSDK.sharedInstance().getDeviceList();

    }

    private void initView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.Facility_Recycler);

        mSetUpIMG = (ImageView)findViewById(R.id.SetUp_TMG);
        mMenuLinear = (LinearLayout)findViewById(R.id.Menu_Linear);

        mSetLinear = (FrameLayout)findViewById(R.id.Set_LinearLayout);

        mTianjia = (LinearLayout)findViewById(R.id.Tianjia_Linear);
        mSetPassword = (LinearLayout)findViewById(R.id.SetPassword_linear);
        mCloseLogin = (LinearLayout)findViewById(R.id.CloseLogin_Linear);
        mFacility = (LinearLayout)findViewById(R.id.Linear_facility);

        mImageView = (LinearLayout) findViewById(R.id.imageView);
        mXiaofangziIMG = (ImageView)findViewById(R.id.xiaofangzi_IMG);
        mDonghua = (FrameLayout)findViewById(R.id.Donghua_FrameLauout);
        mFacilityFrame = (FrameLayout)findViewById(R.id.activity_facility);
        mDonghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDonghua.setVisibility(View.GONE);
            }
        });

        mConsultIMG = (ImageView)findViewById(R.id.consult_IMG);
        mConsultIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacilityActivity.this,ExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.devicelist_login, menu);
        return true;
    }
    public void rotateAnim(View mImageView) {
        Animation anim =new RotateAnimation(90f, 450f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(2000); // 设置动画时
        anim.setRepeatCount(1000000);
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        mImageView.startAnimation(anim);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SetUp_TMG:
                if ( mMenuLinear.getVisibility()==View.GONE){
                    mMenuLinear.setVisibility(View.VISIBLE);
                }else {
                    mMenuLinear.setVisibility(View.GONE);
                }
                break;
            case R.id.Linear_facility:
                mMenuLinear.setVisibility(View.GONE);
                break;
            case R.id.Facility_Recycler:
                mMenuLinear.setVisibility(View.GONE);
                break;
            case R.id.Tianjia_Linear://添加设备
                Intent intent = new Intent(FacilityActivity.this,GosAirlinkChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
                mMenuLinear.setVisibility(View.GONE);
                break;
            case R.id.SetPassword_linear://修改密码
                Intent intent1 = new Intent(FacilityActivity.this, ForgetPasswordActivity.class);
                startActivity(intent1);
                mMenuLinear.setVisibility(View.GONE);
                break;
            case R.id.CloseLogin_Linear://退出登录
                SpUtils.putString(FacilityActivity.this, "Token", null);
                Intent intent2 = new Intent(FacilityActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
                mMenuLinear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this,"点击了第"+position+"个",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) { // 连按退出
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();
//            } else {
////                LogUtil.log.d("@@@@执行退出应用");
//
//                System.exit(0); //退出应用回收所有activity
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
