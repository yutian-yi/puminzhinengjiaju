package pumin.com.pumin.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import pumin.com.pumin.R;
import pumin.com.pumin.activity.WorkListActivity;
import pumin.com.pumin.untils.OnLoadMoreListener;
import pumin.com.pumin.untils.SpUtils;

/**
 * Created by Administrator on 2017/11/2.
 */

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.MyViewHolder> {
    private final List<GizWifiDevice> mList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int indenx;
    private View view;
    private OnItemclickLisenter onItemclickLisenter;
    private OnLoadMoreListener onLoadMoreListener;
    private String TAG = "Adapter_Item";
    private String pm_switch = "";
    private String remark;
    private PopupWindow mCurPopupWindow;
    private PopupWindow popupWindow;

    public Adapter_Item(Context context, List<GizWifiDevice> list, int position) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.indenx = position;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType==1){
//            view = mLayoutInflater.inflate(R.layout.item_item2, parent, false);
//            return new MyViewHolder(view);
//        }else {
        view = mLayoutInflater.inflate(R.layout.item_item, parent, false);
        return new MyViewHolder(view);
//        }

//        MyViewHolder holder = new MyViewHolder(view);
//        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else if (position == 2) {
            return 1;
        }
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String trs = null;
        if (mList.get(position).getRemark().equals("yl_ktw_on,yl_ktw_off")) {
            trs = "空调外机";
        } else if (mList.get(position).getRemark().equals("yl_ktn_on,yl_ktn_off")) {
            trs = "空调内机";
        } else if (mList.get(position).getRemark().equals("yl_xf_on,yl_xf_off")) {
            trs = "新风";
        } else if (mList.get(position).getRemark().equals("yl_cn_on,yl_cn_off")) {
            trs = "采暖";
        } else if (mList.get(position).getRemark().equals("yl_lighting_switchon,yl_lighting_switchoff")) {
            trs = "办公照明";
        } else if (mList.get(position).getRemark().equals("yl_jg_on,yl_jg_off")) {
            trs = "景观";
        } else if (mList.get(position).getRemark().equals("yl_cp_on,yl_cp_off")) {
            trs = "草坪";
        } else if (mList.get(position).getRemark().equals("yl_ld_on,yl_ld_off")) {
            trs = "路灯";
        } else if (mList.get(position).getRemark().equals("yl_qd_on,yl_qd_off")) {
            trs = "墙灯";
        }
        holder.mItemTX.setText(trs);
        remark = mList.get(position).getRemark();
        GizWifiDeviceNetStatus netStatus = mList.get(position).getNetStatus();
        boolean online = mList.get(position).isOnline();
//        mList.get(position)
        Log.d(TAG, "====================== " + mList.get(position) + "==============isOnline=" + mList.get(position).isOnline());
//        if (online){
//            Log.d(TAG, "======================remark " +remark);
//            String[] split = remark.split(",");
//            String s = split[0];
//            Log.d(TAG, "======================s " +s);
//            int resourceByReflect = getResourceByReflect(s);
//            Log.d(TAG, "====================== " +resourceByReflect);
//            holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//            startFlick(holder.mItemIMG);
//        }

        String[] split = remark.split(",");
        Log.d(TAG, "3------------pm_switch = " + pm_switch.toString());

        getQuery(mList.get(position), holder, position);

        if (online) {//在线

            String s = split[0];
            int resourceByReflect = getResourceByReflect(s);
            holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//            holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner));
            holder.mItemTX.setTextColor(Color.BLACK);

            stopFlick(holder.mItemIMG);
            if (pm_switch.length() == 0) {
                holder.mFault.setVisibility(View.VISIBLE);
            } else {
                holder.mFault.setVisibility(View.GONE);

            }
        } else {//不在线
            String s = split[1];
            int resourceByReflect = getResourceByReflect(s);
            holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//            holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner2));
            holder.mItemTX.setTextColor(Color.LTGRAY);
            startFlick(holder.mItemIMG);

        }
        if (pm_switch != null) {
            if (pm_switch.equals("false")) {
//            mOff_on.setText("开");
            } else if (pm_switch.equals("true")) {
//            mOff_on.setText("关");
            }
        }


        Log.d("Adapter_Item", String.valueOf(netStatus));

        if (onItemclickLisenter != null) {
            holder.mItemIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*position删除增加，是变化的*/
                    onItemclickLisenter.onItemClick(v, holder.getLayoutPosition());
                }
            });
            holder.mItemIMG.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemclickLisenter.onItemLongClick(v, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"点击第"+indenx+"列第"+position+"个",Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, "点击第" + indenx + "列第" + mList.get(position).getDid() + "个", Toast.LENGTH_SHORT).show();
                GizWifiDevice gizWifiDevice = mList.get(position);
                setDeviceInfo(gizWifiDevice, holder,position);
                getQuery(gizWifiDevice, holder, position);
            }
        });
    }
        //执行订阅和查询操作
    public void getQuery(GizWifiDevice gizWifiDevice, final MyViewHolder holder, final int positon) {
        GizWifiDeviceListener mListenerD = new GizWifiDeviceListener() {
            @Override
            public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 订阅或解除订阅成功
//                            Toast.makeText(mContext,"订阅成功",Toast.LENGTH_SHORT).show();

                } else {
// 失败
//                            Toast.makeText(mContext,"订阅失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        String uid = SpUtils.getString(mContext, "Uid");
        String Token = SpUtils.getString(mContext, "Token");
        GizWifiSDK.sharedInstance().bindRemoteDevice (uid, Token, gizWifiDevice.getMacAddress(), gizWifiDevice.getProductKey(), "75eb9cfa894647d38466c8a70fe4ab36");
        gizWifiDevice.setListener(mListenerD);
        gizWifiDevice.setSubscribe(true);

        // mDevice是从设备列表中获取到的设备实体对象，设置监听
        gizWifiDevice.getDeviceStatus();
        // 实现回调
        GizWifiDeviceListener mListener = new GizWifiDeviceListener() {//获取设备状态

            @Override
            public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//                    Toast.makeText(mContext, "查询成功", Toast.LENGTH_SHORT).show();
                    // 数据解析与3.5.3相同
                    Log.d(TAG, "------------dataMap = " + dataMap);
                    Object data = dataMap.get("data");
                    try {
//                        Map<String, String> mapStr = JsonUtils.getMapStr(data.toString());
//                        pm_switch = mapStr.get("pm_switch");
                        Log.d(TAG, "1------------pm_switch = " + pm_switch.toString());
                        String[] split = data.toString().split("switch=");
                        pm_switch = split[1].substring(0, split[1].length() - 1);
                        Log.d(TAG, "2------------pm_switch = " + pm_switch.toString());
                        holder.mFault.setVisibility(View.GONE);

                        remark = mList.get(positon).getRemark();
                        String[] splitstr = remark.split(",");
//                        if (pm_switch!=null) {
                        if (pm_switch.equals("false")) {
                            String s = splitstr[1];
                            int resourceByReflect = getResourceByReflect(s);
                            holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//                            holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner2));
                            holder.mItemTX.setTextColor(Color.LTGRAY);//BLACK
                            stopFlick(holder.mItemIMG);
                        } else if (pm_switch.equals("true")) {
                            String s = splitstr[0];
                            int resourceByReflect = getResourceByReflect(s);
                            holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//                            holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner));
                            holder.mItemTX.setTextColor(Color.BLACK);

                            stopFlick(holder.mItemIMG);
                        }
//                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                            Toast.makeText(mContext, "查询成功" + dataMap, Toast.LENGTH_SHORT).show();
                } else {
                    // 查询失败
//                    Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
//                    holder.mFault.setVisibility(View.VISIBLE);

                    Log.d(TAG, "------------result = " + result);
                }
            }
        };
        gizWifiDevice.setListener(mListener);
    }

    /**
     * Description:点击弹出设备管理
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setDeviceInfo(final GizWifiDevice gizWifiDevice, final MyViewHolder holder, final int position) {

//        final Dialog mDialog = new android.app.AlertDialog.Builder(mContext,R.style.remarke).setView(new EditText(mContext)).create();
//        mDialog.setCancelable(true);
//        mDialog.show();
        LinearLayout mDingyue;
        LinearLayout mManager;
        LinearLayout mSetUp;

//        // 用于PopupWindow的View
//        View contentView = LayoutInflater.from(mContext).inflate(R.layout.alert_gos_set_device_info, null, false);
//        // 创建PopupWindow对象，其中：
//        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
//        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
//        final PopupWindow windows = new PopupWindow(contentView, 280, 480);
//        // 设置PopupWindow的背景
//        windows.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
//        // 设置PopupWindow是否能响应外部点击事件
//        // TODO: 2016/5/17 设置可以获取焦点
//        windows.setFocusable(true);
//        windows.setOutsideTouchable(true);
//        // 设置PopupWindow是否能响应点击事件
//        windows.setTouchable(true);
////        // 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
////        int[] location = new int[2];
////        holder.mItemIMG.getLocationOnScreen(location);
//
//        // 显示PopupWindow，其中：
//        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        int[] location = new int[2];
//        holder.mItem.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        if (y>display.getHeight()/2){
//            windows.showAsDropDown(holder.mItem, -25, -(holder.mItem.getHeight()+180));
//        }else {
//            windows.showAsDropDown(holder.mItem, -25, 0);
//        }
//        // TODO：更新popupwindow的状态
//        windows.update();
//        windows.showAtLocation(holder.mItem, Gravity.TOP, 0, 0);

//        Window window = mDialog.getWindow();
//        window.setContentView(R.layout.alert_gos_set_device_info);
//        int[] location = new int[2];
//        holder.mItemTX.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(dm);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        // 获取屏幕宽、高用
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        lp.x = x-460; // 新位置X坐标
//        lp.y = y-1200;
//        Log.d(TAG, "------------result = x = " + x+"y = "+y);


//        lp.width = (int) (display.getWidth() * 0.20); // 宽度设置为屏幕的0.65
//        lp.height = (int) (display.getHeight() * 0.12);
////        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
////

        final View contentView = LayoutInflater.from(holder.mItem.getContext()).inflate(R.layout.alert_gos_set_device_info, null);
        mCurPopupWindow = showTipPopupWindow(holder.mItem,contentView);
        mDingyue = (LinearLayout) contentView.findViewById(R.id.Dingyue_Linear);
        mManager = (LinearLayout) contentView.findViewById(R.id.Manager_linear);//管理设备
        mSetUp = (LinearLayout) contentView.findViewById(R.id.SetUp_Linear);
        mManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gizWifiDevice.isOnline() == true) {
                    gizWifiDevice.isOnline();
                    setDeviceInfo1(gizWifiDevice, holder);
                } else {
                    Toast.makeText(mContext, "当前设备离线，无法操作", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();

            }
        });
        mDingyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, WorkListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", gizWifiDevice);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                popupWindow.dismiss();

            }
        });
        mSetUp.setOnClickListener(new View.OnClickListener() {      // 解除绑定
            @Override
            public void onClick(View v) {
                dialog(gizWifiDevice,position);
                popupWindow.dismiss();
            }
        });
    }

    protected void dialog(final GizWifiDevice gizWifiDevice, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要解绑设备吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String uid = SpUtils.getString(mContext, "Uid");
                String Token = SpUtils.getString(mContext, "Token");
                GizWifiSDK.sharedInstance().unbindDevice(uid, Token, gizWifiDevice.getDid());
// 实现回调
                GizWifiSDKListener mListener = new GizWifiSDKListener() {
                    @Override
                    public void didUnbindDevice(GizWifiErrorCode result, String did) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                            // 解绑成功
                            Toast.makeText(mContext, "解绑成功", Toast.LENGTH_SHORT).show();
                            FacilityAdapter.devices = GizWifiSDK.sharedInstance().getDeviceList();
                            mList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            // 解绑失败
                            Toast.makeText(mContext, "解绑失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                };
                GizWifiSDK.sharedInstance().setListener(mListener);
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

    private void setDeviceInfo1(final GizWifiDevice gizWifiDevice, final MyViewHolder holder) {

        final Dialog mDialog = new android.app.AlertDialog.Builder(mContext).setView(new EditText(mContext)).create();
        mDialog.show();
        mDialog.setCancelable(true);
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.alert_off_on);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        lp.width = (int) (display.getWidth() * 0.60); // 宽度设置为屏幕的0.65
        window.setAttributes(lp);

        final Button mOff_on;
        Button mNo;
        mOff_on = (Button) window.findViewById(R.id.Off_On);
        mNo = (Button) window.findViewById(R.id.NO);


        if (pm_switch.equals("false")) {
            mOff_on.setText("开");

        } else if (pm_switch.equals("true")) {
            mOff_on.setText("关");

        }
        mOff_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 订阅设备并变为可控状态后，执行开灯动作
//                getFacilityState(gizWifiDevice);
                String[] split = remark.split(",");
                int sn = 5;
                ConcurrentHashMap<String, Object> command = new ConcurrentHashMap<String, Object>();
                if (pm_switch.equals("false")) {
                    command.put("pm_switch", true);
                    mOff_on.setText("开");
                    String s = split[0];
                    int resourceByReflect = getResourceByReflect(s);
                    holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
                    holder.mItemTX.setTextColor(Color.BLACK);
//                    holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner));
                } else if (pm_switch.equals("true")) {
                    command.put("pm_switch", false);
                    mOff_on.setText("关");
                    String s = split[1];
                    int resourceByReflect = getResourceByReflect(s);
                    holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//                    holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner2));
                    holder.mItemTX.setTextColor(Color.LTGRAY);

                }
                gizWifiDevice.write(command, sn);
// 实现回调
                GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
                    @Override
                    public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                            Object data = dataMap.get("data");
                            try {
//                        Map<String, String> mapStr = JsonUtils.getMapStr(data.toString());
//                        pm_switch = mapStr.get("pm_switch");
                                String[] split = data.toString().split("switch=");
                                pm_switch = split[1].substring(0, split[1].length() - 1);
                                Log.d(TAG, "------------pm_switch = " + pm_switch.toString());
//                                if (pm_switch.equals("false")) {
//                                    mOff_on.setText("开");
//                                    String s = split[1];
//                                    int resourceByReflect = getResourceByReflect(s);
//                                    holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//                                    holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner2));
//                                } else if (pm_switch.equals("true")) {
//                                    mOff_on.setText("关");
//                                    String s = split[0];
//                                    int resourceByReflect = getResourceByReflect(s);
//                                    holder.mItemIMG.setImageDrawable(mContext.getResources().getDrawable(resourceByReflect));
//                                    holder.mItemIMG.setBackground(mContext.getResources().getDrawable(R.drawable.shape_corner));
//
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (sn == 5) {
                                // 命令序号相符，开灯指令执行成功
//                                Toast.makeText(mContext, "执行成功", Toast.LENGTH_SHORT).show();
                            } else {
                                // 其他命令的ack或者数据上报
//                                Toast.makeText(mContext,"执行失败",Toast.LENGTH_SHORT).show();

                            }
                        } else {
// 操作失败
                            Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "------------result = " + result.toString());
                        }
                    }
                };
                // mDevice是从设备列表中获取到的设备实体对象，设置监听
                gizWifiDevice.setListener(mListener);

                mDialog.dismiss();
            }
        });
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTX;
        private final ImageView mItemIMG;
        private final ImageView mFault;
        private final LinearLayout mItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemTX = (TextView) view.findViewById(R.id.Item_TX);
            mItemIMG = (ImageView) view.findViewById(R.id.Item_IMG);
            mFault = (ImageView) view.findViewById(R.id.Fault_IMG);
            mItem = (LinearLayout) view.findViewById(R.id.item_item);
        }
    }

    /*view控件，在这里找到，自定义监听接口回调*/
    public interface OnItemclickLisenter {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

//    public void getFacilityState(GizWifiDevice mDevice) {
//        // mDevice是从设备列表中获取到的设备实体对象，设置监听
//        mDevice.getDeviceStatus();
//        // 实现回调
//        GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
//            @Override
//            public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
//                if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//                    // 数据解析与3.5.3相同
//                    Log.d(TAG, "------------dataMap = " + dataMap);
////                    Toast.makeText(mContext, "查询成功" + dataMap, Toast.LENGTH_SHORT).show();
//                } else {
//                    // 查询失败
//                    Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "------------result = " + result);
//                }
//            }
//        };
//        mDevice.setListener(mListener);
//
//    }

    /**
     * 开启View闪烁效果
     */
    private void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.4f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     */
    private void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public int getResourceByReflect(String imageName) {
        Class mipmap = R.mipmap.class;
        Field field = null;
        int r_id;
        try {
            field = mipmap.getField(imageName);
            r_id = field.getInt(field.getName());
        } catch (Exception e) {
            r_id = -1;
            Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id;
    }
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
        popupWindow.showAsDropDown(anchorView);

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
