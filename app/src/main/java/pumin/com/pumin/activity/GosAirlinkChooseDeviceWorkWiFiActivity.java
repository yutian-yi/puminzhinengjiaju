package pumin.com.pumin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pumin.com.pumin.R;
import pumin.com.pumin.common.GosDeploy;
import pumin.com.pumin.untils.MyAnimation;
import pumin.com.pumin.untils.NetUtils;

@SuppressLint("InflateParams")
public class GosAirlinkChooseDeviceWorkWiFiActivity extends GosConfigModuleBaseActivity implements OnClickListener {

    private AlertDialog create;
    private ArrayList<ScanResult> wifiList;

    /**
     * wifi信息
     */
    public WifiInfo wifiInfo;

    /**
     * The et SSID
     */
    private EditText etSSID;

    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The btn Next
     */
    private Button btnNext;

    /**
     * The ll ChooseMode
     */
//    private LinearLayout llChooseMode;

    /**
     * The cb Laws
     */
    private CheckBox cbLaws;

    /**
     * The tv Mode
     */
//    private TextView tvMode;

    /**
     * The img WiFiList
     */
    private ImageView imgWiFiList;

    /**
     * 配置用参数
     */
    private String workSSID, workSSIDPsw;

    /**
     * The data
     */
    List<String> modeList;

    /** The Adapter */
//    ModeListAdapter modeListAdapter;

    /**
     * The modeNum
     */
    static int modeNum = 0;


    private static final int REQUEST_CODE_SETTING = 100;
    private ImageView mCloseIMG;
    private ImageView mXiaofengzi;
    private ImageView mImageYuanH;
    private TextView mLianjieTX;
    private ImageView mHomepage;
    private ImageView mConsult;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airlink_choose_device_workwifi);
        // 设置ActionBar
//        String title = getString(R.string.add_device) + GosDeploy.setAddDeviceTitle();
//        setActionBar(true, true, title);

        initData();
        initView();
        ininEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
//            tvMode.setText(modeList.get(modeNum));
            // 预设workSSID && workSSIDPsw
            workSSID = NetUtils.getCurentWifiSSID(this);
            String mypass = spf.getString("mypass", "");

            if (!TextUtils.isEmpty(workSSID)) {
                etSSID.setText(workSSID);
                if (!TextUtils.isEmpty(mypass)) {
                    JSONObject obj = new JSONObject(mypass);

                    if (obj.has(workSSID)) {
                        String pass = obj.getString(workSSID);
                        etPsw.setText(pass);
                    } else {
                        etPsw.setText("");
                    }
                }

            } else {
                etSSID.setText(NetUtils.getCurentWifiSSID(this));
            }

            // 当没有任何文字的时候设置为明文显示
            if (TextUtils.isEmpty(etPsw.getText().toString())) {
                cbLaws.setChecked(true);
                etPsw.setInputType(0x90);
            } else {
                etPsw.setInputType(0x81);
                cbLaws.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
//        tvMode = (TextView) findViewById(R.id.tvMode);
        etSSID = (EditText) findViewById(R.id.etSSID);
        etPsw = (EditText) findViewById(R.id.etPsw);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        btnNext = (Button) findViewById(R.id.btnNext);
//        llChooseMode = (LinearLayout) findViewById(R.id.llChooseMode);
        imgWiFiList = (ImageView) findViewById(R.id.imgWiFiList);
        mHomepage = (ImageView) findViewById(R.id.Homepage_IMG);
        mCloseIMG = (ImageView) findViewById(R.id.close_IMG);
        mCloseIMG.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 配置文件部署
//        btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
//        btnNext.setTextColor(GosDeploy.setButtonTextColor());
        // llChooseMode.setVisibility(GosDeploy.setModuleSelectOn());

//        llChooseMode.setVisibility(View.GONE);
        mConsult = (ImageView)findViewById(R.id.consult_IMG);
        mConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GosAirlinkChooseDeviceWorkWiFiActivity.this,ExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ininEvent() {
        btnNext.setOnClickListener(this);
//        imgWiFiList.setOnClickListener(this);
//        llChooseMode.setOnClickListener(this);
        mHomepage.setOnClickListener(this);
        cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });
    }

    private void initData() {
        // workSSID = spf.getString("workSSID", "");

        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:  //下一步
                workSSID = etSSID.getText().toString();
                workSSIDPsw = etPsw.getText().toString();

                if (TextUtils.isEmpty(workSSID)) {
                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(workSSIDPsw)) {
                    final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.alert_gos_empty);

                    LinearLayout llNo, llSure;
//                    llNo = (LinearLayout) window.findViewById(R.id.llNo);
                    llSure = (LinearLayout) window.findViewById(R.id.llSure);

//                    llNo.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                        }
//                    });

                    llSure.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (dialog.isShowing()) {
                                dialog.cancel();
                            }
                            toAirlinkReady();
                        }
                    });

                } else {
                    toAirlinkReady();
                }


                break;

            case R.id.imgWiFiList:

                AndPermission.with(this)
                        .requestCode(REQUEST_CODE_SETTING)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION).rationale(new RationaleListener() {

                    @Override
                    public void showRequestPermissionRationale(int arg0, Rationale arg1) {
                        AndPermission.rationaleDialog(GosAirlinkChooseDeviceWorkWiFiActivity.this, arg1).show();
                    }
                })
                        .send();


                break;

//            case R.id.llChooseMode:
//
////                Intent intent = new Intent(this, GosModeListActivity.class);
////                startActivity(intent);
//                break;
            case R.id.Homepage_IMG:
                finish();
                break;
            default:
                break;
        }
    }

    private void toAirlinkReady() {
        // 需要记录所有配置过的wifi和密码

        try {
            String mypass = spf.getString("mypass", "");
            if (TextUtils.isEmpty(mypass)) {
                JSONObject mUserAndPass = new JSONObject();

                mUserAndPass.put(workSSID, workSSIDPsw);
                spf.edit().putString("mypass", mUserAndPass.toString()).commit();
            } else {
                JSONObject obj = new JSONObject(mypass);

                obj.put(workSSID, workSSIDPsw);

                spf.edit().putString("mypass", obj.toString()).commit();

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        spf.edit().putString("workSSID", workSSID).commit();
        spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

        int setModuleSelectOn = GosDeploy.setModuleSelectOn();

        if (setModuleSelectOn == 0) {
//            Intent intent = new Intent(this, GosModeListActivity.class);
//            startActivity(intent);
            setFacility();
        } else {
//            Intent intent = new Intent(this, GosAirlinkReadyActivity.class);
//            startActivity(intent);
            setFacility();
        }

//        finish();
    }

    public void setFacility() {//配置设备连接
        // 让手机连上目标Wifi
// MCU发出开启AirLink串口指令，通知模组开启AirLink模式。
//        详情请参考《智能云空调-机智云接入串口通信协议文档》
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在配置设备");
        progressDialog.show();
//        final Dialog mDialog = new android.app.AlertDialog.Builder(this).setView(new EditText(this)).create();
//        mDialog.show();
//        mDialog.setCancelable(true);
//        Window window = mDialog.getWindow();
//        window.setContentView(R.layout.yl_jiazaidonghua);
//        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(dm);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        // 获取屏幕宽、高用
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        lp.width = (int) (display.getWidth() * 0.60); // 宽度设置为屏幕的0.65
//        window.setAttributes(lp);
        List<GizWifiGAgentType> types = new ArrayList<GizWifiGAgentType>();
//        mXiaofengzi = (ImageView) window.findViewById(R.id.xiaofangzi_IMG);
//        mImageYuanH = (ImageView) window.findViewById(R.id.imageView);
//        mLianjieTX = (TextView) window.findViewById(R.id.lianjiemiaoshu_TX);
//        rotateAnim(mImageYuanH);

//        MyAnimation animation = new MyAnimation();
//        animation.setRepeatCount(1000);
//        mXiaofengzi.startAnimation(animation);
        types.add(GizWifiGAgentType.GizGAgentESP);
        GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw, GizWifiConfigureMode.GizWifiAirLink, null, 60, types);
        GizWifiSDKListener mListener = new GizWifiSDKListener() {
            //等待配置完成或超时，回调配置完成接口
            @Override
            public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    // 配置成功
//                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
                    Log.i("-----------------------1", result.toString());
                    Intent intent2 = new Intent(GosAirlinkChooseDeviceWorkWiFiActivity.this, SetRemarkActivity.class);
                    intent2.putExtra("mdid", did);
                    progressDialog.dismiss();
                    startActivity(intent2);
                } else if (result == GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING) {
                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, "正在配置", Toast.LENGTH_SHORT).show();
                    Log.i("-----------------------2", result.toString());
//                    mLianjieTX.setText("已连接设备，正在配置");
                    // 正在配置
                } else {
                    // 配置失败
                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, "配置失败，请重试", Toast.LENGTH_SHORT).show();
                    Log.i("-----------------------3", result.toString());
                    progressDialog.dismiss();
                }
            }
        };
        //配置设备入网，发送要配置的wifi名称、密码
        GizWifiSDK.sharedInstance().setListener(mListener);
    }

    public void rotateAnim(View mImageView) {
        Animation anim = new RotateAnimation(22.5f, 382.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(4000); // 设置动画时
        anim.setRepeatCount(1000);
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        mImageView.startAnimation(anim);
    }
    /*
	 * // 检查当前使用的WiFi是否曾经用过 protected boolean checkworkSSIDUsed(String workSSID)
	 * { if (spf.contains("workSSID")) { if (spf.getString("workSSID",
	 * "").equals(workSSID)) { return true; } } return false; }
	 */

    // 屏蔽掉返回键
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            quitAlert(this);
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                quitAlert(this);
                break;

//            case R.id.choosesoft:
//
//                workSSID = etSSID.getText().toString();
//                workSSIDPsw = etPsw.getText().toString();
//
//                if (TextUtils.isEmpty(workSSID)) {
//                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title, Toast.LENGTH_SHORT)
//                            .show();
//
//                } else {
//                    if (TextUtils.isEmpty(workSSIDPsw)) {
//                        final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.show();
//
//                        Window window = dialog.getWindow();
//                        window.setContentView(R.layout.alert_gos_empty);
//
//                        LinearLayout llNo, llSure;
//                        llNo = (LinearLayout) window.findViewById(R.id.llNo);
//                        llSure = (LinearLayout) window.findViewById(R.id.llSure);
//
//                        llNo.setOnClickListener(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        llSure.setOnClickListener(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                toSoftApReady();
//                            }
//                        });
//                    } else {
//                        toSoftApReady();
//                    }
//
//                    break;
//                }
        }

        return true;
    }

    private void toSoftApReady() {
        // 需要记录所有配置过的wifi和密码

        try {
            String mypass = spf.getString("mypass", "");
            if (TextUtils.isEmpty(mypass)) {
                JSONObject mUserAndPass = new JSONObject();

                mUserAndPass.put(workSSID, workSSIDPsw);
                spf.edit().putString("mypass", mUserAndPass.toString()).commit();
            } else {
                JSONObject obj = new JSONObject(mypass);

                obj.put(workSSID, workSSIDPsw);

                spf.edit().putString("mypass", obj.toString()).commit();

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        spf.edit().putString("workSSID", workSSID).commit();
        spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

//        Intent intent = new Intent(this, GosDeviceReadyActivity.class);
//        startActivity(intent);
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.choosesoftap, menu);
//
//        return true;
//    }

    class WifiListAdapter extends BaseAdapter {

        ArrayList<ScanResult> xpgList;

        public WifiListAdapter(ArrayList<ScanResult> list) {
            this.xpgList = list;
        }

        @Override
        public int getCount() {
            return xpgList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(GosAirlinkChooseDeviceWorkWiFiActivity.this)
                        .inflate(R.layout.item_gos_wifi_list, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            String ssid = xpgList.get(position).SSID;
            holder.getTextView().setText(ssid);

            return view;
        }

    }

    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        TextView textView;

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) view.findViewById(R.id.SSID_text);
            }
            return textView;
        }

    }


    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        super.onSucceed(requestCode, grantPermissions);


        AlertDialog.Builder dia = new AlertDialog.Builder(GosAirlinkChooseDeviceWorkWiFiActivity.this);
        View view = View.inflate(GosAirlinkChooseDeviceWorkWiFiActivity.this, R.layout.alert_gos_wifi_list, null);
        ListView listview = (ListView) view.findViewById(R.id.wifi_list);
        List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
        List<String> localList = new ArrayList<String>();
        localList.clear();
        wifiList = new ArrayList<ScanResult>();
        wifiList.clear();
        for (ScanResult sss : rsList) {

            if (sss.SSID.contains(SoftAP_Start)) {
            } else {
                if (localList.toString().contains(sss.SSID)) {
                } else {
                    localList.add(sss.SSID);
                    wifiList.add(sss);
                }
            }
        }
        WifiListAdapter adapter = new WifiListAdapter(wifiList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ScanResult sResult = wifiList.get(arg2);
                String sSID = sResult.SSID;
                etSSID.setText(sSID);
                etPsw.setText("");
                create.dismiss();
            }
        });
        dia.setView(view);
        create = dia.create();
        create.show();

    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        super.onFailed(requestCode, deniedPermissions);
        {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(GosAirlinkChooseDeviceWorkWiFiActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(GosAirlinkChooseDeviceWorkWiFiActivity.this, REQUEST_CODE_SETTING).show();

                // 第二种：用自定义的提示语。
                // AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                // .setTitle("权限申请失败")
                // .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                // .setPositiveButton("好，去设置")
                // .show();

                // 第三种：自定义dialog样式。
                // SettingService settingService =
                //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
                // 你的dialog点击了确定调用：
                // settingService.execute();
                // 你的dialog点击了取消调用：
                // settingService.cancel();
            }

        }
    }
}
