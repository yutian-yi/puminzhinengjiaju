package pumin.com.pumin.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.ArrayList;
import java.util.List;

import pumin.com.pumin.R;
import pumin.com.pumin.activity.FacilityActivity;
import pumin.com.pumin.untils.OnLoadMoreListener;
import pumin.com.pumin.untils.SpUtils;

/**
 * Created by Administrator on 2017/11/2.
 */

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.MyViewHolder> implements Adapter_Item.OnItemclickLisenter {

    private final List<String> mList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    public  Adapter_Item mRecycleAdapter;
    private View view;
    private OnItemclickLisenter onItemclickLisenter;
    private OnLoadMoreListener onLoadMoreListener;
    private int size;
    //    private List<GizWifiDevice> devices;
    private List<GizWifiDevice> F1list = new ArrayList<>();
    private List<GizWifiDevice> F2list = new ArrayList<>();
    private List<GizWifiDevice> F3list = new ArrayList<>();
    private List<GizWifiDevice> F4list = new ArrayList<>();
    private List<GizWifiDevice> MWlist = new ArrayList<>();
    private List<GizWifiDevice> SYLlist = new ArrayList<>();
    public  static List<GizWifiDevice> devices = new ArrayList<GizWifiDevice>();
    private int number=0;

    public FacilityAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mLayoutInflater.inflate(R.layout.item_facilityrecycler, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
//        mRecyclerView.setMinimumWidth(display.getWidth()/5);
        int height = FacilityActivity.height;
        view.getLayoutParams().height = (display.getHeight() - height) / 7;//设置item的高度

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mLouCengTX.setText(mList.get(position));
        if (onItemclickLisenter != null) {
            holder.mLouCengTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*position删除增加，是变化的*/
                    onItemclickLisenter.onItemClick(v, holder.getLayoutPosition());
                }
            });
            holder.mLouCengTX.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemclickLisenter.onItemLongClick(v, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        List list = new ArrayList();
        // 使用缓存的设备列表刷新UI
//        devices = GizWifiSDK.sharedInstance().getDeviceList();
// 接收设备列表变化上报，刷新UI
        GizWifiSDKListener mListener = new GizWifiSDKListener() {
            @Override
            public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
                // 提示错误原因
                if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//                    Log.d("FacilityAdapter", "result: = 3" + result.name());
                }
                devices = GizWifiSDK.sharedInstance().getDeviceList();
                // 显示变化后的设备列表
                Log.d("FacilityAdapter", "discovered deviceList: result: = 4" + deviceList);
//                devices = deviceList;
//                if (number>10) {
//                    mRecycleAdapter.notifyDataSetChanged();
//                }
//                number++;
                Message message = new Message();
                message.obtain(mHandler,1,position,3,holder);
                message.what = 1;
                mHandler.sendMessage(message);//发送消息 
// 以设备列表中的第一个设备实例为例，为其设置监听

                GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
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
                GizWifiDevice mDevice = null;
                for (int i = 0; i < deviceList.size(); i++) {
                    mDevice = deviceList.get(i);
                    String uid = SpUtils.getString(mContext, "Uid");
                    String Token = SpUtils.getString(mContext, "Token");
                    GizWifiSDK.sharedInstance().bindRemoteDevice (uid, Token, mDevice.getMacAddress(), mDevice.getProductKey(), "75eb9cfa894647d38466c8a70fe4ab36");
                    mDevice.setListener(mListener);
                    mDevice.setSubscribe(true);
                }
            }
        };
        GizWifiSDK.sharedInstance().setListener(mListener);
        devices = GizWifiSDK.sharedInstance().getDeviceList();
//        Log.d("FacilityAdapter", "devices:========= " + devices);
        for (int i = 0; i < devices.size(); i++) {
//            String alias = devices.get(i).getAlias();
            String remark = devices.get(i).getRemark();
            if (devices.get(i).getAlias().equals("四楼")) {
                if (!F1list.contains(devices.get(i))) {
                    F1list.add(devices.get(i));
                }
            } else if (devices.get(i).getAlias().equals("三楼")) {
                if (!F2list.contains(devices.get(i))) {
                    F2list.add(devices.get(i));
                }
            } else if (devices.get(i).getAlias().equals("二楼")) {
                if (!F3list.contains(devices.get(i))) {
                    F3list.add(devices.get(i));
                }
            } else if (devices.get(i).getAlias().equals("一楼")) {
                if (!F4list.contains(devices.get(i))) {
                    F4list.add(devices.get(i));
                }
            } else if (devices.get(i).getAlias().equals("门卫")) {
                if (!MWlist.contains(devices.get(i))) {
                    MWlist.add(devices.get(i));
                }
            } else if (devices.get(i).getAlias().equals("实验楼")) {
                if (!SYLlist.contains(devices.get(i))) {
                    SYLlist.add(devices.get(i));
                }
            }
//            else if (devices.get(i).getAlias().equals("")){
//                if (!F4list.contains(devices.get(i))) {
//                    F4list.add(devices.get(i));
//                }
//            }
        }

        if (devices.size() != 0) {
            if (position == 0) {
                size = SYLlist.size();
                mRecycleAdapter = new Adapter_Item(mContext, SYLlist, position);
            } else if (position == 1) {
                size = F1list.size();
                mRecycleAdapter = new Adapter_Item(mContext, F1list, position);
            } else if (position == 2) {
                size = F2list.size();
                mRecycleAdapter = new Adapter_Item(mContext, F2list, position);
            } else if (position == 3) {
                size = F3list.size();
                mRecycleAdapter = new Adapter_Item(mContext, F3list, position);
            } else if (position == 4) {
                size = F4list.size();
                mRecycleAdapter = new Adapter_Item(mContext, F4list, position);
            } else if (position == 5) {
                size = MWlist.size();
                mRecycleAdapter = new Adapter_Item(mContext, MWlist, position);
            }
//            if (position == 0) {
//                size = 1;
//            } else if (position == 1) {
//                size = 1;
//            } else if (position == 2) {
//                size = 1;
//            } else if (position == 3) {
//                size = 1;
//            } else if (position == 4) {
//                size = MWlist.size();
//            }
//        else {
//            size=7;
//        }
//            for (int i = 0; i < size; i++) {
//                list.add("");
//            }
            final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            //设置布局管理器、
            holder.mRecycler_Item.setLayoutManager(layoutManager);

            //设置为垂直布局，这也是默认的
            layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            //设置Adapter、
            holder.mRecycler_Item.setAdapter(mRecycleAdapter);
            //设置增加或删除条、目的动画
            holder.mRecycler_Item.setItemAnimator(new DefaultItemAnimator());
                     /*设置itemView监听器*/
//        mRecycleAdapter.setOnItemclickLisenter(mContext);
            mRecycleAdapter.notifyDataSetChanged();

        }
    }

    public void setOnItemclickLisenter(OnItemclickLisenter onItemclickLisenter) {
        this.onItemclickLisenter = onItemclickLisenter;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(mContext, "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mLouCengTX;
        private final RecyclerView mRecycler_Item;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLouCengTX = (TextView) view.findViewById(R.id.LouCeng_TX);
            mRecycler_Item = (RecyclerView) view.findViewById(R.id.FacilityRecycler_item);

        }
    }

    /*view控件，在这里找到，自定义监听接口回调*/
    public interface OnItemclickLisenter {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {//覆盖handleMessage方法
            switch (msg.arg1) {//根据收到的消息的what类型处理
                case 1:
                    Log.v("handler", "result: = ===="+msg.arg1);//打印收到的消息
                    break;
                default:
                    super.handleMessage(msg);//这里最好对不需要或者不关心的消息抛给父类，避免丢失消息
                    break;
            }
        }
    };
}
