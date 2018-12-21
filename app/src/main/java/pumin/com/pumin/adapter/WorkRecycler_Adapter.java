package pumin.com.pumin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizDeviceScheduler;
import com.gizwits.gizwifisdk.enumration.GizScheduleWeekday;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import pumin.com.pumin.R;
import pumin.com.pumin.activity.WorkListActivity;
import pumin.com.pumin.untils.OnLoadMoreListener;

/**
 * Created by Administrator on 2017/11/14.
 */

public class WorkRecycler_Adapter extends RecyclerView.Adapter<WorkRecycler_Adapter.MyViewHolder> {
    private final List<GizDeviceScheduler> mList;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    public List weekList = new ArrayList();
    private String TAG = "WorkRecycler_Adapter";
    private OnItemclickLisenter onItemclickLisenter;
    private OnLoadMoreListener onLoadMoreListener;
    private String replace;

    public WorkRecycler_Adapter(Context context, List<GizDeviceScheduler> mWorkList) {
        this.mList = mWorkList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_work_recycler, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }
    public void setOnItemclickLisenter(OnItemclickLisenter onItemclickLisenter) {
        this.onItemclickLisenter = onItemclickLisenter;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (onItemclickLisenter != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*position删除增加，是变化的*/
                    onItemclickLisenter.onItemClick(v, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemclickLisenter.onItemLongClick(v, holder.getLayoutPosition());
                    return true;
                }
            });
        }

        holder.mRemarkTX.setText(mList.get(position).getRemark());
        if (mList.get(position).getDate().length()==0) {
            String Weekstring = mList.get(position).getWeekdays().toString().substring(1, mList.get(position).getWeekdays().toString().length() - 1);
            Log.d(TAG, "Weekstring:" + Weekstring);
            if (!Weekstring.contains(", ")){
                if (Weekstring.equals("GizScheduleMonday")) {
//                        weekDays.add(GizScheduleWeekday.GizScheduleMonday);
//                    weekList.add("星期一");
                    holder.mWorkDate.setText("星期一");
                } else if (Weekstring.equals("GizScheduleTuesday")) {
//                    weekList.add("星期二");
//                        weekDays.add(GizScheduleWeekday.GizScheduleTuesday);
                    holder.mWorkDate.setText("星期二");
                } else if (Weekstring.equals("GizScheduleWednesday")) {
//                    weekList.add("星期三");
//                        weekDays.add(GizScheduleWeekday.GizScheduleWednesday);
                    holder.mWorkDate.setText("星期三");
                } else if (Weekstring.equals("GizScheduleThursday")) {
//                    weekList.add("星期四");
//                        weekDays.add(GizScheduleWeekday.GizScheduleThursday);
                    holder.mWorkDate.setText("星期四");
                } else if (Weekstring.equals("GizScheduleFriday")) {
//                    weekList.add("星期五");
//                        weekDays.add(GizScheduleWeekday.GizScheduleFriday);
                    holder.mWorkDate.setText("星期五");
                } else if (Weekstring.equals("GizScheduleSaturday")) {
//                    weekList.add("星期六");
//                        weekDays.add(GizScheduleWeekday.GizScheduleSaturday);
                    holder.mWorkDate.setText("星期六");
                } else if (Weekstring.equals("GizScheduleSunday")) {
//                    weekList.add("星期日");
//                        weekDays.add(GizScheduleWeekday.GizScheduleSunday);
                    holder.mWorkDate.setText("星期日");
                }
            }else {
                String[] split = Weekstring.split(", ");
                Log.d(TAG, "Weekstring:" + Weekstring);
//                for (int i = 0; i < split.length; i++) {
//                    Log.d(TAG, "split:" + split[i]);
//                    if (split[i].equals("GizScheduleMonday")) {
////                        weekDays.add(GizScheduleWeekday.GizScheduleMonday);
//                            weekList.add(0,"星期一");
//                    } else if (split[i].equals("GizScheduleTuesday")) {
////                        if (!weekList.contains("星期二")){
////                            weekList.add(0,"星期一");
////                        }
//                        weekList.add(1,"星期二");
////                        weekDays.add(GizScheduleWeekday.GizScheduleTuesday);
//                    } else if (split[i].equals("GizScheduleWednesday")) {
//                        weekList.add(2,"星期三");
////                        weekDays.add(GizScheduleWeekday.GizScheduleWednesday);
//                    } else if (split[i].equals("GizScheduleThursday")) {
//                        weekList.add(3,"星期四");
////                        weekDays.add(GizScheduleWeekday.GizScheduleThursday);
//                    } else if (split[i].equals("GizScheduleFriday")) {
//                        weekList.add(4,"星期五");
////                        weekDays.add(GizScheduleWeekday.GizScheduleFriday);
//                    } else if (split[i].equals("GizScheduleSaturday")) {
//                        weekList.add(5,"星期六");
////                        weekDays.add(GizScheduleWeekday.GizScheduleSaturday);
//                    } else if (split[i].equals("GizScheduleSunday")) {
//                        weekList.add(6,"星期日");
////                        weekDays.add(GizScheduleWeekday.GizScheduleSunday);
//                    }
//                }
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
                Log.d(TAG, "weekList:" + weekList);
                holder.mWorkDate.setText(Weekstring);
            }

        }else {
            holder.mWorkDate.setText(mList.get(position).getDate());
        }
        holder.mWorkTime.setText(mList.get(position).getTime());
        ConcurrentHashMap<String, Object> attrs = mList.get(position).getAttrs();
        Object pm_switch = attrs.get("pm_switch");
        if (pm_switch.toString().equals("true")) {
            holder.mWorkOff_On.setText("开");
        }else if (pm_switch.toString().equals("false")){
            holder.mWorkOff_On.setText("关");

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    /*view控件，在这里找到，自定义监听接口回调*/
    public interface OnItemclickLisenter {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRemarkTX;
        private final TextView mWorkDate;
        private final TextView mWorkTime;
        private final TextView mWorkOff_On;

        public MyViewHolder(View itemView) {
            super(itemView);
            mRemarkTX = (TextView)itemView.findViewById(R.id.WorkRemark_TX);
            mWorkDate = (TextView)itemView.findViewById(R.id.WorkDate_TX);
            mWorkTime = (TextView)itemView.findViewById(R.id.WorkTime_TX);
            mWorkOff_On = (TextView)itemView.findViewById(R.id.WorkOff_On_TX);

        }
    }
}
