package pumin.com.pumin.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class SetWorkActivity extends AppCompatActivity implements View.OnClickListener {

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
    private String s = "";
    private String TAG = "SetWorkActivity";
    private TextView mtime;
    private GizDeviceScheduler gizDeviceScheduler;
    private String offonstr;
    private String replace;
    private GizWifiDevice mDevice;
    private String position;
    private ImageView mConsult;
    private TextView mRiqiTX;

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
        position = intent.getStringExtra("position");
        int i = Integer.parseInt(position);
        WorkListActivity workListActivity = new WorkListActivity();
        List<GizDeviceScheduler> mWorkList = workListActivity.mWorkList;
        gizDeviceScheduler = mWorkList.get(i);
        ConcurrentHashMap<String, Object> attrs = gizDeviceScheduler.getAttrs();
        Object pm_switch = attrs.get("pm_switch");
        if (pm_switch.toString().equals("true")) {
            offonstr = "开";
        } else if (pm_switch.toString().equals("false")) {
            offonstr = "关";

        }

        if (work.equals("YiciWork")) {
            mTitleTX.setText("一次性任务");
            mDate_TX.setText(gizDeviceScheduler.getDate());
//            mTime_TX.setText("");
            mOff_On_TX.setText(offonstr);
            mRemark_TX.setText(gizDeviceScheduler.getRemark());
            mtime.setText("时间");
            mRiqiTX.setText("日期");
        } else {
            mRiqiTX.setText("时间");
            String Weekstring = gizDeviceScheduler.getWeekdays().toString().substring(1, gizDeviceScheduler.getWeekdays().toString().length() - 1);
            if (true) {
                replace = Weekstring.replace("GizScheduleMonday", "星期一");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleTuesday", "星期二");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleWednesday", "星期三");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleThursday", "星期四");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleFriday", "星期五");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleSaturday", "星期六");
                Weekstring = replace;
                replace = Weekstring.replace("GizScheduleSunday", "星期日");
                Weekstring = replace;

            }
            mTitleTX.setText("周循环任务");
            mDateFrame.setVisibility(View.VISIBLE);
            mXianView.setVisibility(View.VISIBLE);
            mtime.setText("星期");
//            mDate_TX.setText("");
            WeekTime_TX1.setText(Weekstring);
            String[] split = Weekstring.split(", ");
            Log.d(TAG, "==============" + Weekstring);
            for (int j = 0; j < split.length; j++) {
                if (split[j].equals("星期一")) {
                    areaState[0] = true;
                } else if (split[j].equals("星期二")) {
                    areaState[1] = true;
                } else if (split[j].equals("星期三")) {
                    areaState[2] = true;
                } else if (split[j].equals("星期四")) {
                    areaState[3] = true;
                } else if (split[j].equals("星期五")) {
                    areaState[4] = true;
                } else if (split[j].equals("星期六")) {
                    areaState[5] = true;
                } else if (split[j].equals("星期日")) {
                    areaState[6] = true;
                }
            }
            mOff_On_TX.setText(offonstr);
            mRemark_TX.setText(gizDeviceScheduler.getRemark());
            mTimeFrame.setOnClickListener(new CheckBoxClickListener());
        }
        initDatePicker();
//        getSchedulerList();
    }

    private void initView() {
        mTimeClose = (ImageView) findViewById(R.id.TimeClose_IMG);
        mTitleTX = (TextView) findViewById(R.id.title_TX);
        mBaocunWork = (TextView) findViewById(R.id.baocunWork_IMG);
        mDateFrame = (FrameLayout) findViewById(R.id.Date_FrameLaout);
        mRiqiTX = (TextView)findViewById(R.id.Riqi_TX);

        mTimeFrame = (FrameLayout) findViewById(R.id.Time_FrameLayout);
        mOff_OnFrame = (FrameLayout) findViewById(R.id.Off_On_FrameLayout);
        mRemarkFrame = (FrameLayout) findViewById(R.id.Remark_FrameLayout);

        mDate_TX = (TextView) findViewById(R.id.Date_TX);//第一个时间
        mTime_TX = (TextView) findViewById(R.id.Time_TX);//第二个时间 星期
        WeekTime_TX1 = (TextView) findViewById(R.id.WeekTime_TX1);
        mtime = (TextView) findViewById(R.id.time);
        mOff_On_TX = (TextView) findViewById(R.id.Off_On_TX);
        mRemark_TX = (TextView) findViewById(R.id.Remark_TX);
        mXianView = (LinearLayout) findViewById(R.id.Xian_view);

        mConsult = (ImageView) findViewById(R.id.consult_IMG);
        mConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetWorkActivity.this, ExplainActivity.class);
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
                        List<GizDeviceScheduler> mWorkList = WorkListActivity.mWorkList;
                        GizDeviceScheduler scheduler = mWorkList.get(Integer.parseInt(position));
//                        GizDeviceScheduler scheduler = new GizDeviceScheduler();
                        scheduler.setDate(mTime_TX.getText().toString().split(" ")[0]);
                        scheduler.setTime(mTime_TX.getText().toString().split(" ")[1]);
                        scheduler.setRemark(mRemark_TX.getText().toString());
                        ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();
                        attrs.put("pm_switch", OffOn);
                        scheduler.setAttrs(attrs);

// 创建设备的定时任务，mDevice为在设备列表中得到的设备对象
                        GizDeviceSchedulerCenter.editScheduler(uid, Token, gizWifiDevice, scheduler);
                        Log.d(TAG, "------------scheduler = " + scheduler.getSchedulerID());
                        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
                            @Override
                            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                                    // 定时任务创建成功
                                    Toast.makeText(SetWorkActivity.this, "定时任务修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // 创建失败
                                    Toast.makeText(SetWorkActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "------------result = " + result);
                                }
                            }
                        };
                        // 设置定时任务监听
                        GizDeviceSchedulerCenter.setListener(mListener);
                    }
                } else {
                    Log.d(TAG, "------------s = " + s);
                    if (mDate_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mOff_On_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择开关类型", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (mRemark_TX.getText().toString().length() == 0) {
                        Toast.makeText(this, "请输入设备备注信息", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (WeekTime_TX1.getText().toString().length() == 0) {
                        Toast.makeText(this, "请选择星期", Toast.LENGTH_SHORT).show();
                        break;
                    }
//                    else  if (s.length()==0){
//                        Toast.makeText(this, "未选择星期", Toast.LENGTH_SHORT).show();
//                    }
                    else {
// 每周一到周五重复执行的定时任务
//                        GizDeviceScheduler scheduler = new GizDeviceScheduler();
//                    scheduler.setDate("2017-01-16");
                        List<GizDeviceScheduler> mWorkList = WorkListActivity.mWorkList;
                        GizDeviceScheduler scheduler = mWorkList.get(Integer.parseInt(position));
                        scheduler.setTime(mDate_TX.getText().toString().split(" ")[0]);
                        scheduler.setRemark(mRemark_TX.getText().toString());
                        ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();
                        attrs.put("pm_switch", OffOn);
                        scheduler.setAttrs(attrs);
                        List<GizScheduleWeekday> weekDays = new ArrayList<GizScheduleWeekday>();
                        String[] split = s.split(" ");
                        if (split.length == 0) {
                            Toast.makeText(this, "未选择星期", Toast.LENGTH_SHORT).show();
                            break;
                        }
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
                        GizDeviceSchedulerCenter.editScheduler(uid, Token, gizWifiDevice, scheduler);
// 实现回调
                        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
                            @Override
                            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                                    // 定时任务创建成功
                                    Toast.makeText(SetWorkActivity.this, "定时任务修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // 创建失败
                                    Toast.makeText(SetWorkActivity.this, "修改失败" + result, Toast.LENGTH_SHORT).show();
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
                final Dialog mDialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
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
                final Dialog mDialog1 = new AlertDialog.Builder(this).setView(new EditText(this)).create();
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
                mRemark.setText(gizDeviceScheduler.getRemark());
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
        mDate_TX.setText(gizDeviceScheduler.getTime());
        if (work.equals("YiciWork")) {
            mTime_TX.setText(gizDeviceScheduler.getDate() + " " + gizDeviceScheduler.getTime());
            WeekTime_TX1.setVisibility(View.GONE);
        } else {
            mTime_TX.setText(now);
            mTime_TX.setVisibility(View.GONE);
            WeekTime_TX1.setVisibility(View.VISIBLE);
        }

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (work.equals("YiciWork")) {
                    mDate_TX.setText(time.split(" ")[0]);
                } else {
                    mDate_TX.setText(time.split(" ")[1]);
                }
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
//            new AlertDialog.Builder(SetWorkNewActivity.this).setTitle("选择时间").setItems(areas, new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Toast.makeText(SetWorkNewActivity.this, "您已经选择了: " + which + ":" + areas[which], Toast.LENGTH_LONG).show();
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
//            gizDeviceScheduler.getWeekdays();
//            areaCheckListView = (ListView) gizDeviceScheduler.getWeekdays();
//            areaCheckListView.setItemChecked(2,true);
            AlertDialog ad = new AlertDialog.Builder(SetWorkActivity.this)

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

    public void getSchedulerList() {
        Intent intent = getIntent();
        gizWifiDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        String uid = SpUtils.getString(this, "Uid");
        String Token = SpUtils.getString(this, "Token");

// 同步更新设备的定时任务列表，mDevice为在设备列表中得到的设备对象
        GizDeviceSchedulerCenter.updateSchedulers(uid, Token, gizWifiDevice);
// 实现回调
        GizDeviceSchedulerCenterListener mListener = new GizDeviceSchedulerCenterListener() {
            public List<GizDeviceScheduler> mWorkList;

            @Override
            public void didUpdateSchedulers(GizWifiErrorCode result, GizWifiDevice schedulerOwner, List<GizDeviceScheduler> schedulerList) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    // 定时任务列表获取成功
                    Log.d(TAG, "schedulerList: " + schedulerList);
                    mWorkList = schedulerList;
                    if (mWorkList != null) {
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);//发送消息 
                    }
                } else {
                    // 获取失败
                }
            }
        };
        // 设置定时任务监听
        GizDeviceSchedulerCenter.setListener(mListener);
    }
}
