package pumin.com.pumin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pumin.com.pumin.CofigModule.GosControlModuleBaseActivity;
import pumin.com.pumin.R;
import pumin.com.pumin.context.MessageCenter;
import pumin.com.pumin.untils.SpUtils;

public class SetRemarkActivity extends GosControlModuleBaseActivity implements View.OnClickListener {

    //    private EditText etRemark;
    private Button etAlias, etRemark;
    //    private LinearLayout llNo;
    private Button llSure;
    private List<GizWifiDevice> devices;
    private GizWifiDevice mDevice;

    //    private String[] areas = new String[]{"1F", "2F", "3F", "4F", "门卫"};//别名
    private String[] areas = new String[]{"实验楼", "四楼", "三楼", "二楼", "一楼", "门卫"};//别名
    private String[] areas1 = new String[]{"空调外机", "空调内机", "新风", "采暖", "办公照明", "景观", "草坪", "路灯", "墙灯"};// remake
    public List remake = new ArrayList();
    public Map<String, String> facilityMap = new HashMap<>();
    private RadioOnClick OnClick = new RadioOnClick(-1);
    private RadioOnClick1 OnClick1 = new RadioOnClick1(-1);
    private ListView areaListView;
    private ImageView mClose;
    private ImageView mHome;
    private ImageView mConsult;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_remark);
        initView();
        initData();
        initListener();
        MessageCenter.getInstance(this);

        etAlias.setOnClickListener(new RadioClickListener());
        etRemark.setOnClickListener(new RadioClickListener1());
    }

    class RadioClickListener implements View.OnClickListener {

        private PopupWindow mCurPopupWindow;
        private LinearLayout mLinear1,mLinear2,mLinear3,mLinear4,mLinear5,mLinear6;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
//            AlertDialog ad = new AlertDialog.Builder(SetRemarkActivity.this,R.style.remarke)
//                    .setSingleChoiceItems(areas, OnClick.getIndex(), OnClick).create();
//            areaListView = ad.getListView();
//            ad.show();
//
//           /*随意定义个Dialog*/
//            Window dialogWindow = ad.getWindow();
//
///*实例化Window*/
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
///*实例化Window操作者*/
//            int[] location = new int[2];
//            etAlias.getLocationOnScreen(location);
//            int x = location[0];
//            int y = location[1];
//            // 获取屏幕宽、高用
//            WindowManager wm = (WindowManager) SetRemarkActivity.this
//                    .getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//            Log.d("location", "location =  x = " + x+"--------------y = "+y);
//            lp.x = x; // 新位置X坐标
//            lp.y = y-(int) (display.getHeight() * 0.15); // 新位置Y坐标
////            dialogWindow.setAttributes(lp);
//
//
//            lp.width = (int) (display.getWidth() * 0.40); // 宽度设置为屏幕的0.65
//            lp.height = (int) (display.getHeight() * 0.55);
//            dialogWindow.setAttributes(lp);
//            dialogWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_bg_shape_white));

/*放置属性*/
/*show dialog*/
//            ad.show();
//            Toast.makeText(SetRemarkActivity.this, "当前设备离线，无法操作", Toast.LENGTH_SHORT).show();

            final View contentView = LayoutInflater.from(etAlias.getContext()).inflate(R.layout.popup_content_layout, null);
            mCurPopupWindow = showTipPopupWindow(etAlias, contentView);
            mLinear1 = (LinearLayout)contentView.findViewById(R.id.Linear_1);
            mLinear2 = (LinearLayout)contentView.findViewById(R.id.Linear_2);
            mLinear3 = (LinearLayout)contentView.findViewById(R.id.Linear_3);
            mLinear4 = (LinearLayout)contentView.findViewById(R.id.Linear_4);
            mLinear5 = (LinearLayout)contentView.findViewById(R.id.Linear_5);
            mLinear6 = (LinearLayout)contentView.findViewById(R.id.Linear_6);
            mLinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("实验楼");
                    popupWindow.dismiss();
                }
            });
            mLinear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("四楼");
                    popupWindow.dismiss();
                }
            });
            mLinear3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("三楼");
                    popupWindow.dismiss();
                }
            });
            mLinear4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("二楼");
                    popupWindow.dismiss();
                }
            });
            mLinear5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("一楼");
                    popupWindow.dismiss();
                }
            });
            mLinear6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAlias.setText("门卫");
                    popupWindow.dismiss();
                }
            });
        }
    }

    class RadioClickListener1 implements View.OnClickListener {
        private PopupWindow mCurPopupWindow;
        private ListView mTypeList;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
//            AlertDialog ad = new AlertDialog.Builder(SetRemarkActivity.this, R.style.remarke)
//                    .setSingleChoiceItems(areas1, OnClick1.getIndex(), OnClick1).create();
//            areaListView = ad.getListView();
//
//                     /*随意定义个Dialog*/
//            Window dialogWindow = ad.getWindow();
//
///*实例化Window*/
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
///*实例化Window操作者*/
//            int[] location = new int[2];
//            etRemark.getLocationOnScreen(location);
//            int height = etRemark.getHeight();
//            int x = location[0];
//            int y = location[1];
//            // 获取屏幕宽、高用
//            WindowManager wm = (WindowManager) SetRemarkActivity.this
//                    .getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//            Log.d("location", "location =  x = " + x + "--------------y = " + y + "height = " + height);
//            lp.x = x; // 新位置X坐标
//            lp.y = y - (int) (display.getHeight() * 0.20); // 新位置Y坐标
////            dialogWindow.setAttributes(lp);
//
//
//            lp.width = (int) (display.getWidth() * 0.45); // 宽度设置为屏幕的0.65
//            lp.height = (int) (display.getHeight() * 0.45);
//            dialogWindow.setAttributes(lp);
////            dialogWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_bg_shape_white));
//            ad.show();

            final View contentView = LayoutInflater.from(etRemark.getContext()).inflate(R.layout.popup_content_type, null);
            mTypeList = (ListView)contentView.findViewById(R.id.Type_List);
            MyAdapter adapter = new MyAdapter(SetRemarkActivity.this);
            mTypeList.setAdapter(adapter);
            mCurPopupWindow = showTipPopupWindow(etRemark, contentView);
            mTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    etRemark.setText(areas1[position]);
                    popupWindow.dismiss();
                }
            });
        }
    }
    //ViewHolder静态类
    static class ViewHolder
    {
        public TextView title;
    }
    public class MyAdapter extends BaseAdapter{

        private final LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return areas1.length;
        }

        @Override
        public Object getItem(int position) {
            return areas1[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.popup_list_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.type_item_text);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.title.setText(areas1[position]);

            return convertView;
        }
    }
    class RadioOnClick1 implements DialogInterface.OnClickListener {
        private int index;

        public RadioOnClick1(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
//            etRemark.setText(areas1[index]);
            etRemark.setText(areas1[index]);

//            Toast.makeText(SetRemarkActivity.this, "您已经选择了 " + ":" + areas1[index], Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    class RadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        public RadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            etAlias.setText(areas[index]);
//            Toast.makeText(SetRemarkActivity.this, "您已经选择了 " + ":" + areas[index], Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    private void initListener() {
        mClose.setOnClickListener(this);
        mHome.setOnClickListener(this);
    }

    private void initData() {
        facilityMap.put("空调外机", "yl_ktw_on,yl_ktw_off");
        facilityMap.put("空调内机", "yl_ktn_on,yl_ktn_off");
        facilityMap.put("新风", "yl_xf_on,yl_xf_off");
        facilityMap.put("采暖", "yl_cn_on,yl_cn_off");
        facilityMap.put("办公照明", "yl_lighting_switchon,yl_lighting_switchoff");
        facilityMap.put("景观", "yl_jg_on,yl_jg_off");
        facilityMap.put("草坪", "yl_cp_on,yl_cp_off");
        facilityMap.put("路灯", "yl_ld_on,yl_ld_off");
        facilityMap.put("墙灯", "yl_qd_on,yl_qd_off");
    }

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    private void initView() {
        mClose = (ImageView) findViewById(R.id.close_IMG);
        mHome = (ImageView) findViewById(R.id.Homepage_IMG);
        mConsult = (ImageView) findViewById(R.id.consult_IMG);
        mConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetRemarkActivity.this, ExplainActivity.class);
                startActivity(intent);
            }
        });
        // 使用缓存的设备列表刷新UI
        devices = GizWifiSDK.sharedInstance().getDeviceList();
// 接收设备列表变化上报，刷新UI
        GizWifiSDKListener mListener = new GizWifiSDKListener() {
            @Override
            public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
                // 提示错误原因
                if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    Log.d("", "result: " + result.name());
                }
                // 显示变化后的设备列表
                Log.d("", "discovered deviceList: " + deviceList);
                devices = deviceList;
            }
        };
        GizWifiSDK.sharedInstance().setListener(mListener);

//        final Dialog mDialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
//		mDialog.show();
        etAlias = (Button) findViewById(R.id.etAlias);
        etRemark = (Button) findViewById(R.id.etRemark);

//        llNo = (LinearLayout) findViewById(R.id.llNo);
        llSure = (Button) findViewById(R.id.llSure);

//        llNo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                mDialog.dismiss();
//                Intent intent1 = new Intent(SetRemarkActivity.this,FacilityActivity.class);
//                startActivity(intent1);
//                SetRemarkActivity.this.finish();
//            }
//        });
        Intent intent = getIntent();
        final String mdid = intent.getStringExtra("mdid");
        Log.d("SetRemarkActivity", "mdid = " + mdid);//uVQkiwCfH2pLEu6Bspp8Sa
        for (int i = 0; i < devices.size(); i++) {
            try {
//                        Map<String, String> mapStr = JsonUtils.getMapStr(devices.get(i).toString());
//                String[] split = devices.get(i).toString().split(",");
//                String[] split1 = split[1].split("=");
                String did = devices.get(i).getDid();
                Log.d("SetRemarkActivity", "mapStr.get(\"did\") = " + did);
                if (did.equals(mdid)) {
                    mDevice = devices.get(i);
                    if (!TextUtils.isEmpty(mDevice.getAlias())) {
//                        setEditText(etAlias, mDevice.getAlias());
                        etAlias.setText(mDevice.getAlias());
                    }
                    if (!TextUtils.isEmpty(mDevice.getRemark())) {
//                        setEditText(etRemark, mDevice.getRemark());
                        etRemark.setText(areas1[i]);
// facilityMap.get(areas1[index])
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if (!TextUtils.isEmpty(mDevice.getAlias())) {
//            setEditText(etAlias, mDevice.getAlias());
//        }
//        if (!TextUtils.isEmpty(mDevice.getRemark())) {
//            setEditText(etRemark, mDevice.getRemark());
//        }
        String uid = SpUtils.getString(this, "Uid");
        String Token = SpUtils.getString(this, "Token");

        GizWifiSDK.sharedInstance().bindRemoteDevice (uid, Token, mDevice.getMacAddress(), mDevice.getProductKey(), "75eb9cfa894647d38466c8a70fe4ab36");
// 实现回调、
        GizWifiSDKListener mListener2 = new GizWifiSDKListener() {
            @Override
            public void didBindDevice(GizWifiErrorCode result, String did) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                    // 绑定成功
//                    myToast("绑定成功");
                } else {
                    // 绑定失败
                    myToast("绑定失败" + result.toString());
                    Log.d("SetRemarkActivity", "绑定失败 = " + result);
                }
            }
        };
        GizWifiSDK.sharedInstance().setListener(mListener2);

        Log.d("SetRemarkActivity", "mDevice = " + mDevice);
        llSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etRemark.getText().toString())
                        && TextUtils.isEmpty(etAlias.getText().toString())) {
                    myToast("请输入设备别名或备注！");
                    return;
                }
                Log.d("SetRemarkActivity", "mDevice2 = " + mDevice);//etRemark.getText().toString()
                mDevice.setCustomInfo(facilityMap.get(etRemark.getText()), etAlias.getText().toString());
                /*mDevice = GizWifiDevice [macAddress=A020A621D54A, did=uVQkiwCfH2pLEu6Bspp8Sa, isLAN=true, netStatus=GizDeviceOnline,
                *  productType=GizDeviceNormal, productKey=7997721ec9794b189d57731fa296d238, productName=小风扇, isBind=true, mListener=null, alias=pumin123]*/
//                mDevice.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
////                mDialog.dismiss();
//                String loadingText = (String) getText(R.string.loadingtext);
//                progressDialog.setMessage(loadingText);
//                myToast("设置成功");
//                progressDialog.show();
                // 实现回调
                GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
                    @Override
                    public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 修改成功
                            myToast("设置成功");
                            Intent intent1 = new Intent(SetRemarkActivity.this, FacilityActivity.class);
                            startActivity(intent1);
                            SetRemarkActivity.this.finish();
                        } else {
// 修改失败
                            myToast("设置失败：" + result.name());
                        }
                    }
                };
                mDevice.setListener(mListener);
            }
        });

//        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                hideKeyBoard();
//            }
//        });
    }

    private void setEditText(Button et, Object value) {
        et.setText(value.toString());
        et.setText(value.toString().length());
        et.clearFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_IMG:
                finish();
                break;
            case R.id.Homepage_IMG:
                Intent intent = new Intent(this, FacilityActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*
 * 设置设备别名和备注回调
 */
//    @Override
//    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
//        super.didSetCustomInfo(result, device);
//        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
//            myToast("设置成功");
//            progressDialog.cancel();
//            finish();
//        } else {
//            myToast("设置失败：" + result.name());
//        }
//    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PopupWindow showTipPopupWindow(final View anchorView, final View contentView) {
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
//                onClickListener.onClick(v);
            }
        });

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                autoAdjustArrowPos(popupWindow, contentView, anchorView);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        popupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });
        // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
        // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
        WindowManager wm = (WindowManager) SetRemarkActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        popupWindow.showAsDropDown(anchorView, display.getWidth()/3-10, 0);
        return popupWindow;
    }

    private void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
        View upArrow = contentView.findViewById(R.id.up_arrow);
        View downArrow = contentView.findViewById(R.id.down_arrow);

        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
        upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

        RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
        upArrowParams.leftMargin = arrowLeftMargin;
        RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
        downArrowParams.leftMargin = arrowLeftMargin;
    }
}
