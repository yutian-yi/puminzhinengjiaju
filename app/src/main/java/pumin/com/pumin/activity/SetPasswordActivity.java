package pumin.com.pumin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pumin.com.pumin.R;
import pumin.com.pumin.context.UIConfig;
import pumin.com.pumin.untils.SpUtils;

/*
* 找回密码页面
* */
public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mRegisterClose;
    private EditText etName,etCode,etPsw;
    private Button btnGetCode;
    private Button btnRrgister;
    private CheckBox cbLaws;
    private MyCountDownTimer myCountDownTimer;
    private String TAG = "SetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000);

        mRegisterClose.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        btnRrgister.setOnClickListener(this);
        cbLaws.setOnClickListener(this);

        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
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

    }

    private void initView() {
        etName = (EditText) findViewById(R.id.Forget_etName);
        btnGetCode = (Button) findViewById(R.id.Forget_btnGetCode);
        etCode = (EditText) findViewById(R.id.Forget_etCode);
        etPsw = (EditText) findViewById(R.id.Forget_etPsw);
        btnRrgister = (Button) findViewById(R.id.Forget_btnRrgister);
        cbLaws = (CheckBox) findViewById(R.id.Forget_cbLaws);

        mRegisterClose = (ImageView)findViewById(R.id.Register_left_IMG);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Register_left_IMG:
                SetPasswordActivity.this.finish();
                break;
            case R.id.Forget_btnGetCode://获取验证码
                Editable text = etName.getText();
                if (text.length() != 0 && text.length() == 11) {
                    myCountDownTimer.start();
                    GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(UIConfig.app_secret, text.toString());
// 实现回调
                    GizWifiSDKListener mListener = new GizWifiSDKListener() {
                        @Override
                        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
                            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 请求成功
                                Log.d(TAG, token + "------------1 = " + result.toString());
                            } else {
// 请求失败
                                Log.d(TAG, token + "------------2 = " + result.toString());
                            }
                        }
                    };
                    GizWifiSDK.sharedInstance().setListener(mListener);
                } else {
                    Toast.makeText(this, "您输入的手机号有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Forget_btnRrgister:   // 修改密码
                final Editable phoneNum = etName.getText();
                Editable yanzhengma = etCode.getText();
                final Editable password = etPsw.getText();
                boolean b = judgeContainsStr(password.toString());
                boolean b1 = judgeContainsStr1(password.toString());
                boolean containNumber = isContainNumber(password.toString());
                if (!b1|!b|!containNumber){
                    Toast.makeText(SetPasswordActivity.this, "密码需要包含大小写字母和数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                GizWifiSDK.sharedInstance().resetPassword(phoneNum.toString(), yanzhengma.toString(), password.toString(), GizUserAccountType.GizUserPhone);
// 实现回调

                GizWifiSDKListener mListener = new GizWifiSDKListener() {
                    @Override
                    public  void didChangeUserPassword(GizWifiErrorCode result) {
                        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
// 修改成功
                            Log.d(TAG, "------------3 = " + result.toString());
                            SpUtils.putString(SetPasswordActivity.this, "etName", phoneNum.toString());
                            SpUtils.putString(SetPasswordActivity.this, "etPsws", password.toString());
                            Intent intent = new Intent(SetPasswordActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SetPasswordActivity.this,"修改成功", Toast.LENGTH_SHORT).show();

                        } else {
// 修改失败
                            Log.d(TAG, "------------4 = " + result.toString());
                            Toast.makeText(SetPasswordActivity.this,"修改失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                };
                GizWifiSDK.sharedInstance().setListener(mListener);
                break;

        }
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btnGetCode.setClickable(false);
            btnGetCode.setText(l / 1000 + "s");
            btnGetCode.setBackgroundColor(Color.parseColor("#aaaaaa"));

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnGetCode.setText("重新发送");
            //设置可点击
            btnGetCode.setClickable(true);
            btnGetCode.setBackgroundColor(Color.parseColor("#a7292a"));

        }

    }
    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     * @author fenggaopan 2015年7月21日 上午9:49:40
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     */
    public boolean judgeContainsStr(String cardNum) {
        String regex=".*[A-Z]+.*";
        Matcher m= Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }
    public boolean judgeContainsStr1(String cardNum) {
        String regex=".*[a-z]+.*";
        Matcher m= Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }
    public static boolean isContainNumber(String company) {

        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(company);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
