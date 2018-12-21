package pumin.com.pumin.untils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SpUtils {

    private final static String NAME = "fansan";
    private static SharedPreferences mPreferences;

    /**
     * 获得preference
     *
     * @param context
     * @return
     */
    private static SharedPreferences getPreferences(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return mPreferences;
    }

    /**
     * 获得boolean类型的数据,如果没有返回false
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 获得boolean类型的数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        // 频繁的读文件
        SharedPreferences sp = getPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 存储boolean数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获得String类型的数据,如果没有返回null
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 获得String类型的数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        // 频繁的读文件
        SharedPreferences sp = getPreferences(context);
        return sp.getString(key, defValue);
    }

    /**
     * 存储String数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获得int类型的数据,如果没有返回-1
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * 获得int类型的数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        // 频繁的读文件
        SharedPreferences sp = getPreferences(context);
        return sp.getInt(key, defValue);
    }

    /**
     * 存储int数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获得long类型的数据,如果没有返回-1
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * 获得long类型的数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        // 频繁的读文件
        SharedPreferences sp = getPreferences(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 存储long数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void putStringSet(Context context, String key, Set<String> value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences sp = getPreferences(context);
        return sp.getStringSet(key, null);
    }

    //    //  存储用户选择的 网络状态
//    public static void setNetWorkOption(Context context,Boolean isWiFi){
//
//    }
//    public static Boolean Option (Context context ){
//        return null;
//    }
    public static void putNetwork(Context context, String state) {
        context.getSharedPreferences("NET_WORK", Context.MODE_PRIVATE).edit().putString("state", state).commit();
    }

    public static String getNetwork(Context context) {
        return context.getSharedPreferences("NET_WORK", Context.MODE_PRIVATE).getString("state", "");
    }


}
