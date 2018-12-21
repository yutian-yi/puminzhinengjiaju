package pumin.com.pumin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;

import pumin.com.pumin.context.MessageCenter;
import pumin.com.pumin.R;
import pumin.com.pumin.context.UIConfig;
import pumin.com.pumin.untils.SpUtils;

/*
* 主页 登录页面
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 0;
    private EditText etName, etPsw;
    private Button btnLogin;
    private CheckBox mJizhuPassword;
    private TextView tvRegister,tvRegister1, tvForget;
    private CheckBox cbLaws;
    private Intent intent;
    private String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        mJizhuPassword.setChecked(true);

        GizWifiSDK.sharedInstance().startWithAppID(this, UIConfig.app_id);
//        GizWifiSDK.sharedInstance().startWithAppID(this, "cf0077c6a6c245f2beb70e4151090b0b", "6fefa4237d0347d29083e8a8e7aed369", GosDeploy.setProductKeyList(), serverMap,
//                false);
        MessageCenter.getInstance(this);
        if (SpUtils.getString(MainActivity.this,"Token")!=null) {
            intent = new Intent(MainActivity.this, FacilityActivity.class);
            startActivity(intent);
        }
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        etName.setText(SpUtils.getString(this, "etName"));
        etPsw.setText(SpUtils.getString(this, "etPsws"));
        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
        mJizhuPassword.setChecked(SpUtils.getBoolean(this, "状态"));
        final Editable etNames = etName.getText();
        final Editable etPsws = etPsw.getText();
        if (SpUtils.getBoolean(this, "状态")) {
            SpUtils.putString(MainActivity.this, "etName", etNames.toString());
            SpUtils.putString(MainActivity.this, "etPsws", etPsws.toString());
        } else {
            SpUtils.putString(MainActivity.this, "etName", etNames.toString());
            SpUtils.putString(MainActivity.this, "etPsws", "");
        }
        mJizhuPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    SpUtils.putString(MainActivity.this, "etName", etNames.toString());
                    SpUtils.putString(MainActivity.this, "etPsws", etPsws.toString());
                    SpUtils.putBoolean(MainActivity.this, "状态", true);
                } else {
                    SpUtils.putString(MainActivity.this, "etName", etNames.toString());
                    SpUtils.putString(MainActivity.this, "etPsws", "");
                    SpUtils.putBoolean(MainActivity.this, "状态", false);

                }
            }
        });
        tvForget.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegister1.setOnClickListener(this);

    }

    private void initData() {

    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        etPsw = (EditText) findViewById(R.id.etPsw);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister1 = (TextView) findViewById(R.id.tvRegister1);

        tvForget = (TextView) findViewById(R.id.tvForget);
        mJizhuPassword = (CheckBox) findViewById(R.id.JizhuPassword_CheckBox);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Editable etNames = etName.getText();
        Editable etPsws = etPsw.getText();
        if (SpUtils.getBoolean(this, "状态")) {
            SpUtils.putString(MainActivity.this, "etName", etNames.toString());
            SpUtils.putString(MainActivity.this, "etPsws", etPsws.toString());
        } else {
            SpUtils.putString(MainActivity.this, "etName", etNames.toString());
            SpUtils.putString(MainActivity.this, "etPsws", "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:     //登录
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在登录，请稍候");
                progressDialog.show();
                Editable Name = etName.getText();
                Editable Password = etPsw.getText();
                GizWifiSDK.sharedInstance().userLogin(Name.toString(), Password.toString());
// 实现回调
                GizWifiSDKListener mListener = new GizWifiSDKListener() {
                    @Override
                    public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 登录成功
                            progressDialog.dismiss();
                            Log.d(TAG, "token = " + token + "------uid = " + uid + "------------1 = " + result.toString());
//                            spf.edit().putString("Uid", uid).commit();
//                            spf.edit().putString("Token", token).commit();
                            SpUtils.putString(MainActivity.this, "Token", token);
                            SpUtils.putString(MainActivity.this, "Uid", uid);
                            intent = new Intent(MainActivity.this, FacilityActivity.class);
                            startActivity(intent);
                        } else {
// 登录失败
                            if (result == GizWifiErrorCode.GIZ_SDK_CONNECTION_ERROR) {
                                ToastTools.short_Toast(MainActivity.this, "发生了连接错误");
                                progressDialog.dismiss();
                            }else if (result == GizWifiErrorCode.GIZ_SDK_REQUEST_TIMEOUT){
                                ToastTools.short_Toast(MainActivity.this, "SDK接口执行超时");
                                progressDialog.dismiss();
                            }
                        }
                    }
                };
                GizWifiSDK.sharedInstance().setListener(mListener);

                break;
            case R.id.tvRegister:   //注册新用户
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tvRegister1:   //注册新用户
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tvForget: //忘记密码
                intent = new Intent(MainActivity.this, SetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
