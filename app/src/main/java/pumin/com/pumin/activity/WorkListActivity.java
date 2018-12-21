package pumin.com.pumin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gizwits.gizwifisdk.api.GizDeviceScheduler;
import com.gizwits.gizwifisdk.api.GizDeviceSchedulerCenter;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSchedulerCenterListener;

import java.util.ArrayList;
import java.util.List;

import pumin.com.pumin.R;
import pumin.com.pumin.adapter.WorkRecycler_Adapter;
import pumin.com.pumin.untils.MyAnimation;
import pumin.com.pumin.untils.SpUtils;

public class WorkListActivity extends Activity implements View.OnClickListener, WorkRecycler_Adapter.OnItemclickLisenter {

    private ImageView mCLoseIMG;
    private ImageView mZJWOrkIMG;
    private LinearLayout mMenuLinear;
    private LinearLayout mYiciWOrk;
    private LinearLayout mZhouWork;
    private GizWifiDevice mDevice;
    private String TAG = "WorkListActivity";
    public static List<GizDeviceScheduler> mWorkList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private String off_on;
    private FrameLayout mDonghua;
    private int time = 30;
    private MyCountDownTimer2 myCountDownTimer2;
    private LinearLayout mImageView;
    private ImageView mXiaofangziIMG;
    private ImageView mConsult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worklist);
        initView();
        initData();
        initListener();
        rotateAnim(mImageView);
        MyAnimation animation = new MyAnimation();
        animation.setDuration(2000);
        animation.setRepeatCount(1000000);//设置重复次数
        mXiaofangziIMG.startAnimation(animation);
        mDonghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDonghua.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
        myCountDownTimer2.start();

        mDonghua.setVisibility(View.GONE);

    }

    public void rotateAnim(View mImageView) {
        Animation anim = new RotateAnimation(90f, 450f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(2000); // 设置动画时
        anim.setRepeatCount(1000000);
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        mImageView.startAnimation(anim);
    }

    private void initListener() {
        mCLoseIMG.setOnClickListener(this);
        mZJWOrkIMG.setOnClickListener(this);
        mYiciWOrk.setOnClickListener(this);
        mZhouWork.setOnClickListener(this);
        myCountDownTimer2 = new MyCountDownTimer2(1000 * time, 1000);
        mDonghua.setOnClickListener(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        myCountDownTimer2.start();
        Log.d("############################ WorkListActivity", "mFacilityFrame");
        return super.dispatchTouchEvent(ev);
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
            Log.d("############################ WorkListActivity", "计时结束");
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        String uid = SpUtils.getString(this, "Uid");
        String Token = SpUtils.getString(this, "Token");

// 同步更新设备的定时任务列表，mDevice为在设备列表中得到的设备对象
        GizDeviceSchedulerCenter.updateSchedulers(uid, Token, mDevice);
// 实现回调
        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
            @Override
            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    // 定时任务列表获取成功
                    Log.d(TAG, "schedulerList: " + schedulerList);
                    mWorkList = schedulerList;
                    if (mWorkList != null) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);//发送消息 
                    }
                } else {
                    // 获取失败
                }
            }
        };
        // 设置定时任务监听
        GizDeviceSchedulerCenter.setListener(mListener);
    }

    private void initView() {
        mCLoseIMG = (ImageView) findViewById(R.id.TimeClose_IMG);
        mZJWOrkIMG = (ImageView) findViewById(R.id.ZengjiaWork_IMG);
        mMenuLinear = (LinearLayout) findViewById(R.id.Menu_Linear);
        mYiciWOrk = (LinearLayout) findViewById(R.id.YiciWork_Linear);
        mZhouWork = (LinearLayout) findViewById(R.id.ZhouWork_linear);

        mImageView = (LinearLayout) findViewById(R.id.imageView);
        mXiaofangziIMG = (ImageView) findViewById(R.id.xiaofangzi_IMG);
        mDonghua = (FrameLayout) findViewById(R.id.Donghua_FrameLauout);

        mRecyclerView = (RecyclerView) findViewById(R.id.WorkList_RecyclerView);
        mConsult = (ImageView)findViewById(R.id.consult_IMG);
        mConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkListActivity.this,ExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.TimeClose_IMG:
                finish();
                break;
            case R.id.ZengjiaWork_IMG:
                if (mMenuLinear.getVisibility() == View.VISIBLE) {
                    mMenuLinear.setVisibility(View.GONE);
                } else {
                    mMenuLinear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.YiciWork_Linear://一次性任务
                intent = new Intent(this, SetWorkNewActivity.class);
                bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", mDevice);
                intent.putExtras(bundle);
                intent.putExtra("Work", "YiciWork");
                startActivity(intent);
                mMenuLinear.setVisibility(View.GONE);
                break;
            case R.id.ZhouWork_linear://周循环任务
                intent = new Intent(this, SetWorkNewActivity.class);
                bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", mDevice);
                intent.putExtras(bundle);
                intent.putExtra("Work", "ZhouWork");
                startActivity(intent);
                mMenuLinear.setVisibility(View.GONE);
                break;
        }
    }

    private WorkRecycler_Adapter mRecycleAdapter;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
//要做的事情 
            super.handleMessage(msg);
            int flag = msg.what;
            switch (flag) {
                case 1:
                    mRecycleAdapter = new WorkRecycler_Adapter(WorkListActivity.this, mWorkList);
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
                    mRecycleAdapter.setOnItemclickLisenter(WorkListActivity.this);

                    break;
            }
        }
    };

    @Override
    public void onItemClick(View view, int position) {
        mMenuLinear.setVisibility(View.GONE);

        String date = mWorkList.get(position).getDate();

//        Toast.makeText(this, "点击了" + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SetWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("GizWifiDevice", mDevice);
        intent.putExtras(bundle);
        intent.putExtra("position", position + "");
        if (date.length() != 0) {
            intent.putExtra("Work", "YiciWork");
        } else {
            intent.putExtra("Work", "ZhouWork");

        }
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
//        Toast.makeText(this, "长点击了" + position, Toast.LENGTH_SHORT).show();
        dialog(position);
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除此任务吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String uid = SpUtils.getString(WorkListActivity.this, "Uid");
                String Token = SpUtils.getString(WorkListActivity.this, "Token");
                mWorkList.get(position).getSchedulerID();
// 删除设备的定时任务列表，mDevice为在设备列表中得到的设备对象，your_scheduler_id是要删除的定时任务ID
                GizDeviceSchedulerCenter.deleteScheduler(uid, Token, mDevice, mWorkList.get(position).getSchedulerID());
// 实现回调
                GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
                    @Override
                    public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                            // 定时任务删除成功
//                    Toast.makeText(WorkListActivity.this, "定时任务删除成功", Toast.LENGTH_SHORT).show();
                            mRecycleAdapter.notifyDataSetChanged();
                        } else {
                            // 删除失败
                        }
                    }
                };
                // 设置定时任务监听
                GizDeviceSchedulerCenter.setListener(mListener);
//                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
