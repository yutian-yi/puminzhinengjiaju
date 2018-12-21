package pumin.com.pumin.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceScheduler;
import com.gizwits.gizwifisdk.api.GizDeviceSchedulerCenter;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizScheduleWeekday;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSchedulerCenterListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import pumin.com.pumin.R;
import pumin.com.pumin.untils.SpUtils;
import pumin.com.pumin.view.CustomDatePicker;

public class SetWorkNewActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mDateFrame;
    private ImageView mTimeClose;
    private TextView mBaocunWork;
    private FrameLayout mTimeFrame, mOff_OnFrame, mRemarkFrame;
    private TextView mDate_TX, mTime_TX, WeekTime_TX1, mOff_On_TX, mRemark_TX;
    private TextView mTitleTX;
    private CustomDatePicker customDatePicker1;
    private CustomDatePicker customDatePicker2;
    private TextView mOnTX;
    private TextView OffTX;
    private boolean OffOn;
    private TextView mRemark;
    private TextView mYesTX;
    private TextView mNoTX;
    private CharSequence Remark;
    private GizWifiDevice gizWifiDevice;
    private LinearLayout mXianView;
    private String work;
    private String s;
    private String TAG = "SetWorkActivity";
    private TextView mtime;
    private ImageView mConsult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_work);
        initView();
        initData();
        initListener();
        Intent intent = getIntent();
        work = intent.getStringExtra("Work");
        gizWifiDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");

        if (work.equals("YiciWork")) {
            mTitleTX.setText("一次性任务");
            mDate_TX.setText("");
            mTime_TX.setText("");
            mOff_On_TX.setText("");
            mRemark_TX.setText("");
            mtime.setText("时间");
        } else {
            mTitleTX.setText("周循环任务");
            mDateFrame.setVisibility(View.VISIBLE);
            mXianView.setVisibility(View.VISIBLE);
            mtime.setText("星期");
            mDate_TX.setText("");
            mTime_TX.setText("");
            mOff_On_TX.setText("");
            mRemark_TX.setText("");
            mTimeFrame.setOnClickListener(new CheckBoxClickListener());
        }
        initDatePicker();
    }

    private void initView() {
        mTimeClose = (ImageView) findViewById(R.id.TimeClose_IMG);
        mTitleTX = (TextView) findViewById(R.id.title_TX);
        mBaocunWork = (TextView) findViewById(R.id.baocunWork_IMG);
        mDateFrame = (FrameLayout) findViewById(R.id.Date_FrameLaout);
        mTimeFrame = (FrameLayout) findViewById(R.id.Time_FrameLayout);
        mOff_OnFrame = (FrameLayout) findViewById(R.id.Off_On_FrameLayout);
        mRemarkFrame = (FrameLayout) findViewById(R.id.Remark_FrameLayout);

        mDate_TX = (TextView) findViewById(R.id.Date_TX);
        mTime_TX = (TextView) findViewById(R.id.Time_TX);
        WeekTime_TX1 = (TextView) findViewById(R.id.WeekTime_TX1);
        mtime = (TextView)findViewById(R.id.time);
        mOff_On_TX = (TextView) findViewById(R.id.Off_On_TX);
        mRemark_TX = (TextView) findViewById(R.id.Remark_TX);
        mXianView = (LinearLayout) findViewById(R.id.Xian_view);

        mConsult = (ImageView)findViewById(R.id.consult_IMG);
        mConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetWorkNewActivity.this,ExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        mTimeClose.setOnClickListener(this);
        mBaocunWork.setOnClickListener(this);

        mDateFrame.setOnClickListener(this);
        mTimeFrame.setOnClickListener(this);
        mOff_OnFrame.setOnClickListener(this);
        mRemarkFrame.setOnClickListener(this);

    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        String uid = SpUtils.getString(this, "Uid");
        String Token = SpUtils.getString(this, "Token");
        switch (v.getId()) {
            case R.id.TimeClose_IMG:
                finish();
                break;
            case R.id.baocunWork_IMG:
                if (work.equals("YiciWork")) {
                    // 一次性定时任务，在2017年1月16日早上6点30分开灯
                    if (mTime_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mOff_On_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择开关类型", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mRemark_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请输入设备备注信息", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        GizDeviceScheduler scheduler = new GizDeviceScheduler();
                        scheduler.setDate(mTime_TX.getText().toString().split(" ")[0]);
                        scheduler.setTime(mTime_TX.getText().toString().split(" ")[1]);
                        scheduler.setRemark(mRemark_TX.getText().toString());
                        ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();
                        attrs.put("pm_switch", OffOn);
                        scheduler.setAttrs(attrs);

// 创建设备的定时任务，mDevice为在设备列表中得到的设备对象
                        GizDeviceSchedulerCenter.createScheduler(uid, Token, gizWifiDevice, scheduler);
                        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
                            @Override
                            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                                    // 定时任务创建成功
                                    Toast.makeText(SetWorkNewActivity.this, "定时任务创建成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // 创建失败
                                    Toast.makeText(SetWorkNewActivity.this, "创建失败", Toast.LENGTH_SHORT).show();

                                }
                            }
                        };
                        // 设置定时任务监听
                        GizDeviceSchedulerCenter.setListener(mListener);
                    }
                } else {
                    if (mDate_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mOff_On_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择开关类型", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mRemark_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请输入设备备注信息", Toast.LENGTH_SHORT).show();
                        break;
                    }else if (WeekTime_TX1.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择星期", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
// 每周一到周五重复执行的定时任务
                        GizDeviceScheduler scheduler = new GizDeviceScheduler();// 时间设置有问题，设置上的 时间始终是当前时间
//                    scheduler.setDate("2017-01-16");
                        scheduler.setTime(mTime_TX.getText().toString().split(" ")[1]);
                        scheduler.setRemark(mRemark_TX.getText().toString());
                        ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();
                        attrs.put("pm_switch", OffOn);
                        scheduler.setAttrs(attrs);
                        List<GizScheduleWeekday> weekDays = new ArrayList<GizScheduleWeekday>();
                        String[] split = s.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].equals("星期一")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleMonday);
                            } else if (split[i].equals("星期二")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleTuesday);
                            } else if (split[i].equals("星期三")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleWednesday);
                            } else if (split[i].equals("星期四")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleThursday);
                            } else if (split[i].equals("星期五")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleFriday);
                            } else if (split[i].equals("星期六")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleSaturday);
                            } else if (split[i].equals("星期日")) {
                                weekDays.add(GizScheduleWeekday.GizScheduleSunday);
                            }
                        }
//                    weekDays.add(GizScheduleWeekday.GizScheduleMonday);
//                    weekDays.add(GizScheduleWeekday.GizScheduleTuesday);
//                    weekDays.add(GizScheduleWeekday.GizScheduleWednesday);
//                    weekDays.add(GizScheduleWeekday.GizScheduleThursday);
//                    weekDays.add(GizScheduleWeekday.GizScheduleFriday);
//                    scheduler.setMonthDays(weekDays);
                        scheduler.setWeekdays(weekDays);
// 创建设备的定时任务，mDevice为在设备列表中得到的设备对象
                        GizDeviceSchedulerCenter.createScheduler(uid, Token, gizWifiDevice, scheduler);
// 实现回调
                        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
                            @Override
                            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                                    // 定时任务创建成功
                                    Toast.makeText(SetWorkNewActivity.this, "定时任务创建成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // 创建失败
                                    Toast.makeText(SetWorkNewActivity.this, "创建失败" + result, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "result: " + result);
                                }
                            }
                        };
                        // 设置定时任务监听
                        GizDeviceSchedulerCenter.setListener(mListener);
                    }
                }
                break;
            case R.id.Date_FrameLaout://月份
                customDatePicker1.show(mTime_TX.getText().toString());
                break;
            case R.id.Time_FrameLayout://时间
                Intent intent = getIntent();
                String work = intent.getStringExtra("Work");
                if (work.equals("YiciWork")) {
                    customDatePicker2.show(mTime_TX.getText().toString());
                } else {
                }
                break;
            case R.id.Off_On_FrameLayout://任务类型
                final Dialog mDialog = new android.app.AlertDialog.Builder(this).setView(new EditText(this)).create();
                mDialog.setCancelable(true);
                mDialog.show();
                Window window = mDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setContentView(R.layout.alert_off_on1);
                mOnTX = (TextView) window.findViewById(R.id.OnTX);
                OffTX = (TextView) window.findViewById(R.id.OffTX);

                WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = window.getAttributes();
                // 获取屏幕宽、高用
                WindowManager wm = (WindowManager) this
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                lp.width = (int) (display.getWidth() * 1); // 宽度设置为屏幕的0.65
                window.setAttributes(lp);
                mOnTX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OffOn = true;
                        mOff_On_TX.setText("开");
                        mDialog.dismiss();
                    }
                });
                OffTX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOff_On_TX.setText("关");
                        OffOn = false;
                        mDialog.dismiss();
                    }
                });
                break;
            case R.id.Remark_FrameLayout://任务信息
                final Dialog mDialog1 = new android.app.AlertDialog.Builder(this).setView(new EditText(this)).create();
                mDialog1.setCancelable(true);
                mDialog1.show();
                Window window1 = mDialog1.getWindow();
                window1.setGravity(Gravity.BOTTOM);
                window1.setContentView(R.layout.alert_settext);

                WindowManager manager1 = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm1 = new DisplayMetrics();
                manager1.getDefaultDisplay().getMetrics(dm1);
                WindowManager.LayoutParams lp1 = window1.getAttributes();
                // 获取屏幕宽、高用
                WindowManager wm1 = (WindowManager) this
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display1 = wm1.getDefaultDisplay();
                lp1.width = (int) (display1.getWidth() * 1); // 宽度设置为屏幕的0.65
                window1.setAttributes(lp1);
                mRemark = (TextView) window1.findViewById(R.id.Remark_TX);
                mYesTX = (TextView) window1.findViewById(R.id.yesTX);
                mNoTX = (TextView) window1.findViewById(R.id.noTX);
                mYesTX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Remark = mRemark.getText();
                        mRemark_TX.setText(Remark.toString());
                        mDialog1.dismiss();
                    }
                });
                mNoTX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog1.dismiss();
                    }
                });
                break;

        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        mDate_TX.setText(now.split(" ")[1]);
        if (work.equals("YiciWork")) {
            mTime_TX.setText(now);
            WeekTime_TX1.setVisibility(View.GONE);
        } else {
            mTime_TX.setText(now);
            mTime_TX.setVisibility(View.GONE);
            WeekTime_TX1.setVisibility(View.VISIBLE);
        }

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                mDate_TX.setText(time.split(" ")[1]);
            }
        }, "2010-01-01 00:00", "2030-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime1(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                mTime_TX.setText(time);
            }
        }, "2010-01-01 00:00", "2030-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分

        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    private String[] areas = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private boolean[] areaState = new boolean[]{false, false, false, false, false, false, false};

    private ListView areaCheckListView;


//    class AlertClickListener implements View.OnClickListener {
//
//        @Override
//
//        public void onClick(View v) {
//
//            new AlertDialog.Builder(SetWorkActivity.this).setTitle("选择时间").setItems(areas, new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Toast.makeText(SetWorkActivity.this, "您已经选择了: " + which + ":" + areas[which], Toast.LENGTH_LONG).show();
//
//                    dialog.dismiss();
//
//                }
//
//            }).show();
//
//        }
//
//    }
//

    class CheckBoxClickListener implements View.OnClickListener {

        @Override

        public void onClick(View v) {

            AlertDialog ad = new AlertDialog.Builder(SetWorkNewActivity.this)

                    .setTitle("选择区域")

                    .setMultiChoiceItems(areas, areaState, new DialogInterface.OnMultiChoiceClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {

                            //点击某个区域

                        }

                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            s = "";

                            for (int i = 0; i < areas.length; i++) {

                                if (areaCheckListView.getCheckedItemPositions().get(i)) {

                                    s += areaCheckListView.getAdapter().getItem(i) + " ";

                                } else {

                                    areaCheckListView.getCheckedItemPositions().get(i, false);

                                }

                            }

                            if (areaCheckListView.getCheckedItemPositions().size() > 0) {

//                                Toast.makeText(SetWorkActivity.this, s, Toast.LENGTH_LONG).show();
                                WeekTime_TX1.setText(s);
                                mTime_TX.setVisibility(View.GONE);
                                WeekTime_TX1.setVisibility(View.VISIBLE);

                            } else {

                                //没有选择

                            }

                            dialog.dismiss();

                        }

                    }).setNegativeButton("取消", null).create();

            areaCheckListView = ad.getListView();

            ad.show();

        }

    }
}
